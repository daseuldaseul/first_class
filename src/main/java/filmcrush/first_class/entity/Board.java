package filmcrush.first_class.entity;

import filmcrush.first_class.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private Long replyNum; // 댓글 수, 댓글

    private Long likeNum; // 좋아요 수

    private Long viewNum; // 조회수

    private LocalDateTime boardDate; // 작성일자

    public void updateBoard(BoardFormDto boardFormDto, Movie movie){
        this.boardIndex = boardFormDto.getBoardIndex();
        this.boardTitle = boardFormDto.getBoardTitle();
        this.boardScore = boardFormDto.getBoardScore();
        this.movie = movie;
        this.boardContent = boardFormDto.getBoardContent();

    }

}
