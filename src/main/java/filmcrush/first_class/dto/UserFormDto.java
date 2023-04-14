package filmcrush.first_class.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다")
    private String name;       // 이름

    @NotEmpty(message = "이메일은 필수 입력 값입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;      // 이메일

    @NotEmpty(message = "아이디는 필수 입력 값입니다")
    private String id;         // 아이디

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다")
    @Length(min = 8, message = "비밀번호는 8자 이상으로 입력해주세요")
    private String password;   // 비밀번호

    private String nickname;   // 닉네임
}
