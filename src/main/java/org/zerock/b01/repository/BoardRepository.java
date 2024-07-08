package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

//JpaRepository 상속후 <테이블클래스명, ID 타입>
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    //CRUD 자동 생성
    //커스텀 메소드 만들기
    //Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
    //@Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    //Page<Board> findKeyword(String keyword, Pageable pageable);
}
