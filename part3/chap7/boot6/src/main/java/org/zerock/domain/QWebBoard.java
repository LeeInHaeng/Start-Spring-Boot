package org.zerock.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWebBoard is a Querydsl query type for WebBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWebBoard extends EntityPathBase<WebBoard> {

    private static final long serialVersionUID = 351985512L;

    public static final QWebBoard webBoard = new QWebBoard("webBoard");

    public final NumberPath<Long> bno = createNumber("bno", Long.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> regdate = createDateTime("regdate", java.sql.Timestamp.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.sql.Timestamp> updatedate = createDateTime("updatedate", java.sql.Timestamp.class);

    public final StringPath writer = createString("writer");

    public QWebBoard(String variable) {
        super(WebBoard.class, forVariable(variable));
    }

    public QWebBoard(Path<? extends WebBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWebBoard(PathMetadata metadata) {
        super(WebBoard.class, metadata);
    }

}

