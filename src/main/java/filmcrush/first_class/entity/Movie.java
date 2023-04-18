package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;
import javax.sql.DataSource;

@Getter
@Setter
@Entity
@Table(name = "movie")
@ToString
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieIndex; // 영화 고유번호

    @Column(nullable = false)
    private String movieTitle; // 영화 제목

    private String movieGenre; // 영화 장르

    private Double movieTotalScore; // 영화에 대한 총 평점

    private Long movieReviewNum; // 영화의 리뷰 개수

    private String movieDirector; // 영화 감독

    private String movieActor; // 영화 배우

//    public static Movie movieSource(MovieDto movieDto){
//        Movie movie = new Movie();
//        movie.setMovieIndex(movieDto.getMovieIndex());
//        movie.setMovieDetail(movieDto.getMovieDetail());
//        movie.setMovieGenre(movieDto.getMovieGenre());
//        movie.setMovieTitle(movieDto.getMovieTitle());
//        movie.setMovieReviewNum(movieDto.getMovieReviewNum());
//        movie.setMovieTotalScore(movieDto.getMovieTotalScore());
//
//        return movie;
//    }

    @Configuration
    public class DatabaseConfig {

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

}
