package filmcrush.first_class.service;

import filmcrush.first_class.entity.MovieImg;
import filmcrush.first_class.repository.MovieImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieImgService {
    @Value("${movieImgLocation}")
    private String movieImgLocation;

    private final MovieImgRepository movieImgRepository;

    private final FileService fileService;

    public void saveMovieImg(MovieImg movieImg, MultipartFile movieImgFile) throws Exception {
        String oriImgName = movieImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(movieImgLocation, oriImgName, movieImgFile.getBytes());
            imgUrl = "/images/movie/" + imgName;
        }

        //상품 이미지 정보 저장
        movieImg.updateMovieImg(oriImgName, imgName, imgUrl);
        movieImgRepository.save(movieImg);
    }

    public void updateMovieImg(Long movieImgIndex, MultipartFile movieImgFile) throws Exception {
        if(!movieImgFile.isEmpty()) {
            MovieImg savedMovieImg = movieImgRepository.findById(movieImgIndex).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedMovieImg.getImgName())) {
                fileService.deleteFile(movieImgLocation + "/" + savedMovieImg.getImgName());

                String oriImgName = movieImgFile.getOriginalFilename();
                String imgName = fileService.uploadFile(movieImgLocation, oriImgName, movieImgFile.getBytes());
                String imgUrl = "/images/movie/" + imgName;
                savedMovieImg.updateMovieImg(oriImgName, imgName, imgUrl);

            }
        }
    }
}
