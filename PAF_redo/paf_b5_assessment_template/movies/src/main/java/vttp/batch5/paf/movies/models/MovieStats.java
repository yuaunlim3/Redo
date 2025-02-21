package vttp.batch5.paf.movies.models;

import java.time.LocalDate;

import org.bson.Document;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.Constants;

public class MovieStats {
    private String imdb_id;
    private double vote_average;
    private int vote_count;
    private LocalDate release_Date;
    private double revenue;
    private double budget;
    private int runTime;

    public String getImdb_id() {   return imdb_id;   }
    public void setImdb_id(String imdb_id) {    this.imdb_id = imdb_id;  }
    public double getVote_average() {     return vote_average;  }
    public void setVote_average(double vote_average) {    this.vote_average = vote_average; }
    public int getVote_count() {    return vote_count; }
    public void setVote_count(int vote_count) {   this.vote_count = vote_count; }
    public LocalDate getRelease_Date() {    return release_Date; }
    public void setRelease_Date(LocalDate release_Date) {    this.release_Date = release_Date;}
    public double getRevenue() {     return revenue; }
    public void setRevenue(double revenue) {   this.revenue = revenue;}
    public double getBudget() {   return budget; }
    public void setBudget(double budget) {  this.budget = budget;}
    public int getRunTime() {   return runTime;  }
    public void setRunTime(int runTime) {   this.runTime = runTime; }

    public static MovieStats fromJson(JsonObject json){

        MovieStats movieStats = new MovieStats();
        movieStats.setImdb_id(json.getString(Constants.ID,""));
        movieStats.setVote_average(json.getJsonNumber(Constants.VOTE_AVG) != null ? json.getJsonNumber(Constants.VOTE_AVG).doubleValue() : 0);
        movieStats.setVote_count(json.getInt(Constants.VOTE_COUNT,0));
        movieStats.setBudget(json.getJsonNumber(Constants.BUDGET) != null ? json.getJsonNumber(Constants.BUDGET).doubleValue() : 0);
        String date = json.getString(Constants.RELEASE_DATE,"");
        LocalDate localDate = LocalDate.parse(date);
        movieStats.setRelease_Date(localDate);
        movieStats.setRunTime(json.getInt(Constants.RUNTIME,0));
        movieStats.setRevenue(json.getJsonNumber(Constants.REVENUE) != null ? json.getJsonNumber(Constants.REVENUE).doubleValue() : 0);
        return movieStats;
    }

    public Document toDoc(){
        Document movieDoc = new Document();
        movieDoc.append(Constants.ID, this.imdb_id);
        movieDoc.append(Constants.BUDGET, this.budget);
        movieDoc.append(Constants.VOTE_AVG, this.vote_average);
        movieDoc.append(Constants.VOTE_COUNT, this.vote_count);
        movieDoc.append(Constants.RELEASE_DATE, this.release_Date);
        movieDoc.append(Constants.REVENUE, this.revenue);
        movieDoc.append(Constants.RUNTIME, this.runTime);


        return movieDoc;
    }
    @Override
    public String toString() {
        return "MovieStats [imdb_id=" + imdb_id + ", vote_average=" + vote_average + ", vote_count=" + vote_count
                + ", release_Date=" + release_Date + ", revenue=" + revenue + ", budget=" + budget + ", runTime="
                + runTime + "]";
    }

    

    
}
