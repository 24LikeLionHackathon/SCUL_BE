package com.likelion.scul.common.dto.usersports;

import lombok.Data;

import java.util.List;

@Data
public class UserSportsRequest {
    private List<String> sportsName;
}
