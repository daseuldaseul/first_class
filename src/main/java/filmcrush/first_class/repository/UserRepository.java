package filmcrush.first_class.repository;



import filmcrush.first_class.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, String >{

    Users findByUserName(String id);

    List<Users> findByUserNicknameContaining(String keyword);
}
