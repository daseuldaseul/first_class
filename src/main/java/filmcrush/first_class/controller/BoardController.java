package filmcrush.first_class.controller;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.dto.ReplyFormDto;
import filmcrush.first_class.entity.*;
import filmcrush.first_class.repository.BoardRepository;
import filmcrush.first_class.repository.MovieRepository;
import filmcrush.first_class.repository.ReplyRepository;
import filmcrush.first_class.repository.UserRepository;
import filmcrush.first_class.service.BoardService;
import filmcrush.first_class.service.ReplyService;
import filmcrush.first_class.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
public class BoardController {




    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;


    @Autowired
    private ReplyService replyService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/")
    public String boardForm(@RequestParam(required=false)String type, Model model, @PageableDefault(page=0, size=3, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Board> list = boardService.boardList(pageable);

        if(type == null){
            list = boardService.boardList(pageable);
        }else if(type.equals("view")){
            list = boardService.viewBoardList(pageable);
        }else if(type.equals("date")){
            list = boardService.dateBoardList(pageable);
        }else if(type.equals("like")){
            list = boardService.likeBoardList(pageable);
        }


        //페이지 블럭 처리
        //1을 더해주는 이유 : pageable은 0부터 처리됨
        // getPageable() : Page 객체를 생성할 때 사용된 Pageable 객체(페이지 번호, 사이즈, 정렬 방법등의 정보를 담고 있음) 반환
        // getPageNumber() : Pageable 객체에서 현재 페이지의 번호를 반환함.
        int nowPage = list.getPageable().getPageNumber() + 1;

        // 현재 페이지에서 가장 앞 페이지 번호를 보여줄 변수.
        // max 함수 : 현재 페이지에서 -4를 해줬을 때 1보다 작은 수가 나오면 안됨
        // 보통 UI에서 페이징 좌우로 4페이지 정도씩(총 9~10개정도) 보여주는게 일반적이므로 4로 지정.
        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, list.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if(nowPage == 1){
            endPage  = Math.min(nowPage + 4, list.getTotalPages());
        }else if(nowPage == 2){
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage  = Math.min(nowPage + 3, list.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if(nowPage == (list.getTotalPages()-1)){
            startPage = Math.min(nowPage - 3, list.getTotalPages());
        }else if(nowPage == (list.getTotalPages())){
            startPage = Math.min(nowPage - 4, list.getTotalPages());
        }

        if(startPage < 1){
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;

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
//    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/board/write")
    public String boardWrite(@ModelAttribute("boardFormDto") BoardFormDto boardFormDto, Model model) {
        Board board = new Board();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        board.setBoardTitle(boardFormDto.getBoardTitle());
        board.setBoardContent(boardFormDto.getBoardContent());
        board.setBoardDate(LocalDateTime.now());
        board.setUser(userRepository.findByUserId(author));
        board.setMovie(movieRepository.findByMovieTitle(boardFormDto.getMovie()));


        boardRepository.save(board);
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "redirect:/";
    }

    /**
     * 글쓰기 페이지(조회)
     * **/
    @GetMapping(value = "/board/write")
    public String boardWrite(Model model) {

        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardWrite";
    }


    @GetMapping(value = "/search")
    public String searchKeyword(@RequestParam String keyword, @RequestParam String searchType, Model model, @PageableDefault(page=0, size=10, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable){
        String result = "";
        if(searchType.equals("title")){
            result = searchTitle(keyword, model, pageable);
        }else if(searchType.equals("movieTitle")){
            result = searchMovieTitle(keyword, model, pageable);
        }else if(searchType.equals("userId")){
            result = searchUserNickname(keyword, model, pageable);
        }
        return result;
    }

    public String searchTitle(@RequestParam String keyword, Model model, @PageableDefault(page=0, size=3, sort="boardIndex",
            direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> searchList = boardService.searchTitle(keyword, pageable);
//        int nowPage = searchList.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 9, searchList.getTotalPages());


        int nowPage = searchList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, searchList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if(nowPage == 1){
            endPage = Math.min(nowPage + 4, searchList.getTotalPages());

        }else if(nowPage == 2){
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage  = Math.min(nowPage + 3, searchList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if(nowPage == (searchList.getTotalPages()-1)){
            startPage = Math.min(nowPage - 3, searchList.getTotalPages());
        }else if(nowPage == (searchList.getTotalPages())){
            startPage = Math.min(nowPage - 4, searchList.getTotalPages());
        }

        if(startPage < 1){
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;

        model.addAttribute("searchList", searchList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", searchList.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "board/boardSearch";
    }

    public String searchMovieTitle(@RequestParam String keyword, Model model, @PageableDefault(page=0, size=3, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable){
        List<Movie> movieList = movieRepository.findByMovieTitleContaining(keyword);
        // keyword를 가진 Movie 객체 리스트를 movieList 담음

        Page<Board> searchList = boardService.searchMovie(movieList, pageable);


        int nowPage = searchList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, searchList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if(nowPage == 1){
            endPage = Math.min(nowPage + 4, searchList.getTotalPages());

        }else if(nowPage == 2){
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage  = Math.min(nowPage + 3, searchList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if(nowPage == (searchList.getTotalPages()-1)){
            startPage = Math.min(nowPage - 3, searchList.getTotalPages());
        }else if(nowPage == (searchList.getTotalPages())){
            startPage = Math.min(nowPage - 4, searchList.getTotalPages());
        }

        if(startPage < 1){
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;

        model.addAttribute("searchList", searchList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", searchList.getTotalPages());


        return "board/boardSearch";
    }

    public String searchUserNickname(@RequestParam String keyword, Model model, @PageableDefault(page=0, size=3, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable){

        List<Users> userList = userRepository.findByUserNicknameContaining(keyword);
        Page<Board> searchList = boardService.searchUser(userList, pageable);


        int nowPage = searchList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, searchList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if(nowPage == 1){
            endPage = Math.min(nowPage + 4, searchList.getTotalPages());

        }else if(nowPage == 2){
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage  = Math.min(nowPage + 3, searchList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if(nowPage == (searchList.getTotalPages()-1)){
            startPage = Math.min(nowPage - 3, searchList.getTotalPages());
        }else if(nowPage == (searchList.getTotalPages())){
            startPage = Math.min(nowPage - 4, searchList.getTotalPages());
        }

        if(startPage < 1){
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;


        model.addAttribute("searchList", searchList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", searchList.getTotalPages());


        return "board/boardSearch";
    }

    /**
     * 상세 페이지
     * **/
//    @GetMapping(value = "board/{boardIndex}")
//    public String boardDtl(Model model, @PathVariable("boardIndex") Long boardIndex){
//        BoardDto board = boardService.getBoardView(boardIndex);
//
//        model.addAttribute("boardDto", board);
//        return "board/boardDtl";
//    }

    @GetMapping(value = "/board/{boardIndex}")
    public String boardDtl(Model model, @PathVariable("boardIndex") Long boardIndex) {
        BoardDto boardDto = boardService.getBoardView(boardIndex);
        Board board = boardRepository.findByBoardIndex(boardIndex);
//        board.setViewNum(board.getViewNum() +1);

        //좋아요
//        int like = 0; // 비로그인 유저라면 무조건 like = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();
        Users user = userService.findUser(author);
//        if(author != null){
//            // 로그인한 사용자라면
//
//            /* member_id 반환 */
//            ;
//            /* 현재 로그인한 유저가 이 게시물을 좋아요 했는지 안 했는지 여부 확인 */
//
//
//        }
        //로그인한 유저가 좋아요 했는지 여부 확인
        Optional<UserLike> like = boardService.findLike(board, user);
        int heart = 0;
        if(like.isEmpty()) {
            heart = 0;
        }else {
            heart = 1;
        }

        List<Reply> replyList = replyService.getBoardView(board);



        model.addAttribute("user",user);
        model.addAttribute("replyList", replyList);
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("replyFormDto", new ReplyFormDto());
        model.addAttribute("heart", heart);


        return "board/boardDtl";
    }
    @PostMapping(value = "/board/like/{boardIndex}")
    public @ResponseBody int likeBoard(@PathVariable Long boardIndex, @RequestParam(name = "userId") String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        Users user = userService.findUser(author);


        Board board = boardService.findBoard(boardIndex);
        System.out.println(boardIndex);

        // 저장 true, 삭제 false
        int result = boardService.saveLike(board, user);

        return result;
    }


    @PostMapping(value = "/board/{boardIndex}")
    public String replyWrite(@ModelAttribute("ReplyFormDto") ReplyFormDto replyFormDto, @PathVariable("boardIndex") Long boardIndex, Model model) {
        Reply reply = new Reply();
        BoardDto boardDto = boardService.getBoardView(boardIndex);

        model.addAttribute("boardDto", boardDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        reply.setReplyContent(replyFormDto.getReplyContent());
        reply.setReply_date(LocalDateTime.now());
        reply.setBoard(boardRepository.findByBoardIndex(boardIndex));
        reply.setUser(userRepository.findByUserId(author));

        replyRepository.save(reply);


        Board board = boardRepository.findByBoardIndex(boardIndex);

        List<Reply> replyList = replyService.getBoardView(board);
        board.setReplyNum((long)replyList.size());
        boardRepository.save(board);


        return "redirect:/board/" + boardIndex;
    }



    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";

        Object principal = authentication.getPrincipal();

        return email;
    }
}
