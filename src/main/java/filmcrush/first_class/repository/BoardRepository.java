package filmcrush.first_class.repository;

import filmcrush.first_class.dto.BoardDto;
import filmcrush.first_class.dto.BoardFormDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.Reply;
import filmcrush.first_class.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {
    List<Board> findByBoardTitle(String boardTitle);

    List<Board> findAll();


    Page<Board> findByBoardTitleContaining(String keyword, Pageable pageable);

    Page<Board> findByMovieIn(List<Movie> movie, Pageable pageable);


    Page<Board> findByUserIn(List<Users> user, Pageable pageable);

    Page<Board> findAllByOrderByBoardDateDesc(Pageable pageable);

    Page<Board> findAllByOrderByViewNumDesc(Pageable pageable);

    Page<Board> findAllByOrderByLikeNumDesc(Pageable pageable);

    Board findByBoardIndex(Long boardIndex);


    void deleteByMovie(Movie movie);

    void deleteByBoardIndex(Long boardIndex);

    Page<Board> findByBoardIndexIn(List<Long> list, Pageable pageable);

    List<Board> findByMovie(Movie movie);

}
