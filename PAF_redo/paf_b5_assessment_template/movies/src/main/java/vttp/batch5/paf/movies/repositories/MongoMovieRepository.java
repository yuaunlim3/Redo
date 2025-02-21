package vttp.batch5.paf.movies.repositories;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.Constants;
import vttp.batch5.paf.movies.models.MovieDetails;

@Repository
public class MongoMovieRepository {
    @Autowired private MongoTemplate template;

 // TODO: Task 2.3
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
 /*
  db.imdb.insertMany(
  {imdb_id,title,directors,overview,tagline,genres,imdb_rating,imdb_votes},
  {imdb_id,title,directors,overview,tagline,genres,imdb_rating,imdb_votes},
  {imdb_id,title,directors,overview,tagline,genres,imdb_rating,imdb_votes},
  )
  */
 public void batchInsertMovies(List<MovieDetails> movies) {
    List<Document> docstoInsert = movies.stream().map(MovieDetails::toDoc).collect(Collectors.toList());
    template.insert(docstoInsert,Constants.C_IMDB);
 }

 // TODO: Task 2.4
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
 public void logError(Exception ex,List<String> ids) {

    Document doc = new Document();
    doc.append("ids",ids);
    doc.append("error",ex.getMessage());
    doc.append("timestamp",new Date());
    
    template.insert(doc,Constants.C_ERROR);

 }

 // TODO: Task 3
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //

 /*
  
db.imdb.aggregate(
[
    {
        $unwind: "$directors"
    },
    {
        $group:{
            _id:"$directors",
            movie_count:{
                $sum:1
            },
            imdb_ids: { $push: "$imdb_id" }
        }
    },
    {
        $sort:{
            movie_count:-1
        }
    },
    {
        $limit:5
    }
])
  */

  public List<Document> getDirectors(int limit){
   AggregationOperation unwind = Aggregation.unwind("directors");
   GroupOperation groupOperation = Aggregation.group("directors")
                                    .push(Constants.ID).as("imdb_ids")
                                    .count().as("movie_count");

   SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC,"movie_count"));
   LimitOperation limitOperation = Aggregation.limit(limit);

   Aggregation pipeline = Aggregation.newAggregation(unwind,groupOperation,sortOperation,limitOperation);

   AggregationResults<Document>results = template.aggregate(pipeline,Constants.C_IMDB,Document.class);

   return results.getMappedResults();
  }


}
