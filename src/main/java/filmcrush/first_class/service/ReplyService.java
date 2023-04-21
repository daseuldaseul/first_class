package filmcrush.first_class.service;

import filmcrush.first_class.dto.ReplyDto;
import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Reply;
import filmcrush.first_class.repository.ReReplyRepository;
import filmcrush.first_class.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ReReplyRepository reReplyRepository;

    @Transactional
    public List<Reply> getBoardView(Board board){
        List<Reply> replyList = replyRepository.findByBoard(board);
        return replyList;
    }

    @Transactional
    public void deleteReply(Long replyIndex) {
        Reply reply = replyRepository.findByReplyIndex(replyIndex);
        reReplyRepository.deleteByReply(reply);
        replyRepository.deleteByReplyIndex(replyIndex);
    }
}
