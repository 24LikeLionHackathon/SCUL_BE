package com.likelion.scul.club;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = 103915637L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClub club = new QClub("club");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final StringPath clubContent = createString("clubContent");

    public final NumberPath<Integer> clubCost = createNumber("clubCost", Integer.class);

    public final DatePath<java.time.LocalDate> clubDate = createDate("clubDate", java.time.LocalDate.class);

    public final NumberPath<Long> clubId = createNumber("clubId", Long.class);

    public final StringPath clubImage = createString("clubImage");

    public final StringPath clubName = createString("clubName");

    public final StringPath clubParticipateLink = createString("clubParticipateLink");

    public final NumberPath<Integer> clubParticipateNumber = createNumber("clubParticipateNumber", Integer.class);

    public final StringPath clubPlace = createString("clubPlace");

    public final StringPath clubQnaLink = createString("clubQnaLink");

    public final StringPath clubStatus = createString("clubStatus");

    public final NumberPath<Integer> clubTotalNumber = createNumber("clubTotalNumber", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.likelion.scul.common.domain.QSports sports;

    public final com.likelion.scul.common.domain.QUser user;

    public QClub(String variable) {
        this(Club.class, forVariable(variable), INITS);
    }

    public QClub(Path<? extends Club> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClub(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClub(PathMetadata metadata, PathInits inits) {
        this(Club.class, metadata, inits);
    }

    public QClub(Class<? extends Club> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sports = inits.isInitialized("sports") ? new com.likelion.scul.common.domain.QSports(forProperty("sports")) : null;
        this.user = inits.isInitialized("user") ? new com.likelion.scul.common.domain.QUser(forProperty("user")) : null;
    }

}

