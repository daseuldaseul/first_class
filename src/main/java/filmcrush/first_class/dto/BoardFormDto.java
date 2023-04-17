package filmcrush.first_class.dto;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import filmcrush.first_class.entity.Reply;
import filmcrush.first_class.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardFormDto {

    private Long boardIndex;    // 게시판 고유번호

    private String boardTitle; // 게시글 제목

    private Users user; // 게시글 작성자

    private Long boardScore; // 유저 개인의 별점

    private String movie;

    private String boardContent; // 리뷰 본문 내용

    private List<Reply> replyNum; // 댓글 수

    private Long likeNum; // 좋아요 수

    private Long viewNum; // 조회수

    private LocalDateTime boardDate; // 작성일자

    private static ModelMapper modelMapper = new ModelMapper();

    public Board createBoard() {
        return modelMapper.map(this, Board.class);
    }

    public static BoardFormDto of(Board board) {
        return modelMapper.map(board, BoardFormDto.class);
    }
}
