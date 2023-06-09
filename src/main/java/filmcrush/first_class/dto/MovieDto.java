package filmcrush.first_class.dto;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class MovieDto {

    private Long movieIndex; // 영화 고유번호

    private String movieTitle; // 영화 제목

    private String movieGenre; // 영화 장르

    private Double movieTotalScore; // 영화에 대한 총 평점

    private Long movieReviewNum; // 영화의 리뷰 개수

    private String movieDirector; // 영화 감독

    private String movieActor; // 영화 배우

    private List<MovieImgDto> movieImgDtoList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    // 소스 객체와 대상 객체 간의 값을 자동으로 매핑.

    public static MovieDto of(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }
}
