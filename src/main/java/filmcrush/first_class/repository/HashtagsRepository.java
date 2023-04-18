package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Hashtags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagsRepository  extends JpaRepository<Hashtags, Long> {
    List<Hashtags> findByHashIndexIn(List<Long> index);

    Hashtags findByHashName(String hashtags);

}
