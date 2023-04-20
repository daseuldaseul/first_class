package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="user_like")
@Getter
@Setter

public class UserLike {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id; // 회원 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_index")
    private Board board;

    public UserLike(Users user, Board board){
        this.user = user;
        this.board= board;
    }
    public UserLike() {
        // default constructor
    }
}
