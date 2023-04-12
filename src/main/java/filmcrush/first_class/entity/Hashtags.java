package filmcrush.first_class.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "hashtags")
@ToString
public class Hashtags {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long hashIndex; // 해시태그 고유번호

    @Column(unique = true, nullable = false)
    private String hashName; // 해시태그 이름
}
