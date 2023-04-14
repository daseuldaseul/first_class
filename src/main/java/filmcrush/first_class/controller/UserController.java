package filmcrush.first_class.controller;

import filmcrush.first_class.dto.UserFormDto;
import filmcrush.first_class.entity.Users;
import filmcrush.first_class.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String userForm(Model model){
        model.addAttribute("userFormDto", new UserFormDto());
        return "/user/userForm";
    }

//    @PostMapping(value =  "/new")
//    public String userForm(MemberFormDto userFormDto){
//        Member member = Member.createMember(memberFormDto, passwordEncoder);
//        memberService.saveMember(member);
//
//        return "redirect:/";
//    }

    @PostMapping(value = "/new")
    public String newUser(@Valid UserFormDto userFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "/user/userForm";
        }
        try{
            Users user = Users.createUser(userFormDto, passwordEncoder);
            userService.saveUser(user);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/user/userForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginUser(){
        return "/user/userLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/user/userLoginForm";
    }
}
