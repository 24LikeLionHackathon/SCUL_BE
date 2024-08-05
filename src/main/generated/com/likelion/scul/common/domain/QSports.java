package com.likelion.scul.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSports is a Querydsl query type for Sports
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSports extends EntityPathBase<Sports> {

    private static final long serialVersionUID = -581106305L;

    public static final QSports sports = new QSports("sports");

    public final ListPath<com.likelion.scul.board.domain.Board, com.likelion.scul.board.domain.QBoard> boards = this.<com.likelion.scul.board.domain.Board, com.likelion.scul.board.domain.QBoard>createList("boards", com.likelion.scul.board.domain.Board.class, com.likelion.scul.board.domain.QBoard.class, PathInits.DIRECT2);

    public final NumberPath<Long> sportsId = createNumber("sportsId", Long.class);

    public final StringPath sportsName = createString("sportsName");

    public final ListPath<UserSports, QUserSports> userSports = this.<UserSports, QUserSports>createList("userSports", UserSports.class, QUserSports.class, PathInits.DIRECT2);

    public QSports(String variable) {
        super(Sports.class, forVariable(variable));
    }

    public QSports(Path<? extends Sports> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSports(PathMetadata metadata) {
        super(Sports.class, metadata);
    }

}

