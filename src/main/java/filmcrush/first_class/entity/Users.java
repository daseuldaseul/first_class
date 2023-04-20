package filmcrush.first_class.entity;

import filmcrush.first_class.contant.Role;
import filmcrush.first_class.dto.UserFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
public class Users {

    @Id
    @Column(name = "user_id")
    private String userId; // 회원 아이디

    @Column(nullable = false)
    private String userPassword; // 비밀번호

    @Column(nullable = false)
    private String userNickname; // 닉네임

    @Column(nullable = false)
    private String userName; // 이름

    @Column(unique = true, nullable = false)
    private String userEmail; // 이메일

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<UserLike> likes = new ArrayList<>();

    public static Users createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder){
        Users user = new Users();
        user.setUserId(userFormDto.getId());
        user.setUserName(userFormDto.getName());
        user.setUserEmail(userFormDto.getEmail());
        user.setUserNickname(userFormDto.getNickname());
        String password = passwordEncoder.encode(userFormDto.getPassword());
        user.setUserPassword(password);
        user.setRole(Role.USER);
        return user;
    }

}
