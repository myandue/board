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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Arrays;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/write")
    public String getArticleWrite(){

        return "article-write";
    }

    @PostMapping("/write")
    public String postArticleWrite(Board board, Model model, MultipartFile file) throws Exception{
        //Board board 매개변수는 entity를 통해 param들을 통째로 가져오는 방식.
        //MultipartFile file은 뭐지,, -> input 중 name="file" 인 애를 받아오는 것
        boardService.write(board, file);
        model.addAttribute("message","글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl",""+board.getId()); //redirect할 주소 전달

        return "message"; //이러면 message라는 파일을 찾는거 아냐? 맞넼ㅋㅋㅋ 바본가
    }

    @GetMapping("/list")
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

    @GetMapping("/{id}") //localhost:8080/board/1
    public String articleDetail(Model model, @PathVariable("id") Integer id){
        model.addAttribute("article", boardService.boardView(id));

        return "article-detail";
    }

    @GetMapping("/{id}/modify") //localhost:8080/board/1/modify
    public String getArticleModify(Model model, @PathVariable("id") Integer id){

        model.addAttribute("article", boardService.boardView(id));

        return "article-modify";
    }

    @PostMapping("/{id}/modify")
    public String postArticleModify(Model model, Board board, MultipartFile file) throws Exception{

        boardService.write(board, file);

        model.addAttribute("message","글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl","");
        return "message";
        //write 이랑 modify 상세페이지로 redirect 하고 싶음.
    }

    @GetMapping("/delete") //localhost:8080/board/delete?id=1
    public String articleDelete(@RequestParam("id") Integer id){
    //RequestParam 안의 텍스트와 html에 적힌 텍스트를 매칭시켜 바인딩 된다.
    //해당 텍스트와 매개변수명이 같다면 어노테이션을 생략할 수 있다.

        boardService.delete(id);

        return "redirect:/board/list";
    }
}
