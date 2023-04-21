package filmcrush.first_class.service;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.dto.BoardUpdateDto;
import filmcrush.first_class.dto.ReplyDto;
import filmcrush.first_class.entity.*;
import filmcrush.first_class.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserLikeRepository userLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardHashtagsRepository boardHashtagsRepository;

    @Autowired
    private HashtagsService hashtagsService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReReplyRepository reReplyRepository;

    @Autowired
    ReReplyService reReplyService;

    @Autowired
    ReplyService replyService;

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }


    public Page<Board> viewBoardList(Pageable pageable){
        Page<Board> boardList = boardRepository.findAllByOrderByViewNumDesc(pageable);
        return boardList;
    }

    public Page<Board> dateBoardList(Pageable pageable){
        Page<Board> boardList = boardRepository.findAllByOrderByBoardDateDesc(pageable);
        return boardList;
    }

    public Page<Board> likeBoardList(Pageable pageable){
        Page<Board> boardList = boardRepository.findAllByOrderByLikeNumDesc(pageable);
        return boardList;
    }


    @Transactional
    public BoardDto getBoardView(Long boardIndex){
        Board board = boardRepository.findByBoardIndex(boardIndex);
        BoardDto boardDto = BoardDto.of(board);
        return boardDto;
    }

    public Optional<UserLike> findLike(Board board, Users user) {

        return userLikeRepository.findByUserAndBoard(user, board);

    }

    public int saveLike(Board board, Users user) {

        /** 로그인한 유저가 해당 게시물을 좋아요 했는지 안 했는지 확인 **/
        if(findLike(board, user).isEmpty()){

            /* 좋아요 하지 않은 게시물이면 좋아요 추가, true 반환 */
            user = userRepository.findById(user.getUserId()).orElseThrow(() ->
                    new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
            board = boardRepository.findById(board.getBoardIndex()).orElseThrow(() ->
                    new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

            /* 좋아요 엔티티 생성 */
//            좋아요가 없으면 추가
            board.setLikeNum(board.getLikeNum()+1);

            UserLike userLike = new UserLike(user, board);
            userLikeRepository.save(userLike);

            return 1;
        } else {

            /* 좋아요 한 게시물이면 좋아요 삭제, false 반환 */
            board.setLikeNum(board.getLikeNum()-1);

            userLikeRepository.deleteByUserAndBoard(user, board);

            return 0;
        }
    }

    public Board findBoard(Long boardIndex){
        Board board = boardRepository.findByBoardIndex(boardIndex);
        return board;
    }

    @Transactional
    public void deleteBoard(Long boardIndex){

        Board board = boardRepository.findByBoardIndex(boardIndex);

        List<Reply> replyList = replyService.getBoardView(board);
        List<ReReply> reReplyList = new ArrayList<>();
        for(Reply replies : replyList) {
            reReplyList.addAll(reReplyService.getReplyView(replies));
        }

        for(ReReply reReplies : reReplyList){
            reReplyRepository.deleteByReReplyIndex(reReplies.getReReplyIndex());
        }

        replyRepository.deleteByBoard(board);
        userLikeRepository.deleteByBoard(board);
        boardHashtagsRepository.deleteByBoard(board);
        boardRepository.deleteByBoardIndex(boardIndex);

    }




    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardIndex){
        Board board = boardRepository.findById(boardIndex)
                .orElseThrow(EntityNotFoundException::new);

        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setBoardIndex(board.getBoardIndex());
        boardFormDto.setMovie(board.getMovie().getMovieTitle());
        boardFormDto.setBoardTitle(board.getBoardTitle());
        boardFormDto.setBoardContent(board.getBoardContent());
        boardFormDto.setBoardScore(board.getBoardScore());
        boardFormDto.setReplyNum(board.getReplyNum());
        boardFormDto.setBoardDate(board.getBoardDate());
        boardFormDto.setLikeNum(board.getLikeNum());
        boardFormDto.setViewNum(board.getViewNum());
        boardFormDto.setUser(board.getUser());

        List<BoardHashtags> boardHashList = boardHashtagsRepository.findByBoard(board);
        List<String> hash = new ArrayList<>();
        for(BoardHashtags boardHashtags : boardHashList){
            hash.add(boardHashtags.getHashtags().getHashName());
        }
        String result = "";
        for(String returnHash : hash){
            result += "#";
            result += (returnHash + " ");
        }
        boardFormDto.setHashtags(result);


        return boardFormDto;
    }

    @Transactional
    public Long updateBoard(BoardFormDto boardFormDto) throws Exception{
        Board board = boardRepository.findByBoardIndex(boardFormDto.getBoardIndex());
        Movie movie = movieRepository.findByMovieTitle(boardFormDto.getMovie());
        board.updateBoard(boardFormDto, movie);
        boardRepository.save(board);
        boardHashtagsRepository.deleteByBoard(board);

        String hashtags = boardFormDto.getHashtags();
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

        return board.getBoardIndex();
    }

    @Transactional
    public void searchTitle(@RequestParam String keyword, Model model, @PageableDefault(page = 0, size = 3, sort = "boardIndex",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Board> searchList = boardRepository.findByBoardTitleContaining(keyword, pageable);

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

    }


    @Transactional
    public void searchMovieTitle(@RequestParam String keyword, Model model, @PageableDefault(page = 0, size = 3, sort = "boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {
        List<Movie> movieList = movieRepository.findByMovieTitleContaining(keyword);
        // keyword를 가진 Movie 객체 리스트를 movieList 담음

        Page<Board> searchList = boardRepository.findByMovieIn(movieList, pageable);


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

    }

    @Transactional
    public void searchUserNickname(@RequestParam String keyword, Model model, @PageableDefault(page = 0, size = 3, sort = "boardIndex", direction = Sort.Direction.DESC) Pageable pageable) {

        List<Users> userList = userRepository.findByUserNicknameContaining(keyword);
        Page<Board> searchList = boardRepository.findByUserIn(userList, pageable);


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


    }

    @Transactional
    public void boardWrite(String author, String hashtags, BoardFormDto boardFormDto, Model model){


        Board board = new Board();
        board.setBoardTitle(boardFormDto.getBoardTitle());
        board.setBoardContent(boardFormDto.getBoardContent());
        board.setBoardDate(LocalDateTime.now());
        board.setLikeNum(0L);
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


    }
    @Transactional
    public void paging(Page page, Model model){
        int nowPage = page.getPageable().getPageNumber() + 1;

        int startPage = Math.max(nowPage - 2, 1);

        int endPage = Math.min(nowPage + 2, page.getTotalPages());

        if (nowPage == 1) {
            endPage = Math.min(nowPage + 4, page.getTotalPages());
        } else if (nowPage == 2) {
            endPage = Math.min(nowPage + 3, page.getTotalPages());
        }

        if (nowPage == (page.getTotalPages() - 1)) {
            startPage = Math.min(nowPage - 3, page.getTotalPages());
        } else if (nowPage == (page.getTotalPages())) {
            startPage = Math.min(nowPage - 4, page.getTotalPages());
        }

        if (startPage < 1) {
            startPage = 1;
        }

        int prevPage = nowPage - 1;
        int nextPage = nowPage + 1;

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("totalPages", page.getTotalPages());
    }
}