package vttp.batch5.paf.movies.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.models.Directors;
import vttp.batch5.paf.movies.services.MovieService;

@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    @Autowired
    private MovieService movieService;

    @GetMapping(path = "/summary")
    @ResponseBody
    public ResponseEntity<String> getData(@RequestParam(defaultValue = "5") int count) {
        List<JsonObject> directors = movieService.getProlificDirectors(count).stream().map(Directors::toJson).collect(Collectors.toList());;
        JsonArrayBuilder array = Json.createArrayBuilder();
        for (JsonObject director : directors) {
            array.add(director);
        }

        return ResponseEntity.ok(array.build().toString());
    }

    @GetMapping(path = "/summary/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getPDF(@RequestParam(defaultValue = "5") int count) {
        try {
            byte[] report = movieService.generatePDFReport(count);
            return ResponseEntity.ok(report);
        } catch (Exception ex) {
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatusCode.valueOf(500))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error.toString().getBytes());
        }
    }
}
