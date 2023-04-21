package filmcrush.first_class.repository;

import filmcrush.first_class.entity.ReReply;
import filmcrush.first_class.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReReplyRepository extends JpaRepository<ReReply, Long> {

    List<ReReply> findByReply(Reply reply);

    void deleteByReReplyIndex(Long reReplyIndex);

    ReReply findByReReplyIndex(Long reReplyIndex);

    void deleteByReply(Reply reply);

//    void deleteByReply(List<Reply> reply);

}