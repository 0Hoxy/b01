package org.zerock.b01.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.Board;
//JpaRepository 인터페이스를 상속할 때에는 엔티티 타입과 @Id 타입을 지정해 주어야 하는 점을 제외하면 아무런 코드가 없어도 개발이 가능하다.
//확장 문제가 있을 수 있다고 생각할 수 있는데, 이 문제는 쿼리 메소드라는 기능이나 Querydsl을 이용하면 해결할 수 있다.
public interface BoardRepository extends JpaRepository<Board, Long> {
}
