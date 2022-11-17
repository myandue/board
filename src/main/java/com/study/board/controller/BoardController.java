package com.study.board.controller;

import com.study.board.entity.Board;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

    @GetMapping("/board/write")
    public String boardWriteForm(){

        return "boardwrite";
    }

    @PostMapping("/board/write")
    public String boardWritePro(Board board){
        System.out.println("title = " + board.getTitle());
        System.out.println("content = " + board.getContent());

        return "";
    }
}
