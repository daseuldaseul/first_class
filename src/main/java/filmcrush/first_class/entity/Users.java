package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
public class Users {

    @Id
    @Column(name = "user_id")
    private String id; // 회원 아이디

    @Column(nullable = false)
    private String userPassword; // 비밀번호

    @Column(nullable = false)
    private String userNickname; // 닉네임

    @Column(nullable = false)
    private String userName; // 이름

    @Column(unique = true, nullable = false)
    private String userEmail; // 이메일

}
