package com.likelion.scul.club.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubUser is a Querydsl query type for ClubUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClubUser extends EntityPathBase<ClubUser> {

    private static final long serialVersionUID = 666838700L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubUser clubUser = new QClubUser("clubUser");

    public final QClub club;

    public final NumberPath<Long> ClubUserId = createNumber("ClubUserId", Long.class);

    public final com.likelion.scul.common.domain.QUser user;

    public QClubUser(String variable) {
        this(ClubUser.class, forVariable(variable), INITS);
    }

    public QClubUser(Path<? extends ClubUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubUser(PathMetadata metadata, PathInits inits) {
        this(ClubUser.class, metadata, inits);
    }

    public QClubUser(Class<? extends ClubUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.likelion.scul.common.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

