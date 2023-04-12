package filmcrush.first_class.controller;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.repository.BoardRepository;
import filmcrush.first_class.service.BoardService;
import filmcrush.first_class.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @GetMapping(value = "/")
    public String boardForm(Model model) {
       List<Board> boardList = boardRepository.findAll();

       model.addAttribute("boardList", boardList);
       return "board/boardForm";
   }

    @PostMapping(value = "/")
    public String boardWrite(Model model, Board board) {
        model.addAttribute("boardDto", new BoardDto());
        boardService.write(board);
        return "board/boardForm";
    }
}
