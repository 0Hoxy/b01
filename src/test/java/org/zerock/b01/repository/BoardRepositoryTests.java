package org.zerock.b01.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListAllDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    //@Test
    public void testInsert() {
        for (int i = 1; i <= 100; i++) {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user"+(i%10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        }
    }

    //@Test
    public void testSelect(){
        Long bno = 1L;
        //Optional 타입은 없을 경우 null 값 에러 방지를 위함
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow(); //결과가 있을 경우에 가져오고 없으면 예외발생

        log.info(board.toString());
    }

    //update 기능 테스트
    //@Test
    public void testUpdate(){
        Long bno = 100L;
        //Optional 타입은 없을 경우 null 값 에러 방지를 위함
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update..title 100", "update..content 100");
        boardRepository.save(board);//update 도 save 메서드로 사용함
        // 1. 새로운 객체 입력시 새로 등록됨 (Create)
        // 2. 같은 객체 입력시 수정된 내용이 업데이트 됨 (기준은 id 기준)
    }

    //@Test
    public void testDelete(){
        long bno = 1L;
        boardRepository.deleteById(bno);
    }

    //게시판 페이징 테스트
    //@Test
    public void testPaging(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        //페이지 객체를 findAll()에 입력하여 원하는 페이지의 데이터만 가져옴
        Page<Board> result = boardRepository.findAll(pageable);
        //페이지 리스트는 보드리스트와 여러 정보들이 담겨있음
        log.info("총 갯수: " +result.getTotalElements());
        log.info("총 페이지수: " + result.getTotalPages());
        log.info("페이지 번호: " + result.getNumber());
        log.info("페이지 사이즈: " + result.getSize());
        // 보드 리스트 데이터
        List<Board> boards = result.getContent();
        for (Board board : boards) {
            log.info(board.toString());
        }
    }

    @Test
    public void testSearch1() {
        //2 page order by bno desc
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }

    @Test
    public void testSearchAll2() {
        String[] types = {"t", "c", "w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        //total pages
        log.info(result.getTotalPages());
        //page size
        log.info(result.getSize());
        //PageNumber
        log.info(result.getNumber());
        //Prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }
        boardRepository.save(board);
    }

    @Test
    public void testReadWithImages() {
        //반드시 존재하는 bno로 확인
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("----------------------------");
        for (BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }

    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages() {

        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        Board board = result.orElseThrow();

        //기존의 첨부파일들은 삭제
        board.clearImages();

        //새로운 첨부파일들
        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }
        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {

        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll() {
        for (int i = 0; i <= 100; i++) {
            Board board = Board.builder()
                    .title("Title" + i)
                    .content("Content" + i).
                    writer("writer" + i)
                    .build();

            for (int j = 0; j < 3; j++) {
                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

//        boardRepository.searchWithAll(null, null, pageable);

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("------------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));
    }
}
