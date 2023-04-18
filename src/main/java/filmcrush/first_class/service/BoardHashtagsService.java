package filmcrush.first_class.service;

import filmcrush.first_class.entity.BoardHashtags;
import filmcrush.first_class.entity.Hashtags;
import filmcrush.first_class.repository.BoardHashtagsRepository;
import filmcrush.first_class.repository.HashtagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardHashtagsService {

    @Autowired
    BoardHashtagsRepository boardHashtagsRepository;

    @Autowired
    HashtagsRepository hashtagsRepository;

    public List<Long> findHashBoardIndex(String hashtag){
        Hashtags hashtags = hashtagsRepository.findByHashName(hashtag);
        List<BoardHashtags> hashList = boardHashtagsRepository.findByHashtags(hashtags);

        List<Long> indexList = new ArrayList<>();

        for(BoardHashtags hash : hashList){
            indexList.add(hash.getBoard().getBoardIndex());
        }

        return indexList;
    }

}
