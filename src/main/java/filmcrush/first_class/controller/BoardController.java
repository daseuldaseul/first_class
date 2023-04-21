package filmcrush.first_class.controller;

import filmcrush.first_class.dto.*;
import filmcrush.first_class.entity.*;
import filmcrush.first_class.repository.*;
import filmcrush.first_class.service.*;
import filmcrush.first_class.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;


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

    @Autowired
    MovieService movieService;

    @Autowired
    MovieImgService movieImgService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReReplyService reReplyService;

    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    UserLikeRepository userLikeRepository;

    @Autowired
    MovieImgRepository movieImgRepository;

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

        model.addAttribute("boardList", list);
        boardService.paging(list, model);

        List<MovieImg> movieImgList = movieImgRepository.findAllByOrderByMovieDesc();
        List<MovieImg> mainMovieImgList = movieImgList.subList(0, 4);
        model.addAttribute("movieImgList", mainMovieImgList);
        return "board/boardForm";
    }

    /**
     * 글쓰기 페이지(입력)
     **/
//    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/board/write")
    public String boardWrite(String hashtags, @ModelAttribute("boardFormDto") BoardFormDto boardFormDto, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();

        boardService.boardWrite(author, hashtags, boardFormDto, model);

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
            boardService.searchTitle(keyword, model, pageable);
        } else if (searchType.equals("movieTitle")) {
            boardService.searchMovieTitle(keyword, model, pageable);
        } else if (searchType.equals("userId")) {
            boardService.searchUserNickname(keyword, model, pageable);
        }
        List<MovieImg> movieImgList = movieImgRepository.findAllByOrderByMovieDesc();
        List<MovieImg> mainMovieImgList = movieImgList.subList(0, 4);
        model.addAttribute("movieImgList", mainMovieImgList);
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

        //좋아요
//        int like = 0; // 비로그인 유저라면 무조건 like = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();
        Users user = userService.findUser(author);


        //로그인한 유저가 좋아요 했는지 여부 확인
        Optional<UserLike> like = boardService.findLike(board, user);
        int heart = 0;
        if(like.isEmpty()) {
            heart = 0;
        }else {
            heart = 1;
        }

        List<Reply> replyList = replyService.getBoardView(board);
        List<ReReply> reReplyList = new ArrayList<>();
        for(Reply reply : replyList) {
            reReplyList.addAll(reReplyService.getReplyView(reply));

        }

        MovieImg movieImg = movieImgService.getMovieImgDtl(board.getMovie().getMovieIndex());

        model.addAttribute("reReplyList", reReplyList);
        model.addAttribute("movieImgDto", movieImg);
        model.addAttribute("user",user);
        model.addAttribute("hashList", hashList);
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
        reply.setReplyDate(LocalDateTime.now());
        reply.setBoard(boardRepository.findByBoardIndex(boardIndex));
        reply.setUser(userRepository.findByUserId(author));

        replyRepository.save(reply);


        Board board = boardRepository.findByBoardIndex(boardIndex);

        List<Reply> replyList = replyService.getBoardView(board);
        List<ReReply> reReplyList = new ArrayList<>();
        for(Reply replies : replyList) {
            reReplyList.addAll(reReplyService.getReplyView(replies));
        }
        board.setReplyNum((long)(replyList.size()+reReplyList.size()));
        boardRepository.save(board);


        return "redirect:/board/" + boardIndex;

    }


    @GetMapping(value="/board/movieSearch")
    public String movieSearch(){
        return "board/movieSearch";
    }
    @PostMapping(value="/board/movieSearch")
    public void movieSearch(String keyword, Model model, @PageableDefault(page=0, size=10, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Movie> movieList = movieRepository.findByMovieTitleContaining(keyword, pageable);
        boardService.paging(movieList, model);
        model.addAttribute("movieList", movieList);

    }
    @GetMapping(value = "/hashtag")
    public String hashtag(String hashtag, Model model, @PageableDefault(page=0, size=10, sort="boardIndex", direction = Sort.Direction.DESC) Pageable pageable){
        List<Long> indexList = boardHashtagsService.findHashBoardIndex(hashtag);
        Page<Board> hashList = boardRepository.findByBoardIndexIn(indexList, pageable);

        model.addAttribute("hashList", hashList);

        boardService.paging(hashList, model);

        return "board/hashtag";


    }

    @GetMapping(value="board/delete/{boardIndex}")
    public String deleteBoard(@PathVariable("boardIndex") Long boardIndex){
        Board board = boardRepository.findByBoardIndex(boardIndex);
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
        List<ReReply> reReplyList = new ArrayList<>();
        for(Reply replies : replyList) {
            reReplyList.addAll(reReplyService.getReplyView(replies));
        }
        board.setReplyNum((long)(replyList.size()+reReplyList.size()));
        boardRepository.save(board);


        return "redirect:/board/" + index;
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";

        Object principal = authentication.getPrincipal();

        return email;
    }

    @GetMapping(value="/reReply/{boardIndex}/{replyIndex}")
    public String reReplyWrite(@ModelAttribute("reReplyFormDto") ReReplyFormDto reReplyFormDto,
                               @PathVariable("replyIndex") Long replyIndex, @PathVariable("boardIndex") Long boardIndex, Model model){

        ReReply reReply = new ReReply();

        BoardDto boardDto = boardService.getBoardView(boardIndex);
        Board board = boardRepository.findByBoardIndex(boardIndex);
        model.addAttribute("boardDto", boardDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String author = authentication.getName();
        reReply.setReReplyContent(reReplyFormDto.getReReplyContent());
        reReply.setReReplyDate(LocalDateTime.now());
        reReply.setReply(replyRepository.findByReplyIndex(replyIndex));
        reReply.setUser(userRepository.findByUserId(author));
        reReplyRepository.save(reReply);

        List<Reply> replyList = replyRepository.findByBoard(board);
        List<ReReply> reReplyList = new ArrayList<>();
        for(Reply replies : replyList) {
            reReplyList.addAll(reReplyService.getReplyView(replies));
        }
        board.setReplyNum((long)(replyList.size()+reReplyList.size()));
        boardRepository.save(board);


        return "redirect:/board/" + boardIndex;


    }

    @GetMapping(value="reReply/delete/{reReplyIndex}")
    public String deleteReReply(@PathVariable("reReplyIndex") Long reReplyIndex){
        ReReply reReply = reReplyRepository.findByReReplyIndex(reReplyIndex);
        Long index = reReply.getReply().getReplyIndex();
        Reply reply = replyRepository.findByReplyIndex(index);

        Board board = reply.getBoard();
        Long boardIndex = board.getBoardIndex();

        reReplyService.deleteReReply(reReplyIndex);
        List<Reply> replyList = replyRepository.findByBoard(board);
        List<ReReply> reReplyList = new ArrayList<>();
        for(Reply replies : replyList) {
            reReplyList.addAll(reReplyService.getReplyView(replies));
        }
        board.setReplyNum((long)(replyList.size()+reReplyList.size()));
        boardRepository.save(board);
        return "redirect:/board/" + boardIndex;
    }

}


