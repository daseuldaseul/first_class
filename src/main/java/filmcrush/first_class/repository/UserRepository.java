package filmcrush.first_class.repository;



import filmcrush.first_class.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String >{

    Users findByUserId(String id);

    List<Users> findByUserNicknameContaining(String keyword);

    Users findByUserEmail(String userEmail);

    Users findByUserNickname(String userNickname);
}
