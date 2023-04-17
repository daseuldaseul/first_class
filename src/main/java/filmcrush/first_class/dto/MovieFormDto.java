package filmcrush.first_class.dto;

import filmcrush.first_class.entity.Movie;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MovieFormDto {
    private Long movieIndex; // 영화 고유번호

    @NotBlank(message = "영화제목은 필수 입력 값입니다.")
    private String movieTitle; // 영화 제목

    private String movieGenre; // 영화 장르

    private String movieDirector; // 영화 감독

    private String movieActor; // 영화 배우

    private List<MovieImgDto> movieImgDtoList = new ArrayList<>();

    private List<Long> movieImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Movie createMovie() {
        return modelMapper.map(this, Movie.class);
    }

    public static MovieFormDto of(Movie movie) {
        return modelMapper.map(movie, MovieFormDto.class);
    }
}
