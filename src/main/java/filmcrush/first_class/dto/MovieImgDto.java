package filmcrush.first_class.dto;

import filmcrush.first_class.entity.MovieImg;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.Column;

@Getter
@Service
public class MovieImgDto {
    private Long movieImgIndex;

    private String imgName; // 이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로


    private static ModelMapper modelMapper = new ModelMapper();

    public static MovieImgDto of(MovieImg movieImg) {
        return modelMapper.map(movieImg, MovieImgDto.class);
    }
}
