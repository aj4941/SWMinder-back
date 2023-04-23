package com.swm47.swminder.Member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    private String username;

    private String loginId;

    private String password;

    private String profileImage;

    private String contact;

    private Date birth;

    private String email;

    private String address;

    private String education;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @CollectionTable(joinColumns = @JoinColumn(name = "member_id"))
    private List<String> skills = new ArrayList<>();

}
