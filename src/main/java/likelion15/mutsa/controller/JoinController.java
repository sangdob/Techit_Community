package likelion15.mutsa.controller;

import likelion15.mutsa.dto.JoinDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final JoinService joinService;

    // 초기화 - db에 임의의 유저 저장(로그인,회원가입 테스트를 위해)
    @GetMapping("/create")
    @ResponseBody
    public String createUser(){
        joinService.createUser();
        return "김다미 유저정보 db 저장 완료";
    }

    // 회원가입 페이지
    @GetMapping("/join-view")
    public String joinView(
    ){
        return "join";
    }

    @GetMapping("/complete-join")
    public String showCompleteJoin(@RequestParam("realName") String realName, Model model) {
        model.addAttribute("realName", realName);
        return "complete-join";
    }

    @PostMapping("/join")
    public String completeJoin(JoinDto joinDto,
                               RedirectAttributes re, Model model) {

        if (joinService.checkEmailDuplicate(joinDto.getEmail())) {
            re.addFlashAttribute("error","이미 가입된 이메일입니다.");
            return "redirect:/join-view";
        } else if (joinService.checkUsernameDuplicate(joinDto.getName())) {
            re.addFlashAttribute("error","이미 존재하는 닉네임입니다.");
            return "redirect:/join-view";
        }

        User user = joinService.joinUser(joinDto);

        System.out.println("user = " + user);

        re.addAttribute("realName", joinDto.getRealName());
        return "redirect:/complete-join";
    }
}