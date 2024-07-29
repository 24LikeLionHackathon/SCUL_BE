package com.likelion.scul.common.service;

import com.likelion.scul.auth.domain.dto.AddUserInfoRequest;
import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.domain.UserSports;
import com.likelion.scul.common.repository.SportsRepository;
import com.likelion.scul.common.repository.UserSportsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSportsService {
    private UserSportsRepository userSportsRepository;
    private SportsRepository sportsRepository;

    public UserSportsService(
            UserSportsRepository userSportsRepository,
            SportsRepository sportsRepository) {
        this.userSportsRepository = userSportsRepository;
        this.sportsRepository = sportsRepository;
    }

    public void saveUserSports(AddUserInfoRequest request, User newUser) {
        for (int i = 0 ; i < request.getSportsName().size(); i++) {

            Sports selectedSports = sportsRepository.findBySportsName(request.getSportsName().get(i))
                    .orElseThrow(() -> new RuntimeException("없는 스포츠입니다."));
            UserSports newUserSports = new UserSports();
            newUserSports.setSports(selectedSports);
            newUserSports.setUser(newUser);
            newUserSports.setSportsPriority(i);

            userSportsRepository.save(newUserSports);
        }
    }
}
