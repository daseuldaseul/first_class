package filmcrush.first_class.entity;

import filmcrush.first_class.dto.MovieFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

    public void updateMovie(MovieFormDto movieFormDto) {
        this.movieTitle = movieFormDto.getMovieTitle();
        this.movieGenre = movieFormDto.getMovieGenre();
        this.movieDirector = movieFormDto.getMovieDirector();
        this.movieActor = movieFormDto.getMovieActor();
    }

}
