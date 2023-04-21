package filmcrush.first_class.service;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.MovieDto;
import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.dto.MovieImgDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import filmcrush.first_class.repository.BoardRepository;
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
    private final BoardRepository boardRepository;

    public Page<Movie> movieList(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }


    public Long saveMovie(MovieFormDto movieFormDto, List<MultipartFile> movieImgFileList) throws Exception {
        //상품 등록
        Movie movie = movieFormDto.createMovie();
        movieRepository.save(movie);

        //이미지 등록

            MovieImg movieImg = new MovieImg();
            movieImg.setMovie(movie);

            movieImgService.saveMovieImg(movieImg, movieImgFileList.get(0), movie.getMovieIndex());

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

//        movie = movieRepository.findById(movieIndex)
//                .orElseThrow(EntityNotFoundException::new);
        MovieFormDto movieFormDto = MovieFormDto.of(movie);
        movieFormDto.setMovieImgDtoList(movieImgDtoList);
        return movieFormDto;

    }






    public Long updateMovie(MovieFormDto movieFormDto, MultipartFile movieImgFileList) throws Exception {
        //상품 수정
        Movie movie = movieRepository.findById(movieFormDto.getMovieIndex())
                .orElseThrow(EntityNotFoundException::new);
        movie.updateMovie(movieFormDto);

        movieImgService.updateMovieImg(movie.getMovieIndex(), movieImgFileList);
       // Long movieImgIndex = movieFormDto.getMovieImgIndex();

//        //이미지 등록
//        if(movieImgIndex != null) {
//            movieImgService.updateMovieImg(movieImgIndex, movieImgFileList);
//        }
        return movie.getMovieIndex();
    }

//    public Long updateMovie2(Long movieIndex, MovieFormDto movieFormDto, MultipartFile movieImgFileList) {
//        Movie movie = movieRepository.findByMovieIndex(movieIndex); // 삭제하고싶은 movie 객체
//        movieImgRepository.deleteByMovie(movie); // 이미지 삭제
//
//
//    }

    @Transactional
    public void deleteMovie(Long movieIndex) {
        Movie movie = movieRepository.findByMovieIndex(movieIndex); // 삭제하고싶은 movie 객체
        movieImgRepository.deleteByMovie(movie); // 이미지 삭제
        boardRepository.deleteByMovie(movie); // 게시물 삭제
        movieRepository.deleteByMovieIndex(movieIndex); // 영화 삭제

    }

}
