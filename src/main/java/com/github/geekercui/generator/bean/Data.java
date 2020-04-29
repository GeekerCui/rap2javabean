package com.github.geekercui.generator.bean;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class Data {

    private Integer id;
    private String name;
    private String description;
    private String logo;
    private String token;
    private Boolean visibility;
    private Long ownerId;
    private Integer organizationId;
    private Long creatorId;
    private String lockerId;
    private Date createdAt;
    private Date updatedAt;
    private String deletedAt;
    private Creator creator;
    private Owner owner;
    private String locker;
    private List<String> members;
    private Organization organization;
    private List<String> collaborators;
    private List<Modules> modules;
    private Boolean canUserEdit;

}