package filmcrush.first_class.dto;


import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Movie;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardUpdateDto {

    private Long boardIndex;

    private String boardTitle; // 게시글 제목

    private Long boardScore; // 유저 개인의 별점

    private Movie movie;

    private String boardContent; // 리뷰 본문 내용

    private static ModelMapper modelMapper = new ModelMapper();

    public Board createBoard() {
        return modelMapper.map(this, Board.class);
    }

    public static BoardUpdateDto of(Board board) {
        return modelMapper.map(board, BoardUpdateDto.class);
    }
}