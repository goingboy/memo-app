package com.memo.dto;

import lombok.Data;

@Data
public class MemoDTO {

    private String title;
    private String content;
    private Long groupId;
}
