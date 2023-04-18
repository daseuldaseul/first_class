package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.BoardHashtags;
import filmcrush.first_class.entity.Hashtags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardHashtagsRepository extends JpaRepository<BoardHashtags, Long> {

    List<BoardHashtags> findByBoard(Board board);

    List<BoardHashtags> findByHashtags(Hashtags hashtags);


}
