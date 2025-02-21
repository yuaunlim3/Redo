package vttp.batch5.paf.movies.models;

import java.util.Arrays;

import org.bson.Document;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.Constants;

public class MovieDetails {
    private String imdb_id;
    private String title;
    private String[] director;
    private String overview;
    private String tagline;
    private String[] genres;
    private int imdb_votes;
    private int imdb_rating;
    public String getImdb_id() {    return imdb_id;  }
    public void setImdb_id(String imdb_id) {    this.imdb_id = imdb_id;   }
    public String getTitle() {    return title;  }
    public void setTitle(String title) {    this.title = title;  }
    public String[] getDirector() {    return director; }
    public void setDirector(String[] director) {   this.director = director; }
    public String getOverview() {   return overview;}
    public void setOverview(String overview) {    this.overview = overview;}
    public String getTagline() {   return tagline; }
    public void setTagline(String tagline) {    this.tagline = tagline;}
    public String[] getGenres() {   return genres; }
    public void setGenres(String[] genres) {  this.genres = genres; }
    public int getImdb_votes() {   return imdb_votes; }
    public void setImdb_votes(int imdb_votes) {  this.imdb_votes = imdb_votes; }
    public int getImdb_rating() {   return imdb_rating; }
    public void setImdb_rating(int imdb_rating) {  this.imdb_rating = imdb_rating;  }

    public static MovieDetails fromJson(JsonObject json){
        MovieDetails movieDetails = new MovieDetails();
        String directors = json.getString(Constants.DIRECTOR,"");
        movieDetails.setDirector(directors.split(","));
        String genres = json.getString(Constants.GENRES,"");
        movieDetails.setGenres(genres.split(","));

        movieDetails.setImdb_id(json.getString(Constants.ID,""));
        movieDetails.setImdb_rating(json.getInt(Constants.IMDB_RATING,0));
        movieDetails.setImdb_votes(json.getInt(Constants.IMDB_VOTES,0));
        movieDetails.setOverview(json.getString(Constants.OVERVIEW,""));
        movieDetails.setTagline(json.getString(Constants.TAGLINE,""));
        movieDetails.setTitle(json.getString(Constants.TITLE,""));
        return movieDetails;
    }

    public Document toDoc(){
        Document document = new Document();
        document.append(Constants.ID,this.imdb_id);
        document.append(Constants.TITLE,this.title);
        document.append("directors",this.director);
        document.append(Constants.OVERVIEW,this.overview);
        document.append(Constants.TAGLINE,this.tagline);
        document.append(Constants.GENRES,this.genres);
        document.append(Constants.IMDB_RATING,this.imdb_rating);
        document.append(Constants.IMDB_VOTES,this.imdb_votes);

        return document;
    }
    @Override
    public String toString() {
        return "MovieDetails [imdb_id=" + imdb_id + ", title=" + title + ", director=" + Arrays.toString(director)
                + ", overview=" + overview + ", tagline=" + tagline + ", genres=" + Arrays.toString(genres)
                + ", imdb_votes=" + imdb_votes + ", imdb_rating=" + imdb_rating + "]";
    }

    

    
}
