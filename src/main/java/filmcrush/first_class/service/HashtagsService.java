package filmcrush.first_class.service;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.BoardHashtags;
import filmcrush.first_class.entity.Hashtags;
import filmcrush.first_class.repository.BoardHashtagsRepository;
import filmcrush.first_class.repository.HashtagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HashtagsService {

    @Autowired
    BoardHashtagsRepository boardHashtagsRepository;

    @Autowired
    HashtagsRepository hashtagsRepository;

    public List<Hashtags> hashtagsList(Board board){
        List<BoardHashtags> boardHashtagsList = boardHashtagsRepository.findByBoard(board);
        List<Long> indexList = new ArrayList<>();
        for(BoardHashtags hashtagList: boardHashtagsList){
            Hashtags hash = hashtagList.getHashtags();
            indexList.add(hash.getHashIndex());
        }

        List<Hashtags> hashtags = hashtagsRepository.findByHashIndexIn(indexList);
        return hashtags;
    }

    public Hashtags findHash(String hashtags){
        Hashtags tag = hashtagsRepository.findByHashName(hashtags);
        if(tag == null){
            Hashtags hash = new Hashtags();
            hash.setHashName(hashtags);
            hashtagsRepository.save(hash);
            tag = hashtagsRepository.findByHashName(hashtags);
        }
        return tag;
    }
}
