package filmcrush.first_class.controller;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.dto.ReplyDto;
import filmcrush.first_class.dto.ReplyFormDto;
import filmcrush.first_class.entity.*;
import filmcrush.first_class.repository.*;
import filmcrush.first_class.service.BoardHashtagsService;
import filmcrush.first_class.service.BoardService;
import filmcrush.first_class.service.HashtagsService;
import filmcrush.first_class.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private HashtagsService hashtagsService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardHashtagsRepository boardHashtagsRepository;

    @Autowired
    BoardHashtagsService boardHashtagsService;

    @GetMapping(value = "/")
    public String boardForm(@RequestParam(required = false) String type, Model model, @PageableDefault(page = 0, size = 10, sort = "boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Board> list = boardService.boardList(pageable);

        if (type == null) {
            list = boardService.boardList(pageable);
        } else if (type.equals("view")) {
            list = boardService.viewBoardList(pageable);
        } else if (type.equals("date")) {
            list = boardService.dateBoardList(pageable);
        } else if (type.equals("like")) {
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
        if (nowPage == 1) {
            endPage = Math.min(nowPage + 4, list.getTotalPages());
        } else if (nowPage == 2) {
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage = Math.min(nowPage + 3, list.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if (nowPage == (list.getTotalPages() - 1)) {
            startPage = Math.min(nowPage - 3, list.getTotalPages());
        } else if (nowPage == (list.getTotalPages())) {
            startPage = Math.min(nowPage - 4, list.getTotalPages());
        }

        if (startPage < 1) {
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
     **/
//    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/board/write")
    public String boardWrite(String hashtags, @ModelAttribute("boardFormDto") BoardFormDto boardFormDto, Model model) {
        Board board = new Board();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        board.setBoardTitle(boardFormDto.getBoardTitle());
        board.setBoardContent(boardFormDto.getBoardContent());
        board.setBoardDate(LocalDateTime.now());
        board.setUser(userRepository.findByUserId(author));
        board.setMovie(movieRepository.findByMovieTitle(boardFormDto.getMovie()));
        board.setBoardScore(boardFormDto.getBoardScore());
        board.setViewNum(0L);
        board.setReplyNum(0L);

        boardRepository.save(board);
        String[] words = hashtags.split("#");
        List<String> hashtagList = new ArrayList<>();
        for (String word : words) {
            String trimmed = word.trim();
            if (!trimmed.isEmpty()) {
                hashtagList.add(trimmed);
            }

        }

        for(String tags : hashtagList){
            BoardHashtags boardHashtags = new BoardHashtags();
            Hashtags tag = hashtagsService.findHash(tags);
            boardHashtags.setBoard(board);
            boardHashtags.setHashtags(tag);
            boardHashtagsRepository.save(boardHashtags);
        }
        model.addAttribute("boardFormDto", new BoardFormDto());
        return "redirect:/";
    }

    /**
     * 글쓰기 페이지(조회)
     **/
    @GetMapping(value = "/board/write")
    public String boardWrite(Model model) {

        model.addAttribute("boardFormDto", new BoardFormDto());
        return "board/boardWrite";
    }


    @GetMapping(value = "/search")
    public String searchKeyword(@RequestParam String keyword, @RequestParam String searchType, Model model, @PageableDefault(page = 0, size = 10, sort = "boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {
        String result = "";
        if (searchType.equals("title")) {
            result = searchTitle(keyword, model, pageable);
        } else if (searchType.equals("movieTitle")) {
            result = searchMovieTitle(keyword, model, pageable);
        } else if (searchType.equals("userId")) {
            result = searchUserNickname(keyword, model, pageable);
        }
        return result;
    }

    public String searchTitle(@RequestParam String keyword, Model model, @PageableDefault(page = 0, size = 3, sort = "boardIndex",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Board> searchList = boardService.searchTitle(keyword, pageable);
//        int nowPage = searchList.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 9, searchList.getTotalPages());


        int nowPage = searchList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, searchList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if (nowPage == 1) {
            endPage = Math.min(nowPage + 4, searchList.getTotalPages());

        } else if (nowPage == 2) {
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage = Math.min(nowPage + 3, searchList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if (nowPage == (searchList.getTotalPages() - 1)) {
            startPage = Math.min(nowPage - 3, searchList.getTotalPages());
        } else if (nowPage == (searchList.getTotalPages())) {
            startPage = Math.min(nowPage - 4, searchList.getTotalPages());
        }

        if (startPage < 1) {
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

    public String searchMovieTitle(@RequestParam String keyword, Model model, @PageableDefault(page = 0, size = 3, sort = "boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {
        List<Movie> movieList = movieRepository.findByMovieTitleContaining(keyword);
        // keyword를 가진 Movie 객체 리스트를 movieList 담음

        Page<Board> searchList = boardService.searchMovie(movieList, pageable);


        int nowPage = searchList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, searchList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if (nowPage == 1) {
            endPage = Math.min(nowPage + 4, searchList.getTotalPages());

        } else if (nowPage == 2) {
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage = Math.min(nowPage + 3, searchList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if (nowPage == (searchList.getTotalPages() - 1)) {
            startPage = Math.min(nowPage - 3, searchList.getTotalPages());
        } else if (nowPage == (searchList.getTotalPages())) {
            startPage = Math.min(nowPage - 4, searchList.getTotalPages());
        }

        if (startPage < 1) {
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

    public String searchUserNickname(@RequestParam String keyword, Model model, @PageableDefault(page = 0, size = 3, sort = "boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {

        List<Users> userList = userRepository.findByUserNicknameContaining(keyword);
        Page<Board> searchList = boardService.searchUser(userList, pageable);


        int nowPage = searchList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, searchList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if (nowPage == 1) {
            endPage = Math.min(nowPage + 4, searchList.getTotalPages());

        } else if (nowPage == 2) {
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage = Math.min(nowPage + 3, searchList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if (nowPage == (searchList.getTotalPages() - 1)) {
            startPage = Math.min(nowPage - 3, searchList.getTotalPages());
        } else if (nowPage == (searchList.getTotalPages())) {
            startPage = Math.min(nowPage - 4, searchList.getTotalPages());
        }

        if (startPage < 1) {
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
     **/
    @GetMapping(value = "/board/{boardIndex}")
    public String boardDtl(Model model, @PathVariable("boardIndex") Long boardIndex) {
        BoardDto boardDto = boardService.getBoardView(boardIndex);
        Board board = boardRepository.findByBoardIndex(boardIndex);
        List<Hashtags> hashList = hashtagsService.hashtagsList(board);

        board.setViewNum(board.getViewNum() +1);
        List<Reply> replyList = replyService.getBoardView(board);
        model.addAttribute("hashList", hashList);
        model.addAttribute("replyList", replyList);
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("replyFormDto", new ReplyFormDto());


        return "board/boardDtl";
    }

    @PostMapping(value = "/board/{boardIndex}")
    public String replyWrite(@ModelAttribute("ReplyFormDto") ReplyFormDto replyFormDto, @PathVariable("boardIndex") Long boardIndex, Model model) {
        Reply reply = new Reply();
        BoardDto boardDto = boardService.getBoardView(boardIndex);

        model.addAttribute("boardDto", boardDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        reply.setReplyContent(replyFormDto.getReplyContent());
        reply.setReplyDate(LocalDateTime.now());
        reply.setBoard(boardRepository.findByBoardIndex(boardIndex));
        reply.setUser(userRepository.findByUserId(author));

        replyRepository.save(reply);


        Board board = boardRepository.findByBoardIndex(boardIndex);

        List<Reply> replyList = replyService.getBoardView(board);
        board.setReplyNum((long)replyList.size());
        boardRepository.save(board);


        return "redirect:/board/" + boardIndex;

    }

//    @GetMapping(value= "/board/reply")
//    public String replyWrite(Model model) {
//        model.addAttribute("replyFormDto", new ReplyFormDto());
//        return "board/boardDtl";
//    }

//    @PostMapping(value= "/board/reply")
//    public String replyWrite(@ModelAttribute("boardFormDto") ReplyFormDto replyFormDto, @RequestParam("boardIndex") Long boardIndex, Model model){
//        Reply reply = new Reply();
//        BoardDto boardDto = boardService.getBoardView(boardIndex);
//
//        model.addAttribute("boardDto", boardDto);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String author = authentication.getName();
//
//        reply.setReplyContent(replyFormDto.getReplyContent());
//        reply.setReply_date(replyFormDto.getReply_date());
//        reply.setBoard(boardRepository.findByBoardIndex(boardIndex));
//        reply.setUser(userRepository.findByUserId(author));
//
//        replyRepository.save(reply);
//        model.addAttribute("replyFormDto", new ReplyFormDto());
//        return "board/boardDtl";
//    }

    @GetMapping(value="/board/movieSearch")
    public String movieSearch(){
        return "board/movieSearch";
    }
    @PostMapping(value="/board/movieSearch")
    public void movieSearch(String keyword, Model model, @PageableDefault(page=0, size=10, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Movie> movieList = movieRepository.findByMovieTitleContaining(keyword, pageable);


        int nowPage = movieList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, movieList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if(nowPage == 1){
            endPage = Math.min(nowPage + 4, movieList.getTotalPages());

        }else if(nowPage == 2){
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage  = Math.min(nowPage + 3, movieList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if(nowPage == (movieList.getTotalPages()-1)){
            startPage = Math.min(nowPage - 3, movieList.getTotalPages());
        }else if(nowPage == (movieList.getTotalPages())){
            startPage = Math.min(nowPage - 4, movieList.getTotalPages());
        }

        if(startPage < 1){
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;

        model.addAttribute("movieList", movieList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", movieList.getTotalPages());


    }
    @GetMapping(value = "/hashtag")
    public String hashtag(String hashtag, Model model, @PageableDefault(page=0, size=10, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable){
        List<Long> indexList = boardHashtagsService.findHashBoardIndex(hashtag);
        Page<Board> hashList = boardRepository.findByBoardIndexIn(indexList, pageable);

        int nowPage = hashList.getPageable().getPageNumber() + 1;


        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, hashList.getTotalPages());

        // 현재 페이지 1번일 때 1 뒤에 2, 3, 4, 5p까지 출력되도록함.
        if(nowPage == 1){
            endPage = Math.min(nowPage + 4, hashList.getTotalPages());

        }else if(nowPage == 2){
            // 현재 페이지 2번일 때 뒤에 3, 4, 5p까지 출력되도록 함.
            endPage  = Math.min(nowPage + 3, hashList.getTotalPages());
        }

        // 현재 페이지가 마지막 페이지일 때
        // 현재 페이지 앞에 페이지를 뜨게 만듦
        if(nowPage == (hashList.getTotalPages()-1)){
            startPage = Math.min(nowPage - 3, hashList.getTotalPages());
        }else if(nowPage == (hashList.getTotalPages())){
            startPage = Math.min(nowPage - 4, hashList.getTotalPages());
        }

        if(startPage < 1){
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;


        model.addAttribute("hashList", hashList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", hashList.getTotalPages());

        return "board/hashtag";


    }

    @GetMapping(value="board/delete/{boardIndex}")
    public String deleteBoard(@PathVariable("boardIndex") Long boardIndex){
        boardService.deleteBoard(boardIndex);
        return "redirect:/";
    }



    //게시글 수정 페이지 만들기
    @GetMapping(value="board/write/{boardIndex}")
    public String updateBoard(@PathVariable("boardIndex") Long boardIndex, Model model){
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardIndex);

        model.addAttribute("boardFormDto", boardFormDto);

        return "board/boardWrite";
    }
    @PostMapping(value="board/write/{boardIndex}")
    public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
                              Model model, @PathVariable("boardIndex") Long boardIndex) throws Exception{

        if(bindingResult.hasErrors()){

            return "board/boardWrite";
        }
        try {

            boardService.updateBoard(boardFormDto);
        }catch(Exception e){
            model.addAttribute("errorMessage","게시판 수정 중 에러 발생");
            return "board/boardWrite";
        }

        return "redirect:/board/" + boardIndex;
    }

    @GetMapping(value="reply/delete/{replyIndex}")
    public String deleteReply(@PathVariable("replyIndex") Long replyIndex){



        Reply reply = replyRepository.findByReplyIndex(replyIndex);
        Long index = reply.getBoard().getBoardIndex();
        Board board = boardRepository.findByBoardIndex(index);
        replyService.deleteReply(replyIndex);
        List<Reply> replyList = replyService.getBoardView(board);
        board.setReplyNum((long)replyList.size());
        boardRepository.save(board);

        return "redirect:/board/" + index;
    }

}
