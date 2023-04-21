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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

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

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Page<Board> searchTitle(String title, Pageable pageable){
        Page<Board> boardList = boardRepository.findByBoardTitleContaining(title, pageable);
        return boardList;
    }

    @Transactional
    public Page<Board> searchMovie(List<Movie> movie, Pageable pageable){
        Page<Board> boardList = boardRepository.findByMovieIn(movie, pageable);
        return boardList;
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
    public Page<Board> searchUser(List<Users> user, Pageable pageable){
        Page<Board> boardList = boardRepository.findByUserIn(user, pageable);
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
        boardHashtagsRepository.deleteByBoard(board);
        boardRepository.deleteByBoardIndex(boardIndex);
// 여기하는중
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



}
