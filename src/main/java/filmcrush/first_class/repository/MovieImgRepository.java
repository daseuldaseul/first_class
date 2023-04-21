package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.MovieImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieImgRepository extends JpaRepository<MovieImg, Long> {
    List<MovieImg> findByMovie(Movie movie);

    void deleteByMovie(Movie movie);
    //엔티티에 있는 참조내용인 Movie movie를 가리킨다.

    List<MovieImg> findAllByOrderByMovieDesc();

}
