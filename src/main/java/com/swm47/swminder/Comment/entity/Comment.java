package com.swm47.swminder.Comment.entity;

import com.swm47.swminder.Board.entity.Board;
import com.swm47.swminder.Meetup.entity.Meetup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String author;

    private String content;

    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;
    public void addBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEETUP_ID")
    private Meetup meetup;

    public void addMeetup(Meetup meetup) {
        this.meetup = meetup;
        meetup.getComments().add(this);
    }
}
