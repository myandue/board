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
    public String boardWritePro(Board board){
        //Board board 매개변수는 entity를 통해 param들을 통째로 가져오는 방식.
        boardService.write(board);

        return "redirect:/board/list";
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
    public String boardModifyPro(@PathVariable("id") Integer id, Board board){

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        //boardService.write(board);
        //위처럼 작성해도 새롭게 작성되는게 아니라 수정이 반영되던데..?
        return "redirect:/board/list";
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
