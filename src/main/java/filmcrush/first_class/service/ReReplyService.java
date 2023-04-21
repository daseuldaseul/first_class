package filmcrush.first_class.service;

import filmcrush.first_class.entity.ReReply;
import filmcrush.first_class.entity.Reply;
import filmcrush.first_class.repository.ReReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReReplyService {

    @Autowired
    ReReplyRepository reReplyRepository;
    @Transactional
    public List<ReReply> getReplyView(Reply reply){
        List<ReReply> reReplyList = reReplyRepository.findByReply(reply);

        return reReplyList;
    }

    @Transactional
    public void deleteReReply(Long reReplyIndex) {

        reReplyRepository.deleteByReReplyIndex(reReplyIndex);
    }

}

