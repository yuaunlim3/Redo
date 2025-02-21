package vttp.batch5.paf.movies;

public class Constants {
        // JsonObject
        public static final String ID = "imdb_id";
        public static final String VOTE_AVG = "vote_average";
        public static final String VOTE_COUNT = "vote_count";
        public static final String RELEASE_DATE = "release_date";
        public static final String REVENUE = "revenue";
        public static final String BUDGET = "budget";
        public static final String RUNTIME = "runtime";

        public static final String TITLE = "title";
        public static final String DIRECTOR = "director";
        public static final String OVERVIEW = "overview";
        public static final String TAGLINE = "tagline";
        public static final String GENRES = "genres";
        public static final String IMDB_RATING = "imdb_rating";
        public static final String IMDB_VOTES = "imdb_votes";

        // mySQL
        public static final String CHECK_ID = """
                        SELECT count(*) as count FROM imdb WHERE imdb_id = ?
                        """;

        public static final String GET_ID = """
                        SELECT * FROM imdb WHERE imdb_id = ?
                        """;
        public static final String INSERT_SQL = """
                        INSERT INTO imdb (imdb_id,vote_average,vote_count,release_date,revenue,budget,runtime)
                        VALUES(?,?,?,?,?,?,?);
                        """;

        // Mongo
        public static final String C_IMDB = "imdb";
        public static final String C_ERROR = "error";
}
