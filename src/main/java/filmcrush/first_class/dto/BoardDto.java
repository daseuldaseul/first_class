package filmcrush.first_class.dto;

import filmcrush.first_class.entity.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDto {
    private Long boardIndex;

    private String boardTitle;

    private Long boardScore;

    private String boardContent;

    private Long replyNum; // 댓글 수, 댓글

    private Long likeNum;

    private Long viewNum;

    private LocalDateTime boardDate;

    private Movie movie;

    private Users user;

    private MovieImg movieImg;

    private static ModelMapper modelMapper = new ModelMapper();
    // 소스 객체와 대상 객체 간의 값을 자동으로 매핑.

    public static BoardDto of(Board board) {
        return modelMapper.map(board, BoardDto.class);
    }
    // board 엔티티 객체로 받아 board 객체의 자료형과 멤버변수 명이 같을때 boardDto로 값을 복사해서 반환
    // static -> 객체를 생성하지 않아도 호출 가능
}
