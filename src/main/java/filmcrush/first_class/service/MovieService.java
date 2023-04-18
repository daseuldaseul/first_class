package filmcrush.first_class.service;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.MovieDto;
import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.dto.MovieImgDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import filmcrush.first_class.repository.MovieImgRepository;
import filmcrush.first_class.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieImgService movieImgService;
    private final MovieImgRepository movieImgRepository;

    public Long saveMovie(MovieFormDto movieFormDto, List<MultipartFile> movieImgFileList) throws Exception {
        //상품 등록
        Movie movie = movieFormDto.createMovie();
        movieRepository.save(movie);

        //이미지 등록

            MovieImg movieImg = new MovieImg();
            movieImg.setMovie(movie);

            movieImgService.saveMovieImg(movieImg, movieImgFileList.get(0));

        return movie.getMovieIndex();
    }

    @Transactional(readOnly = true)
    public MovieFormDto getMovieDtl(Long movieIndex) {
        Movie movie = movieRepository.findByMovieIndex(movieIndex);
        List<MovieImg> movieImgList = movieImgRepository.findByMovie(movie);
        List<MovieImgDto> movieImgDtoList = new ArrayList<>();
        for (MovieImg movieImg : movieImgList) {
            MovieImgDto movieImgDto = MovieImgDto.of(movieImg);
            movieImgDtoList.add(movieImgDto);
        }

        movie = movieRepository.findById(movieIndex)
                .orElseThrow(EntityNotFoundException::new);
        MovieFormDto movieFormDto = MovieFormDto.of(movie);
        movieFormDto.setMovieImgDtoList(movieImgDtoList);
        return movieFormDto;

    }



    public Long updateMovie(MovieFormDto movieFormDto, List<MultipartFile> movieImgFileList) throws Exception {
        //상품 수정
        Movie movie = movieRepository.findById(movieFormDto.getMovieIndex())
                .orElseThrow(EntityNotFoundException::new);
        movie.updateMovie(movieFormDto);

        List<Long> movieImgIds = movieFormDto.getMovieImgIds();

        //이미지 등록
        movieImgService.updateMovieImg(movieImgIds.get(0), movieImgFileList.get(0));
        return movie.getMovieIndex();
    }

//    @Transactional(readOnly = true)
//    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
//    }
//
//    @Transactional(readOnly = true)
//    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        return itemRepository.getMainItemPage(itemSearchDto, pageable);
//    }
}
