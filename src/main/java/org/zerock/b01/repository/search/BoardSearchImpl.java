package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board); //select...from board
        BooleanBuilder booleanBuilder  = new BooleanBuilder();// (
        booleanBuilder.or(board.title.contains("11")); // title like
        booleanBuilder.or(board.content.contains("11")); // content like
        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));
//        query.where(board.title.contains("1")); // where title like
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();
        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ((types != null) && (types.length > 0) && keyword != null) { // (types == keyword) is true

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
                    query.where(booleanBuilder);
        }// end if
                //bno > 0
                query.where(board.bno.gt(0L));
                //paging
                this.getQuerydsl().applyPagination(pageable, query);
                List<Board> list = query.fetch();
                long count = query.fetchCount();
                // 페이징 처리의 최종 결과는 Page<T> 타입을 반환하는 것이므로 Querydsl에서는 이를 직접 처리해야 하는 불편함이 있습니다.
                // Spring Data JPA에서는 이를처리를 위해서 PageImpl이라는 클래스를 제공해서 3개의 파라미터로 Page<T>를 생성할 수 있습니다.
                return new PageImpl<Board>(list, pageable, count);

    }
}
