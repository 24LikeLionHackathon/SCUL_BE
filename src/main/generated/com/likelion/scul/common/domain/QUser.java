package com.likelion.scul.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1372608053L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final ListPath<com.likelion.scul.club.domain.ClubUser, com.likelion.scul.club.domain.QClubUser> clubUsers = this.<com.likelion.scul.club.domain.ClubUser, com.likelion.scul.club.domain.QClubUser>createList("clubUsers", com.likelion.scul.club.domain.ClubUser.class, com.likelion.scul.club.domain.QClubUser.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final QUserImage userImage;

    public final ListPath<UserSports, QUserSports> userSports = this.<UserSports, QUserSports>createList("userSports", UserSports.class, QUserSports.class, PathInits.DIRECT2);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userImage = inits.isInitialized("userImage") ? new QUserImage(forProperty("userImage"), inits.get("userImage")) : null;
    }

}

