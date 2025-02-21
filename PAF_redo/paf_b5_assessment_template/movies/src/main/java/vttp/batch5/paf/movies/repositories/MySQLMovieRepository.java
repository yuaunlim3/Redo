package vttp.batch5.paf.movies.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.Constants;
import vttp.batch5.paf.movies.models.Directors;
import vttp.batch5.paf.movies.models.MovieStats;

@Repository
public class MySQLMovieRepository {
  @Autowired private JdbcTemplate template;
  
  public Boolean check(String id){
    SqlRowSet rs = template.queryForRowSet(Constants.CHECK_ID, id);
    int checker = 0;
    while(rs.next()){
      checker = rs.getInt("count");

    }

    if(checker > 0){
      return true;
    }

    return false;
  }
  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public void batchInsertMovies(List<MovieStats> movies) {
    List<Object[]> params = movies.stream()
                  .map(movie -> new Object[]{movie.getImdb_id(),movie.getVote_average(),movie.getVote_count(),movie.getRelease_Date(),movie.getRevenue(),movie.getBudget(),movie.getRunTime()})
                  .collect(Collectors.toList());
    template.batchUpdate(Constants.INSERT_SQL, params);
  }
  
  // TODO: Task 3

  public Directors getInfo(Directors director , String id){
    SqlRowSet rw = template.queryForRowSet(Constants.GET_ID,id);
    while(rw.next()){
      float revenue = director.getRevenue();
      float budget = director.getBudget();
      revenue+= rw.getFloat("revenue");
      budget+= rw.getFloat("budget");
      director.setBudget(budget);
      director.setRevenue(revenue);
    }

    return director;
  }


}
