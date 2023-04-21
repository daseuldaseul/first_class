package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reReply")
@ToString
public class ReReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reReplyIndex; // 댓글 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_index")
    private Reply reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private String reReplyContent; // 댓글 내용

    @Column(nullable = false)
    private LocalDateTime reReplyDate; // 댓글 작성 시간

}