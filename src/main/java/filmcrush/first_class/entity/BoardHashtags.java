package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "board_hashtags")
public class BoardHashtags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bhashIndex; // 게시판 해시코드 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_index")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_index")
    private Hashtags hashtags;
}
