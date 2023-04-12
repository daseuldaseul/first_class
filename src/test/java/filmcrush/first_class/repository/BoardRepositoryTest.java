package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.User;
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
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("Board 테스트 해봄 ㅜ")
    public void createBoardTest() {
        Board board = new Board();
        board.setBoardTitle("제목임");
        board.setBoardScore(5L);
        board.setBoardContent("테스트 본문임");
        board.setReplyNum(1L);
        board.setLikeNum(0L);
        board.setBoardDate(LocalDateTime.now());

        Board savedBoard = boardRepository.save(board);
        System.out.println(savedBoard.toString());
    }

    public void createBoardList() {
        for(int i=1; i<=10; i++) {
            Board board = new Board();
        //    board.setBoardIndex(0L + i);
        //    board.setMovie(new Movie());
        //    board.setUser(new User());
            board.setBoardTitle("테스트 리뷰 제목" + i);
            board.setBoardScore(4L);
            board.setBoardContent("테스트 리뷰 내용");
            board.setReplyNum(10L + i);
            board.setLikeNum(0L + i);
            board.setViewNum(10L);
            board.setBoardDate(LocalDateTime.now());

            Board savedBoard = boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("테스트 리뷰 인덱스 조회 테스트")
    public void findByBoardIndexTest() {
        this.createBoardList();

        List<Board> boardList = boardRepository.findByBoardTitle("테스트 리뷰 제목1");
        for(Board board : boardList) {

            System.out.println(board.toString() + "테스트결과아아아ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
        }
    }
}