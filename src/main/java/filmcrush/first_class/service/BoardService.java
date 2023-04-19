package filmcrush.first_class.service;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.Users;
import filmcrush.first_class.repository.BoardHashtagsRepository;
import filmcrush.first_class.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardHashtagsRepository boardHashtagsRepository;

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
        try {
            Board board = boardRepository.findByBoardIndex(boardIndex);
            boardHashtagsRepository.deleteByBoard(board);
            boardRepository.deleteByBoardIndex(boardIndex);
        }catch(Exception e){
            System.out.println("에러발생");
        }
    }
}
