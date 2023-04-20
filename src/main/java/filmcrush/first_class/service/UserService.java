package filmcrush.first_class.service;


import filmcrush.first_class.entity.Users;
import filmcrush.first_class.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    @Autowired
    private final UserRepository userRepository;

    public Users saveUser(Users user){
        validateDuplicateUser(user);
        return userRepository.save(user);
    }

    private void validateDuplicateUser(Users user){
        Users findUserEmail = userRepository.findByUserEmail(user.getUserEmail());
        if(findUserEmail != null){
            throw new IllegalStateException("이미 가입된 이메일입니다");
        }

        Users findUserId = userRepository.findByUserId(user.getUserId());
        if(findUserId != null){
            throw new IllegalStateException("이미 사용중인 아이디 입니다");
        }

        Users findUserNickname = userRepository.findByUserNickname(user.getUserNickname());
        if(findUserNickname != null){
            throw new IllegalStateException("이미 사용중인 닉네임 입니다");
        }



    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException{
        Users user = userRepository.findByUserId(id);

        if(user == null){
            throw new UsernameNotFoundException(id);
        }

        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPassword())
                .roles(user.getRole().toString())
                .build();
    }
}
