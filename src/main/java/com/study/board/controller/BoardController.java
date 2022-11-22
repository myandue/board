package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm(){

        return "board-write";
    }

    @PostMapping("/board/write")
    public String boardWritePro(Board board, Model model){
        //Board board 매개변수는 entity를 통해 param들을 통째로 가져오는 방식.
        boardService.write(board);

        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl","/board/list"); //redirect할 주소 전달

        return "message"; //이러면 message라는 파일을 찾는거 아냐? 맞넼ㅋㅋㅋ 바본가
    }

    @GetMapping("/board/list")
    public String boardList(Model model){

        model.addAttribute("list",boardService.boardList());

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
    public String boardModifyPro(Model model, Board board){

        boardService.write(board);

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
