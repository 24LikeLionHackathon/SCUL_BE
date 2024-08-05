package com.likelion.scul.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserSports is a Querydsl query type for UserSports
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserSports extends EntityPathBase<UserSports> {

    private static final long serialVersionUID = -1835099862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserSports userSports = new QUserSports("userSports");

    public final QSports sports;

    public final NumberPath<Integer> sportsPriority = createNumber("sportsPriority", Integer.class);

    public final QUser user;

    public final NumberPath<Long> userSportId = createNumber("userSportId", Long.class);

    public QUserSports(String variable) {
        this(UserSports.class, forVariable(variable), INITS);
    }

    public QUserSports(Path<? extends UserSports> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserSports(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserSports(PathMetadata metadata, PathInits inits) {
        this(UserSports.class, metadata, inits);
    }

    public QUserSports(Class<? extends UserSports> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sports = inits.isInitialized("sports") ? new QSports(forProperty("sports")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

