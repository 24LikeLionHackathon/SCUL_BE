package com.likelion.scul.club.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubApplication is a Querydsl query type for ClubApplication
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubApplication extends EntityPathBase<ClubApplication> {

    private static final long serialVersionUID = 2100230991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubApplication clubApplication = new QClubApplication("clubApplication");

    public final com.likelion.scul.common.domain.QUser applicant;

    public final StringPath applicantIntro = createString("applicantIntro");

    public final QClub club;

    public final NumberPath<Long> clubApplicationId = createNumber("clubApplicationId", Long.class);

    public final com.likelion.scul.common.domain.QUser leader;

    public QClubApplication(String variable) {
        this(ClubApplication.class, forVariable(variable), INITS);
    }

    public QClubApplication(Path<? extends ClubApplication> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubApplication(PathMetadata metadata, PathInits inits) {
        this(ClubApplication.class, metadata, inits);
    }

    public QClubApplication(Class<? extends ClubApplication> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.applicant = inits.isInitialized("applicant") ? new com.likelion.scul.common.domain.QUser(forProperty("applicant")) : null;
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.leader = inits.isInitialized("leader") ? new com.likelion.scul.common.domain.QUser(forProperty("leader")) : null;
    }

}

