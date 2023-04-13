package filmcrush.first_class.service;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public void write(Board board) {
        boardRepository.save(board);
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Page<Board> searchTitle(String title, Pageable pageable){
        Page<Board> boardList = boardRepository.findByBoardTitleContaining(title, pageable);
        return boardList;
    }

    @Transactional
    public Page<Board> searchMovie(Movie movie, Pageable pageable){
        Page<Board> boardList = boardRepository.findByMovie(movie, pageable);
        return boardList;
    }

//    Page<Board> searchKeyword(String searchType, String title, Pageable pageable){
//        Page<Board> boardList = boardRepository.findByBoardTitleContaining(title, pageable);
//        return boardList;
//    }
}
