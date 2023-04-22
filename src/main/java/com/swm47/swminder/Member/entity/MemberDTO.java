package com.swm47.swminder.Member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

    private String username;

    private String loginId;

    private String password;

    private String profileImage;

    private String contact;

    private Date birth;

    private String email;

    private String address;

    private String education;

    private List<String> skills;

    public Member toEntity() {
        Member member = Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .profileImage(profileImage)
                .contact(contact)
                .birth(birth)
                .email(email)
                .address(address)
                .education(education)
                .build();

        skills.forEach(skill -> member.getSkills().add(skill));
        return member;
    }
}
