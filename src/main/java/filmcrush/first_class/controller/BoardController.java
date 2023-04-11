package filmcrush.first_class.controller;

import filmcrush.first_class.dto.BoardDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
   @GetMapping(value = "/")
    public String boardForm(Model model) {
       model.addAttribute("boardDto", new BoardDto());
       return "templates/board/boardForm";
   }
}
