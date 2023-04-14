package filmcrush.first_class.service;

import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.entity.Board;
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

//    public Long write(BoardFormDto boardFormDto) {
//        Board board = boardFormDto.createBoard();
//        boardRepository.save(board);
//        return board.getBoardIndex();
//    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Page<Board> search(String title, Pageable pageable){
        Page<Board> boardList = boardRepository.findByBoardTitleContaining(title, pageable);
        return boardList;
    }

    @Transactional
    public BoardFormDto getBoardView(Long boardIndex){
        Board board = boardRepository.findByBoardIndex(boardIndex);
        BoardFormDto boardFormDto = BoardFormDto.of(board);
        return boardFormDto;
    }
}
