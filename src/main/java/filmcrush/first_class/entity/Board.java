package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIndex;    // 게시판 고유번호

    private String boardTitle; // 게시글 제목

    private String boardWriter; // 게시글 작성자

    private Long boardScore; // 유저 개인의 별점

    private String boardMovieTitle; // 게시글에서 설정한 영화 제목

    private String boardContent; // 리뷰 본문 내용

    private Long replyNum; // 댓글 수

    private Long likeNum; // 좋아요 수

    private Long viewNum; // 조회수

    private LocalDateTime boardDate; // 작성일자
}
