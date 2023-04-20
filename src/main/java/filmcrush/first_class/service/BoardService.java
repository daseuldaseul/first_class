package filmcrush.first_class.service;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.dto.BoardUpdateDto;
import filmcrush.first_class.dto.ReplyDto;
import filmcrush.first_class.entity.*;
import filmcrush.first_class.repository.BoardHashtagsRepository;
import filmcrush.first_class.repository.BoardRepository;
import filmcrush.first_class.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardHashtagsRepository boardHashtagsRepository;

    @Autowired
    private HashtagsService hashtagsService;

    @Autowired
    private MovieRepository movieRepository;

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

    @Transactional
    public void deleteBoard(Long boardIndex){

        Board board = boardRepository.findByBoardIndex(boardIndex);
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



}
