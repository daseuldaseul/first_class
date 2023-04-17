package filmcrush.first_class.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MovieDto {

    private Long movieIndex; // 영화 고유번호

    private String movieTitle; // 영화 제목

    private String movieGenre; // 영화 장르

    private Double movieTotalScore; // 영화에 대한 총 평점

    private Long movieReviewNum; // 영화의 리뷰 개수

    private String movieDetail; // 영화 상세 정보

}
