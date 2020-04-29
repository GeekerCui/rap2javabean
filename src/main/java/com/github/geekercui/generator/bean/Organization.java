/**
  * Copyright 2020 bejson.com 
  */
package com.github.geekercui.generator.bean;
import lombok.Data;

import java.util.Date;

@Data
public class Organization {

    private Integer id;
    private String name;
    private String description;
    private String logo;
    private Boolean visibility;
    private Long creatorId;
    private Long ownerId;
    private Date createdAt;
    private Date updatedAt;
    private String deletedAt;

}