package com.likelion.scul.club.repository;

import com.likelion.scul.club.domain.Club;
import com.likelion.scul.club.domain.QClub;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

import static com.likelion.scul.club.domain.QClub.club;

public class ClubRepositoryImpl extends QuerydslRepositorySupport implements ClubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ClubRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Club.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Club> findBySearchOption(Long sportsId, String status, LocalDate date, String place, int minCost, int maxCost, int participantMinCount, int participantMaxCount, String searchCondition, String searchText) {
        QClub club = QClub.club;

        System.out.println("sportsId:" + sportsId);
        System.out.println("status:" + status);
        System.out.println("date:" + date);
        System.out.println("searchCondition: " + searchCondition);
        System.out.println("searchText: " + searchText);

        JPQLQuery<Club> query = queryFactory.selectFrom(club)
                .where(eqSports(sportsId), eqStatus(status), eqDate(date), eqPlace(place), filterCost(minCost, maxCost), filterParticipantCount(participantMinCount, participantMaxCount), searchContent(searchCondition, searchText))
                .orderBy(club.createdAt.desc());

        return query.fetch();
    }

    private BooleanExpression eqSports(Long sportsId) {
        if (sportsId == null) {
            return null;
        }
        return club.sports.sportsId.eq(sportsId);
    }

    private BooleanExpression eqStatus(String status) {
        if (status == null || status.isEmpty() || status.equals("마감 포함")) {
            return null;
        }

        return club.clubStatus.eq(status);
    }

    private BooleanExpression eqDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return club.clubDate.eq(date);
    }


    private BooleanExpression filterCost(Integer minCost, Integer maxCost) {
        if ((minCost == null || minCost == 0) && (maxCost == null || maxCost == 0)) {
            return null;
        }
        BooleanExpression costCondition = null;
        if (minCost != null && minCost > 0) {
            costCondition = club.clubCost.goe(minCost);
        }
        if (maxCost != null && maxCost > 0) {
            if (costCondition == null) {
                costCondition = club.clubCost.loe(maxCost);
            } else {
                costCondition = costCondition.and(club.clubCost.loe(maxCost));
            }
        }
        return costCondition;
    }

    private BooleanExpression filterParticipantCount(Integer participateMinCount, Integer participateMaxCount) {

        if ((participateMinCount == null || participateMinCount == 0) && (participateMaxCount == null || participateMaxCount == 0)) {
            return null;
        }
        BooleanExpression participateCondition = null;
        if (participateMinCount != null && participateMaxCount > 0) {
            participateCondition = club.clubCost.goe(participateMinCount);
        }
        if (participateMaxCount != null && participateMaxCount > 0) {
            if (participateCondition == null) {
                participateCondition = club.clubCost.loe(participateMaxCount);
            } else {
                participateCondition = participateCondition.and(club.clubCost.loe(participateMaxCount));
            }
        }
        return participateCondition;
    }

    private BooleanExpression eqPlace (String place) {
        if (place == null || place.isEmpty()) {
            return null;
        }
        return club.clubPlace.eq(place);
    }

    private BooleanExpression searchContent (String searchCondition, String searchText) {
        if (searchCondition == null || searchCondition.isEmpty() || searchText == null || searchText.isEmpty()) {
            return null;
        }
        return switch (searchCondition) {
            case "제목" -> club.clubName.contains(searchText);
            case "내용" -> club.clubContent.contains(searchText);
            case "작성자" -> club.user != null ? club.user.nickname.contains(searchText) : null;
            default -> null;
        };
    }
}
