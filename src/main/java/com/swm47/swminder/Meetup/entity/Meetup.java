package com.swm47.swminder.Meetup.entity;

import com.swm47.swminder.Comment.entity.Comment;
import com.swm47.swminder.MemberMeetup.entity.MemberMeetup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meetup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetupId;

    private String title;

    private String category;

    private String author;

    private Date createdDate;

    private Date startTime;

    private Date endTime;

    @OneToMany(mappedBy = "meetup", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "meetup", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberMeetup> memberMeetups = new ArrayList<>();
}
