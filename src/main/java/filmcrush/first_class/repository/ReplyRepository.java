package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
