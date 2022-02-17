package com.bend.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
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
public class Article {

    @TableId(value = "aid", type = IdType.INPUT)
    private String aid;

    private String first;
    private String second;
    private String path;
    private Integer belong;
    private Integer type;
    private Integer state = 0;
    private LocalDateTime updateTime = LocalDateTimeUtil.now();
}
