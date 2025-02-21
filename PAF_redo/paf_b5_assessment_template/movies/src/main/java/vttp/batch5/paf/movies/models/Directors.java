package vttp.batch5.paf.movies.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Directors {
    private String name;
    private int movies;
    private float revenue;
    private float budget;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMovies() {
        return movies;
    }
    public void setMovies(int movies) {
        this.movies = movies;
    }
    public float getRevenue() {
        return revenue;
    }
    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }
    public float getBudget() {
        return budget;
    }
    public void setBudget(float budget) {
        this.budget = budget;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                    .add("director_name",this.name)
                    .add("movies_count",this.movies)
                    .add("total_revenue",this.revenue)
                    .add("total_budget",this.budget)
                    .build();
    }

    public JsonObject forReport(){
        return Json.createObjectBuilder()
        .add("director",this.name)
        .add("count",this.movies)
        .add("revenue",this.revenue)
        .add("budget",this.budget)
        .build();
    }

    
}
