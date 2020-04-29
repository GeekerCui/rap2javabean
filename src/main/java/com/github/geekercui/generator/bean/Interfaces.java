package com.github.geekercui.generator.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Interfaces {

    private Integer id;
    private String name;
    private String url;
    private String method;
    private String description;
    private Long priority;
    private Integer status;
    private Long creatorId;
    private String lockerId;
    private Integer moduleId;
    private Integer repositoryId;
    private Date createdAt;
    private Date updatedAt;
    private String deletedAt;
    private String locker;
    private List<Properties> properties;

}