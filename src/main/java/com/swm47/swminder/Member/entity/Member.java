package com.swm47.swminder.Member.entity;

import com.swm47.swminder.Board.entity.Board;
import com.swm47.swminder.MemberMeetup.entity.MemberMeetup;
import com.swm47.swminder.MemberMentoring.entity.MemberMentoring;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder @ToString(exclude = "boards")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

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

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberMentoring> memberMentorings = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberMeetup> memberMeetups = new ArrayList<>();
}
