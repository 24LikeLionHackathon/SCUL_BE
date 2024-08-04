package com.likelion.scul.club.domain;

import com.likelion.scul.common.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ClubApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubApplicationId;
    private String applicantIntro;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private User leader;

    public ClubApplication() {

    }

    public ClubApplication(String applicantIntro, Club club, User applicant, User leader) {
        this.applicantIntro = applicantIntro;
        this.club = club;
        this.applicant = applicant;
        this.leader = leader;
    }

}
