package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                     BindingResult bindingResult) throws BindException {
        log.info("Registering new reply: " + replyDTO);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Map<String,Long> map = new HashMap<>();

        Long rno = replyService.register(replyDTO);
        map.put("rno", rno);

        return map;
    }

    //get 으로 게시물의 댓글 목록가져옴
    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ReplyDTO> responseDTO =
                replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }

    //특정 댓글조회
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {
        ReplyDTO replyDTO = replyService.read(rno);
        return replyDTO;
    }

    //댓글 삭제하기 (rno)
    @DeleteMapping("/{rno}")
    public Map<String,Long> delete(@PathVariable("rno") Long rno) {
        replyService.remove(rno);
        Map<String,Long> map = new HashMap<>();
        map.put("rno", rno);
        return map;
    }

    //댓글을 수정하기
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno,
                                    @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String,Long> map = new HashMap<>();
        map.put("rno", rno);
        return map;
    }
}