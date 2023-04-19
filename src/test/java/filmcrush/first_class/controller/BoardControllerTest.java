package filmcrush.first_class.controller;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Users;
import filmcrush.first_class.repository.BoardRepository;
import filmcrush.first_class.service.BoardService;
import filmcrush.first_class.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class BoardControllerTest {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserService userService;

    @Autowired
    BoardService boardService;
    
    @Test
    @DisplayName("Board 테스트 해봄 ㅜ")
    public void createBoardTest() {
        Long boardIndex =1L;

        String author = "bong";
        Users user = new Users();

        user.setUserId("bong");

        System.out.println(user.toString());

        Board board = new Board();
        board.setBoardIndex(boardIndex);


        user= userService.findUser(author);


        board = boardRepository.findByBoardIndex(boardIndex);


        String UserId = user.getUserId();
        // 저장 true, 삭제 false
        int result = boardService.saveLike(board, user);
        
//        if(result){
//        System.out.println("true");
//        }else {
//            System.out.println("t실패");
//        }
    }
}