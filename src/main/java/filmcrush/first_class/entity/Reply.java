package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reply")
@ToString
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyIndex; // 댓글 고유번호

    private Long replyBoardIndex; // 댓글 단 게시판의 고유번호

    private String replyWriter; // 댓글 작성자

    private String replyContent; // 댓글 내용

    private LocalDateTime reply_date; // 댓글 작성 시간

}
