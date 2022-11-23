package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    //게시글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir");
        //현재 프로젝트 경로
        String filePath = projectPath + "/src/main/webapp/";
        //파일 저장할 경로 지정

        UUID uuid = UUID.randomUUID();
        //식별자: 파일에 붙일 랜덤 이름 생성
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(filePath, fileName);
        file.transferTo(saveFile);
        //실제로 들어온 파일을 위 지정해둔 경로, 이름으로 변경해줌

        board.setFilename(fileName);
        board.setFilepath("/webapp/"+fileName);

        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
        //findAll return 타입: 매개변수x -> List, pageable 매개변수 -> Page
    }

    //검색된 게시글 리스트 처리
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageble){
        return boardRepository.findByTitleContaining(searchKeyword, pageble);
    }

    //특정 게시글 불러오기
    public Board boardView(Integer id){
        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제 처리
    public void delete(Integer id){
        boardRepository.deleteById(id);
    }
}
