package filmcrush.first_class.repository;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {
    List<Board> findByBoardTitle(String boardTitle);

    List<Board> findAll();

    Page<Board> findByBoardTitleContaining(String keyword, Pageable pageable);

    Page<Board> findByMovieContaining(Movie movie, Pageable pageable);

    Page<Board> findByMovie(Movie movie, Pageable pageable);
}

