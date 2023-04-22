package com.swm47.swminder.Member.service;

import com.swm47.swminder.Member.entity.Member;
import com.swm47.swminder.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        // 일단 loginId를 가지고 계정을 찾음
        Optional<Member> result = memberRepository.findByLoginId(username);

        if (result.isPresent()) {
            Member member = result.get();
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            // 여기서는 일단 인증, 인가 데이터를 반환 (User 객체)
            // 내가 로그인 창에서 입력한 비밀번호와 DB에서 암호화된 비밀번호가 일치한지 필터에서 확인됨
            return new User(member.getLoginId(), member.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("회원이 존재하지 않습니다.");
        }
    }
}
