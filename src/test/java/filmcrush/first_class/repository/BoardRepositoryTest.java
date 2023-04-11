package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

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
}