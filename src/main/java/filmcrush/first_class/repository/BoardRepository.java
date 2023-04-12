package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository <Board, Long> {

    List<Board> findByBoardTitle(String boardTitle);


}
