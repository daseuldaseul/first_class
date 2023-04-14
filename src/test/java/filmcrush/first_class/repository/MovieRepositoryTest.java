package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class MovieRepositoryTest {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    BoardRepository boardRepository;


    public void createMovieTest() {
        for(int i=1; i<=10; i++) {
            Board board = new Board();
            Movie movie = new Movie();

            movie.setMovieTitle("제목" + i);

            Movie savedMovie = movieRepository.save(movie);

            board.setBoardTitle("리뷰 제목" + i);

            board.setMovie(savedMovie);
            Board savedBoard = boardRepository.save(board);
        }
    }

  //  @Test
    @DisplayName("테스트 리뷰 인덱스 조회 테스트")
    public void findByMovieIndexTest() {
        this.createMovieTest();

//        List<Movie> movieList = movieRepository.findByMovieTitle("제목1");
//        for(Movie movie : movieList) {
//            System.out.println(movie.toString() + "테스트결과아아아ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
//        }
//
//        List<Board> boardList = boardRepository.findByBoardTitle("리뷰 제목1");
//        for(Board board : boardList) {
//            System.out.println(board.getMovie().getMovieTitle() + "테스트결과아아아ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
//        }
    }
}