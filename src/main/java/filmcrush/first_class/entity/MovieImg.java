package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="movie_img")
@Getter
@Setter
public class MovieImg {
    @Id
    @Column(name="movie_img_index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieImgIndex;

    private String imgName; // 이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_index")
    private Movie movie;

    public void updateMovieImg(String oriImgName, String imgName, String imgUrl, Movie movie) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
        this.movie = movie;
    }
}
