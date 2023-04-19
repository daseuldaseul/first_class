package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByMovieTitle(String movieTitle);

    List<Movie> findByMovieTitleContaining(String keyword);
    Page<Movie> findByMovieTitleContaining(String keyword, Pageable pageable);

    Movie findByMovieIndex(Long movieIndex);

    Page<Movie> findAll(Pageable pageable);

    void deleteByMovieIndex(Long movieIndex);
    //매개변수로 들어온 movieIndex를 찾아서 삭제를 하겠다.
    // void인 이유는 delete하고 끝내야하니까.
}
