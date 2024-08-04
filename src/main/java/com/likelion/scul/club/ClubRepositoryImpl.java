package com.likelion.scul.club;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

import static com.likelion.scul.club.QClub.club;

public class ClubRepositoryImpl extends QuerydslRepositorySupport implements ClubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ClubRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Club.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Club> findBySearchOption(Long sportsId, String status, LocalDate date, String place, int minCost, int maxCost, int participantCount, String searchCondition, String SearchText) {
        QClub club = QClub.club;

        JPQLQuery<Club> query = queryFactory.selectFrom(club)
                .where(eqSports(sportsId), eqStatus(status), eqDate(date), eqPlace(place), filterCost(minCost, maxCost), eqParticipantCount(participantCount), searchContent(searchCondition, SearchText))
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
        if (status == null || status.isEmpty()) {
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

    private BooleanExpression eqParticipantCount(Integer participantCount) {
        if (participantCount == null || participantCount == 0) {
            return null;
        }
        return club.clubParticipateNumber.eq(participantCount);
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
            case "작성자" -> club.user.nickname.contains(searchText);
            default -> null;
        };
    }
}
