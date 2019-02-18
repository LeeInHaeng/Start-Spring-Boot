package org.zerock.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWebReply is a Querydsl query type for WebReply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebReply extends EntityPathBase<WebReply> {

    private static final long serialVersionUID = 366478188L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWebReply webReply = new QWebReply("webReply");

    public final QWebBoard board;

    public final DateTimePath<java.sql.Timestamp> regdate = createDateTime("regdate", java.sql.Timestamp.class);

    public final StringPath replyer = createString("replyer");

    public final StringPath replyText = createString("replyText");

    public final NumberPath<Long> rno = createNumber("rno", Long.class);

    public final DateTimePath<java.sql.Timestamp> updatedate = createDateTime("updatedate", java.sql.Timestamp.class);

    public QWebReply(String variable) {
        this(WebReply.class, forVariable(variable), INITS);
    }

    public QWebReply(Path<? extends WebReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWebReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWebReply(PathMetadata metadata, PathInits inits) {
        this(WebReply.class, metadata, inits);
    }

    public QWebReply(Class<? extends WebReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QWebBoard(forProperty("board")) : null;
    }

}

