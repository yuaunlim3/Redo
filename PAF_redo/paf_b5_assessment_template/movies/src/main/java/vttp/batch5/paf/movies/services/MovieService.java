package vttp.batch5.paf.movies.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.json.data.JsonDataSource;
import vttp.batch5.paf.movies.Constants;
import vttp.batch5.paf.movies.models.Directors;
import vttp.batch5.paf.movies.models.MovieDetails;
import vttp.batch5.paf.movies.models.MovieStats;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Service
public class MovieService {

  @Value("${spring.data.name}")
  private String name;

  @Value("${spring.data.batch}")
  private String batch;
  @
  Autowired
  private MySQLMovieRepository sqlRepo;

  @Autowired private MongoMovieRepository mongoRepo;

  public List<JsonObject> unzipFile() throws IOException {
    String zipFilePath = "data/movies_post_2010.zip";
    File file = new File(zipFilePath);

    List<JsonObject> moviesJson = new LinkedList<>();
    ZipEntry entry;

    try {
      FileInputStream fis = new FileInputStream(file);
      ZipInputStream zis = new ZipInputStream(fis);
      while ((entry = zis.getNextEntry()) != null) {
        BufferedReader br = new BufferedReader(new InputStreamReader(zis));
        String line;
        while ((line = br.readLine()) != null) {
          JsonReader jsonReader = Json.createReader(new StringReader(line));
          JsonObject jsonObject = jsonReader.readObject();
          String date = jsonObject.getString("release_date");
          LocalDate localDate = LocalDate.parse(date);
          int year = localDate.getYear();
          if (year > 2018) {
            moviesJson.add(jsonObject);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return moviesJson;
  }
  // TODO: Task 2

  public Boolean check(JsonObject json) {
    String id = json.getString(Constants.ID);
    return sqlRepo.check(id);

  }
  @Transactional
  public void upload(List<JsonObject> movies){
    List<MovieDetails> movieDetails = movies.stream().map(MovieDetails::fromJson).collect(Collectors.toList());
    List<MovieStats> movieStats = movies.stream().map(MovieStats::fromJson).collect(Collectors.toList());

    try{
      sqlRepo.batchInsertMovies(movieStats);
      mongoRepo.batchInsertMovies(movieDetails);
    }catch(Exception ex){
      List<String> ids = new LinkedList<>();
      for(MovieDetails movie:movieDetails){
        ids.add(movie.getImdb_id());
      }
      mongoRepo.logError(ex,ids);
    }


  }

  // TODO: Task 3
  // You may change the signature of this method by passing any number of
  // parameters
  // and returning any type
  public List<Directors> getProlificDirectors(int limit) {
    List<Directors> directors = new LinkedList<>();

    List<Document> movieDetails = mongoRepo.getDirectors(limit);
    for(Document movie:movieDetails){
      Directors director = new Directors();
      director.setName(movie.getString("_id"));
      director.setMovies(movie.getInteger("movie_count"));
      List<String> ids = movie.getList("imdb_ids", String.class);
      for(String id:ids){
        director = sqlRepo.getInfo(director, id);
      }

      directors.add(director);
    }

    return directors;
  }

  // TODO: Task 4
  // You may change the signature of this method by passing any number of
  // parameters
  // and returning any type
  public byte[] generatePDFReport(int count) throws Exception {

    JsonObject reportRoot = Json.createObjectBuilder()
        .add("name", name)
        .add("batch", batch)
        .build();

    JsonArrayBuilder builder = Json.createArrayBuilder();
    getProlificDirectors(count).stream()
        .map(Directors::forReport)
        .forEach(builder::add);

    JsonArray directors = builder.build();

    System.out.printf(">>>> directors: %s\n", directors);

    JsonDataSource reportDS = new JsonDataSource(
          new ByteArrayInputStream(reportRoot.toString().getBytes()));
    JsonDataSource directorDS = new JsonDataSource(
          new ByteArrayInputStream(directors.toString().getBytes()));

    Map<String, Object> params = new HashMap<>();
    params.put("DIRECTOR_TABLE_DATASET", directorDS);

    JasperReport report = (JasperReport)JRLoader.loadObject(new File("data/director_movies_report.jasper"));

    JasperPrint print = JasperFillManager.fillReport(report, params, reportDS);

    return JasperExportManager.exportReportToPdf(print);

  }

}
