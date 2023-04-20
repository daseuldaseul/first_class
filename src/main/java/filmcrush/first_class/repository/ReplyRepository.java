package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByBoard(Board board);

    void deleteByReplyIndex(Long replyIndex);

    Reply findByReplyIndex(Long replyIndex);

}
