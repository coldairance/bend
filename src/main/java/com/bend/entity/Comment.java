package com.bend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {
    @TableId(value = "cid", type = IdType.AUTO)
    private Integer cid;
    private String name;
    private String comment;
    private String answer;
    private String aid;
    private LocalDateTime time;
}
