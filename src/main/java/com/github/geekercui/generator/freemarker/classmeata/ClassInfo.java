package com.github.geekercui.generator.freemarker.classmeata;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
public class ClassInfo {
    private String packageName;

    private String className;
    private List<String> imports = new ArrayList<>();
    private List<Attribute> attributes;
    private String lastPackage;

}
