package com.likelion.scul.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sportsId;
    private String sportsName;
    private String sportsIcon;
    private String sportsDefaultImg;


    // Getters and Setters

    public Long getSportsId() {
        return sportsId;
    }

    public void setSportsId(Long sportsId) {
        this.sportsId = sportsId;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public String getSportsIcon() {
        return sportsIcon;
    }

    public void setSportsIcon(String sportsIcon) {
        this.sportsIcon = sportsIcon;
    }

    public String getSportsDefaultImg() {
        return sportsDefaultImg;
    }

    public void setSportsDefaultImg(String sportsDefaultImg) {
        this.sportsDefaultImg = sportsDefaultImg;
    }
}
