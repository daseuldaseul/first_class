package filmcrush.first_class.dto;

import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto1 {

    private Long boardIndex;    // 게시판 고유번호

    private String boardTitle; // 게시글 제목

    private User user; // 게시글 작성자

    private Long boardScore; // 유저 개인의 별점

    private Movie movie;

    private String boardContent; // 리뷰 본문 내용

    private Long replyNum; // 댓글 수

    private Long likeNum; // 좋아요 수

    private Long viewNum; // 조회수

    private LocalDateTime boardDate; // 작성일자



}
