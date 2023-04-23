package com.swm47.swminder.Member.controller;

import com.swm47.swminder.Member.entity.MemberDTO;
import com.swm47.swminder.Member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/signUp")
    public ResponseEntity<String> signUp() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Long> signUp(@RequestBody MemberDTO memberDTO) {
        Long memberId = memberService.saveMember(memberDTO);
        return new ResponseEntity<>(memberId, HttpStatus.OK);
    }
}
