package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByMovieTitle(String movieTitle);

    List<Movie> findByMovieTitleContaining(String keyword);
    Page<Movie> findByMovieTitleContaining(String keyword, Pageable pageable);


}
