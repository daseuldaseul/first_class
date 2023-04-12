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


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class boardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("저장 테스트")
//    public void createBoardTest(){
//
//        Board board = new Board();
//
//        board.setBoardTitle("제목임");
//        board.setBoardScore(5L);
//        board.setBoardContent("테스트 본문임");
//        board.setReplyNum(1L);
//        board.setLikeNum(0L);
//        board.setBoardDate(LocalDateTime.now());
//
//        Board savedBoard = boardRepository.save(board);
//        System.out.println(savedBoard.toString());
//
//    }
    public void createBoardList(){
        for(int i=1; i<= 10; i++){
            Board board = new Board();

            board.setBoardTitle("제목임"+i);
            board.setBoardScore(5L);
            board.setBoardContent("테스트 본문임"+i);
            board.setReplyNum(1L);
            board.setLikeNum(0L);
            board.setBoardDate(LocalDateTime.now());

            Board savedBoard = boardRepository.save(board);
//            System.out.println(savedBoard.toString());
        }
    }

    @Test
    @DisplayName("리뷰제목 조회 테스트")
    public void findByBoardTitleTest(){
        this.createBoardList();
        List<Board> boardList = boardRepository.findByBoardTitle("제목임4");
        for(Board board : boardList){
            System.out.println(board.toString());
        }
    }

}