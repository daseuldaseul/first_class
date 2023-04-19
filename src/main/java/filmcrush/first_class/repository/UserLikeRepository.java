package filmcrush.first_class.repository;

import filmcrush.first_class.entity.Board;
import filmcrush.first_class.entity.UserLike;
import filmcrush.first_class.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
//    boolean existsByUserAndBoard(Users user, Board board);
    @Transactional
    void deleteByUserAndBoard(Users user, Board board);

    Optional<UserLike> findByUserAndBoard(Users user, Board board);
//    void  deleteByBoardEntity_IdAndMemberEntity_Id(Long boardId, Long memberId);
}
