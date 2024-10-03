package prs.midwit.PetaProject.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import prs.midwit.PetaProject.member.dto.req.MemberSignupRequest;
import prs.midwit.PetaProject.member.service.MemberService;

@Controller
@RequestMapping("api/v1/members")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignUp(@RequestBody MemberSignupRequest request) {
        memberService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
