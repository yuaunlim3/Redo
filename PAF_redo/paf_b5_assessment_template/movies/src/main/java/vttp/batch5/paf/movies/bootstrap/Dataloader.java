package vttp.batch5.paf.movies.bootstrap;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.services.MovieService;

@Component
public class Dataloader implements CommandLineRunner {

  // TODO: Task 2

  @Autowired private MovieService movieService;

  @Override
  public void run(String... args) throws IOException {
    List<JsonObject> movies = movieService.unzipFile();
    Boolean uploaded = false;

    for(JsonObject movie:movies){
      boolean checker = movieService.check(movie);
      if(!checker){
        System.out.println("HAVENT UPLOAD");
        break;
      }
      else{
        uploaded = true;
      }
    }

    if(!uploaded){
      List<JsonObject> toUpload = new LinkedList<>();
      int size = 0;
      for(int i = 0; i < movies.size(); i++){
        toUpload.add(movies.get(i));
        size++;
        if( size%25 == 0){
          System.out.println(toUpload.size());
          movieService.upload(toUpload);
          toUpload.clear();
        }
      }
      if(!toUpload.isEmpty()){
        movieService.upload(movies);
      }
    }


    
  }

}
