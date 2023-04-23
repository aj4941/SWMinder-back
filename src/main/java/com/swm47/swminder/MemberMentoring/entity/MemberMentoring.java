package com.swm47.swminder.MemberMentoring.entity;

import com.swm47.swminder.Member.entity.Member;
import com.swm47.swminder.Mentoring.entity.Mentoring;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
public class MemberMentoring {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberMentoringId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
        member.getMemberMentorings().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENTORING_ID")
    private Mentoring mentoring;

    public void addMentoring(Mentoring mentoring) {
        this.mentoring = mentoring;
        mentoring.getMemberMentorings().add(this);
    }

}
