package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //jpa가 관리하는 object에 붙여줌
@Data //롬복 -> 외부에서 getter setter 로 접근할 수 있게하는듯
public class Board {

    @Id //pk 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY -> mysql, mariadb / SEQUENCE -> Oracle
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;
}
