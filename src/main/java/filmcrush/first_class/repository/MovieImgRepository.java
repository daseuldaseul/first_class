package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieImgRepository extends JpaRepository<MovieImg, Long> {
    List<MovieImg> findByMovie(Movie movie);


}
