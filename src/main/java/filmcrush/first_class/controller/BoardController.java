package filmcrush.first_class.controller;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.repository.BoardRepository;
import filmcrush.first_class.repository.MovieRepository;
import filmcrush.first_class.service.BoardService;
import filmcrush.first_class.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @Autowired
    MovieRepository movieRepository;

    @GetMapping(value = "/")
    public String boardForm(Model model, @PageableDefault(page=0, size=3, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {

       // List<Board> boardList = boardRepository.findAll();

       Page<Board> list = boardService.boardList(pageable);

       //페이지 블럭 처리
       //1을 더해주는 이유 : pageable은 0부터 처리됨
       // getPageable() : Page 객체를 생성할 때 사용된 Pageable 객체(페이지 번호, 사이즈, 정렬 방법등의 정보를 담고 있음) 반환
       // getPageNumber() : Pageable 객체에서 현재 페이지의 번호를 반환함.
       int nowPage = list.getPageable().getPageNumber() + 1;
       int pageSize = 10;

       // 현재 페이지에서 가장 앞 페이지 번호를 보여줄 변수.
       // max 함수 : 현재 페이지에서 -4를 해줬을 때 1보다 작은 수가 나오면 안됨
        // 보통 UI에서 페이징 좌우로 4페이지 정도씩(총 9~10개정도) 보여주는게 일반적이므로 4로 지정.
//       int startPage = Math.max(nowPage - 4, 1);
        int startPage = ((nowPage - 1) / pageSize) * pageSize + 1;
        int endPage = Math.min(startPage + pageSize - 1, list.getTotalPages());

        //int endPage = Math.min(nowPage + 4, list.getTotalPages() -1);
       int prevPage = nowPage - 1;
       int nextPage = nowPage < list.getTotalPages() ? nowPage + 2 : list.getTotalPages() -1;


       model.addAttribute("boardList", list);
       model.addAttribute("nowPage", nowPage);
       model.addAttribute("startPage", startPage);
       model.addAttribute("endPage", endPage);
       model.addAttribute("prevPage", prevPage);
       model.addAttribute("nextPage", nextPage);
       model.addAttribute("totalPages", list.getTotalPages());
       return "board/boardForm";
   }

    /**
     * 글쓰기 페이지(입력)
     * **/
    @PostMapping(value = "/board/write")
    public String boardWrite(Model model, Board board, String movieTitle) {


        Movie savedMovie = movieRepository.findByMovieTitle(movieTitle);
        board.setMovie(savedMovie);
        boardService.write(board);
        model.addAttribute("boardDto", new BoardDto());
        return "redirect:/";
    }

    /**
     * 글쓰기 페이지(조회)
     * **/
    @GetMapping(value = "/board/write")
    public String boardWrite(Model model) {
        model.addAttribute("boardDto", new BoardFormDto());
        return "board/boardWrite";
    }

    @GetMapping(value = "/search")
    public String search(String title, Model model, @PageableDefault(page=0, size=3, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {

        // List<Board> boardList = boardRepository.findAll();

        Page<Board> searchList = boardService.search(title, pageable);
       //페이지 블럭 처리
        //1을 더해주는 이유 : pageable은 0부터 처리됨
        // getPageable() : Page 객체를 생성할 때 사용된 Pageable 객체(페이지 번호, 사이즈, 정렬 방법등의 정보를 담고 있음) 반환
        // getPageNumber() : Pageable 객체에서 현재 페이지의 번호를 반환함.
        int nowPage = searchList.getPageable().getPageNumber() + 1;

        // 현재 페이지에서 가장 앞 페이지 번호를 보여줄 변수.
        // max 함수 : 현재 페이지에서 -4를 해줬을 때 1보다 작은 수가 나오면 안됨
        // 보통 UI에서 페이징 좌우로 4페이지 정도씩(총 9~10개정도) 보여주는게 일반적이므로 4로 지정.
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, searchList.getTotalPages());

        model.addAttribute("searchList", searchList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "board/boardSearch";
    }
}
