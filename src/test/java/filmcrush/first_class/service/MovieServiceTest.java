package filmcrush.first_class.service;

import filmcrush.first_class.dto.MovieFormDto;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import filmcrush.first_class.repository.MovieImgRepository;
import filmcrush.first_class.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MovieServiceTest {
    @Autowired
    MovieService movieService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieImgRepository movieImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            String path = "C:/filmcrush/movie/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }
    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveMovie() throws Exception {
        MovieFormDto movieFormDto = new MovieFormDto();
        movieFormDto.setMovieTitle("영화제목");
        movieFormDto.setMovieGenre("테스트장르");
        movieFormDto.setMovieDirector("테스트감독");
        movieFormDto.setMovieActor("테스트배우");

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long movieIndex = movieService.saveMovie(movieFormDto, multipartFileList);

        List<MovieImg> movieImgList = movieImgRepository.findByMovie(movieIndex);
        Movie movie = movieRepository.findById(movieIndex).orElseThrow(EntityNotFoundException::new);

        assertEquals(movieFormDto.getMovieTitle(), movie.getMovieTitle());
        assertEquals(movieFormDto.getMovieGenre(), movie.getMovieGenre());
        assertEquals(movieFormDto.getMovieDirector(), movie.getMovieDirector());
        assertEquals(movieFormDto.getMovieActor(), movie.getMovieActor());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), movieImgList.get(0).getOriImgName());
    }


}

