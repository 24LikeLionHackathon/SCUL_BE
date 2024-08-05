package com.likelion.scul.auth.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGoogleRefreshToken is a Querydsl query type for GoogleRefreshToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoogleRefreshToken extends EntityPathBase<GoogleRefreshToken> {

    private static final long serialVersionUID = 102749812L;

    public static final QGoogleRefreshToken googleRefreshToken1 = new QGoogleRefreshToken("googleRefreshToken1");

    public final StringPath googleRefreshToken = createString("googleRefreshToken");

    public final NumberPath<Long> googleRefreshTokenId = createNumber("googleRefreshTokenId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QGoogleRefreshToken(String variable) {
        super(GoogleRefreshToken.class, forVariable(variable));
    }

    public QGoogleRefreshToken(Path<? extends GoogleRefreshToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGoogleRefreshToken(PathMetadata metadata) {
        super(GoogleRefreshToken.class, metadata);
    }

}

