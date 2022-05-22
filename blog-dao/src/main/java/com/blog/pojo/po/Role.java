package com.blog.pojo.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Role {

    private static final long serialVersionUID = 1L;

    private Integer roleID;
    private String roleName;
    private Integer roleCode;
}
