package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIndex;    // 게시판 고유번호

    @Column(nullable = false)
    private String boardTitle; // 게시글 제목

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user; // 게시글 작성자

    private Long boardScore; // 유저 개인의 별점

    @ManyToOne
    @JoinColumn(name = "movie_index")
    private Movie movie;

    private String boardContent; // 리뷰 본문 내용

    private Long likeNum; // 좋아요 수

    private Long viewNum; // 조회수

    private LocalDateTime boardDate; // 작성일자

    private Long replyNum;
}
