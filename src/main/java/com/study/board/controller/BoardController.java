package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm(){

        return "board-write";
    }

    @PostMapping("/board/write")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{
        //Board board 매개변수는 entity를 통해 param들을 통째로 가져오는 방식.
        //MultipartFile file은 뭐지,, -> input 중 name="file" 인 애를 받아오는 것
        boardService.write(board, file);
        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","view/"+board.getId()); //redirect할 주소 전달

        return "message"; //이러면 message라는 파일을 찾는거 아냐? 맞넼ㅋㅋㅋ 바본가
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page=0, size=20, sort="id", direction= Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){

        Page<Board> list = null;

        if(searchKeyword==null){
            list = boardService.boardList(pageable);
        }else{
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage+5,list.getTotalPages());

        //page 변수들 배열같은데에 넣어서 한데 모아 보내고싶음

        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "board-list";
    }

    @GetMapping("/board/view/{id}") //localhost:8080/board/view/1
    public String boardView(Model model, @PathVariable("id") Integer id){
        model.addAttribute("article", boardService.boardView(id));

        return "board-view";
    }

    @GetMapping("/board/modify/{id}") //localhost:8080/board/modify/1
    public String boardModify(Model model, @PathVariable("id") Integer id){

        model.addAttribute("article", boardService.boardView(id));

        return "board-modify";
    }

    @PostMapping("/board/modify/{id}")
    public String boardModifyPro(Model model, Board board, MultipartFile file) throws Exception{

        boardService.write(board, file);

        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list");
        return "message";
        //write 이랑 modify 상세페이지로 redirect 하고 싶음.
    }

    @GetMapping("/board/delete") //localhost:8080/board/delete?id=1
    public String boardDelete(@RequestParam("id") Integer id){
    //RequestParam 안의 텍스트와 html에 적힌 텍스트를 매칭시켜 바인딩 된다.
    //해당 텍스트와 매개변수명이 같다면 어노테이션을 생략할 수 있다.

        boardService.delete(id);

        return "redirect:/board/list";
    }
}
