package com.swm47.swminder.Member.service;

import com.swm47.swminder.Member.entity.Member;
import com.swm47.swminder.Member.entity.MemberDTO;
import com.swm47.swminder.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long saveMember(MemberDTO memberDTO) {
        memberDTO.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
        Member savedMember = memberRepository.save(memberDTO.toEntity());
        return savedMember.getMemberId();
    }
}
