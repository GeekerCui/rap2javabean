package com.github.geekercui.generator.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Modules {

    private Integer id;
    private String name;
    private String description;
    private Long priority;
    private Long creatorId;
    private Integer repositoryId;
    private Date createdAt;
    private Date updatedAt;
    private String deletedAt;
    private List<Interfaces> interfaces;

}