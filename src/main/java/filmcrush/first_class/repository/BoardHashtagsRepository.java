package filmcrush.first_class.repository;

import filmcrush.first_class.entity.BoardHashtags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardHashtagsRepository extends JpaRepository<BoardHashtags, Long> {
    BoardHashtags findByHashtags(Long hashIndex);
}
