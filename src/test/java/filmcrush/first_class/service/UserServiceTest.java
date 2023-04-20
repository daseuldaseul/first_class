package filmcrush.first_class.service;

import filmcrush.first_class.dto.UserFormDto;
import filmcrush.first_class.entity.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Users createUser1() {
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setId("ektmfdl");
        userFormDto.setName("홍길동");
        userFormDto.setEmail("ektmfdl@ektmfdl");
        userFormDto.setPassword("12345678");
        userFormDto.setNickname("다슬이");
        return Users.createUser(userFormDto, passwordEncoder);
    }

    public Users createUser2() {
        UserFormDto userFormDto = new UserFormDto();
        userFormDto.setId("ektmfdl");
        userFormDto.setName("홍길동2");
        userFormDto.setEmail("ektmfdl2@ektmfdl");
        userFormDto.setPassword("12345678");
        userFormDto.setNickname("다슬이2");
        return Users.createUser(userFormDto, passwordEncoder);
    }


    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateUserTest() {
        Users user1 = createUser1();
        Users user2 = createUser2();
        userService.saveUser(user1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            userService.saveUser(user2);
        });

        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }




}