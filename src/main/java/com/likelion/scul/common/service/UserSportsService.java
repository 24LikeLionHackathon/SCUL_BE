package com.likelion.scul.common.service;

import com.likelion.scul.common.domain.Sports;
import com.likelion.scul.common.domain.User;
import com.likelion.scul.common.domain.UserSports;
import com.likelion.scul.common.dto.usersports.UserSportsResponse;
import com.likelion.scul.common.repository.SportsRepository;
import com.likelion.scul.common.repository.UserSportsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void saveUserSports(List<String> sportsName, User newUser) {
        for (int i = 0; i < sportsName.size(); i++) {
            Sports selectedSports = sportsRepository.findBySportsName(sportsName.get(i))
                    .orElseThrow(() -> new RuntimeException("없는 스포츠입니다."));
            UserSports newUserSports = new UserSports();
            newUserSports.setSports(selectedSports);
            newUserSports.setUser(newUser);

            userSportsRepository.save(newUserSports);
        }
    }

    public UserSportsResponse findByUserId(Long userId) {
        UserSportsResponse response = new UserSportsResponse();
        List<String> list = userSportsRepository.findSportsNamesByUserId(userId);
        response.setSportsName(list);
        return response;
    }

    public void deleteUserSports(User user) {
        userSportsRepository.deleteByUserId(user.getUserId());
    }
}
