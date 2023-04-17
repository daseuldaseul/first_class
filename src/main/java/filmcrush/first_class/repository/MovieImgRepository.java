package filmcrush.first_class.repository;

import filmcrush.first_class.entity.MovieImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieImgRepository extends JpaRepository<MovieImg, Long> {

}
