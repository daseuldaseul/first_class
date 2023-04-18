package filmcrush.first_class.dto;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.Reply;
import filmcrush.first_class.entity.Users;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyFormDto {
    private Long replyIndex;

    private Board board;

    private Users user;

    private String replyContent;

    private LocalDateTime reply_date;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReplyDto of(Reply reply) {
        return modelMapper.map(reply, ReplyDto.class);
    }
}