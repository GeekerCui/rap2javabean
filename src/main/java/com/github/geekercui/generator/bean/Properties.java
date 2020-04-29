package com.github.geekercui.generator.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Properties {

    private Integer id;
    private String scope;
    private String type;
    private Integer pos;
    private String name;
    private String rule;
    private String value;
    private String description;
    private Integer parentId;
    private Long priority;
    private Integer interfaceId;
    private Long creatorId;
    private Integer moduleId;
    private Integer repositoryId;
    private Boolean required;
    private Date createdAt;
    private Date updatedAt;
    private String deletedAt;

}