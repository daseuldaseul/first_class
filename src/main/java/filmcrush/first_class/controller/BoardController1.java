package filmcrush.first_class.controller;

import filmcrush.first_class.dto.BoardRequestDto1;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class BoardController1 {

//    private final BoardService boardService;

    @GetMapping("/board/{boardIndex}")
    public String boardPage(Model model, @PathVariable("boardIndex") Long boardIndex){
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardIndex);
        model.addAttribute("board", boardFormDto);

        return "board/boardDtl";
    }
}
