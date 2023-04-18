package filmcrush.first_class.service;

import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.dto.MovieImgDto;
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
    public MovieFormDto getMovieDtl(Movie movie) {

        List<MovieImg> movieImgList = movieImgRepository.findByMovie(movie.getMovieIndex());
        List<MovieImgDto> movieImgDtoList = new ArrayList<>();
        for (MovieImg movieImg : movieImgList) {
            MovieImgDto movieImgDto = MovieImgDto.of(movieImg);
            movieImgDtoList.add(movieImgDto);
        }

        MovieFormDto movieFormDto = MovieFormDto.of(movie);
        movieFormDto.setMovieImgDtoList(movieImgDtoList);
        return movieFormDto;

    }
//
//    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
//        //상품 수정
//        Item item = itemRepository.findById(itemFormDto.getId())
//                .orElseThrow(EntityNotFoundException::new);
//        item.updateItem(itemFormDto);
//
//        List<Long> itemImgIds = itemFormDto.getItemImgIds();
//
//        //이미지 등록
//        for(int i=0; i<itemImgFileList.size(); i++) {
//            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
//        }
//        return item.getId();
//    }
//
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
