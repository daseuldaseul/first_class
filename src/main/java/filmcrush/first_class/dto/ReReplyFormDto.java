package filmcrush.first_class.dto;

import filmcrush.first_class.entity.Reply;
import filmcrush.first_class.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReReplyFormDto {
    private Long reReplyIndex;

    private Reply reply;

    private Users user;

    private String reReplyContent;

    private LocalDateTime reReplyDate;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReReplyDto of(Reply reply) {
        return modelMapper.map(reply, ReReplyDto.class);
    }
}