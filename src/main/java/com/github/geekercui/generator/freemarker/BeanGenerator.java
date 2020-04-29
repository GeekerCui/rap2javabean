package com.github.geekercui.generator.freemarker;

import com.alibaba.fastjson.JSON;
import com.github.geekercui.generator.bean.Interfaces;
import com.github.geekercui.generator.bean.Modules;
import com.github.geekercui.generator.bean.Properties;
import com.github.geekercui.generator.bean.RapBean;
import com.github.geekercui.generator.freemarker.classmeata.Attribute;
import com.github.geekercui.generator.freemarker.classmeata.ClassInfo;
import com.github.geekercui.generator.http.RapHttpClient;
import com.github.geekercui.generator.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BeanGenerator {

    private final String SCOPE_REQUEST = "request";
    private final String SCOPE_RESPONSE = "response";

    private final String TYPE_ARRAY = "Array";
    private final String TYPE_OBJECT = "Object";

    // 请求的属性结构map，key-->parentId,value-->properties列表
    private Map<Integer, List<Properties>> requestParentPropertiesMap = new HashMap<>();
    // 响应的属性结构map，key-->parentId,value-->properties列表
    private Map<Integer, List<Properties>> responseParentPropertiesMap = new HashMap<>();

    // 对象类型名字映射，key-->properties的对象ID ，value-->对象的名字
    private Map<Integer,String> objectNameMap = new HashMap<>();

    EntriyFreeMarker freeMarker = new EntriyFreeMarker();

    public void generator() {
        RapHttpClient rapHttpClient = new RapHttpClient();
        String ids = PropertiesUtils.get("id");
        Arrays.asList(StringUtils.split(ids,",")).forEach(
                id -> {
                    try {
                        String interfaceData = rapHttpClient.getInterfaceData(StringUtils.trim(id));
                        System.out.println(interfaceData);
                        // 转换成java对象方便处理
                        RapBean rapBean = JSON.parseObject(interfaceData, RapBean.class);

                        System.out.println(rapBean);

                        if(null != rapBean && null != rapBean.getData() && null != rapBean.getData().getModules()) {
                            // 对每个模块进行处理
                            for (Modules module: rapBean.getData().getModules()) {
                                // 判断每个模块的接口
                                if(null != module.getInterfaces()) {
                                     // 处理每个接口里的数据
                                    for (Interfaces interfaces:  module.getInterfaces()) {
                                        if(StringUtils.isEmpty(interfaces.getUrl())) {
                                            System.out.println("必须填写url");
                                            continue;
                                        }
                                        generateInterface(interfaces);

                                    }
                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );


    }

    private void generateInterface(Interfaces interfaces) {
        // 对每个接口判断properties进行聚集
        if(Objects.nonNull(interfaces.getProperties())) {
            interfaces.getProperties().forEach(properties -> {
                // 对每个属性进行生成源数据
                generateMetaDataMap(properties);
            });
        }

        // 开始对每个接口进行对象生成

        // 顶层对象是按照接口命名的
        String interfaceUrl = interfaces.getUrl();


        String englishName = interfaceUrl.substring(interfaceUrl.lastIndexOf("/")+1);


        // 生成request对象，对象名是**RequestVo
        generateVo(englishName,requestParentPropertiesMap.get(-1),SCOPE_REQUEST);

        // 生成request对象，对象名是**ResponseVo
        generateVo(englishName,responseParentPropertiesMap.get(-1),SCOPE_RESPONSE);

        //清理任务
        cleanMetaMap();
    }

    private void cleanMetaMap() {
        requestParentPropertiesMap.clear();
        responseParentPropertiesMap.clear();
        objectNameMap.clear();
    }

    private void generateMetaDataMap(Properties properties) {
        // 如果是对象类别则名字存下来，以后生成类的时候要使用的符号
        if(StringUtils.equals(TYPE_ARRAY,properties.getType()) ||
                StringUtils.equals(TYPE_OBJECT,properties.getType()) ) {
            objectNameMap.put(properties.getId(),properties.getName());
        }

        Integer parentId = properties.getParentId();
        // 判断如果是请求类放到请求的对象池中
        if(properties.getScope().equalsIgnoreCase(SCOPE_REQUEST)) {

            List<Properties> requestPropertiesList = requestParentPropertiesMap.get(parentId);
            if(Objects.isNull(requestPropertiesList)) {
                requestPropertiesList = new ArrayList<>();
                requestParentPropertiesMap.put(parentId,requestPropertiesList);
            }

            requestPropertiesList.add(properties);
        } else if(StringUtils.equals(SCOPE_RESPONSE, properties.getScope())) {
            List<Properties> responsePropertiesList = responseParentPropertiesMap.get(parentId);
            if(Objects.isNull(responsePropertiesList)) {
                responsePropertiesList = new ArrayList<>();
                responseParentPropertiesMap.put(parentId,responsePropertiesList);
            }

            responsePropertiesList.add(properties);
        }
    }

    private String toUpperCaseFirst(String str,String suffix) {
        return str.substring(0, 1).toUpperCase().concat(str.substring(1)).concat(suffix);
    }


    private void generateVo(String clzName, List<Properties> propertiesList, String scope) {

        if(Objects.isNull(propertiesList) || StringUtils.isEmpty(clzName) ) {
            return;
        }

        String suffix = "RequestVo";
        if(StringUtils.equals(SCOPE_RESPONSE,scope)) {
            suffix = "ResponseVo";
        }

        clzName = toUpperCaseFirst(clzName , suffix);

        ClassInfo classInfo = new ClassInfo();
        List<String> imports = classInfo.getImports();

        List<String> annotations = null;
        List<Attribute> attributes = new ArrayList<>();

        classInfo.setPackageName(PropertiesUtils.get("package"));
        classInfo.setClassName(clzName);

        boolean hasList = false;
        boolean hasValid = false;

        for (Properties properties: propertiesList) {
            Attribute attribute = new Attribute();
            attribute.setName(properties.getName());
            attribute.setDesc(properties.getDescription());

            String type = properties.getType();
            String value = properties.getValue();

            // 处理number
            if (StringUtils.equals("Number", type)) {
                type = "Integer";

                if (StringUtils.equals("Double", value)) {
                    type = "Double";
                }
                attribute.setType(type);
            }
            // 处理array类型
            else if (StringUtils.equals("Boolean",type) || StringUtils.equals("String",type) ) {
                attribute.setType(type);

            } else if (StringUtils.equals("Array", type)) {
                if (StringUtils.equals("String", value)) {
                    attribute.setType("List<String>");
                } else if (StringUtils.equals("Integer", value)) {
                    attribute.setType("List<Integer>");
                } else if (StringUtils.equals("Double", value)) {
                    attribute.setType("List<Double>");
                } else if (StringUtils.equals("Boolean", value)) {
                    attribute.setType("List<Boolean>");
                } else {
                    // List<对象>
                    String name = properties.getName();
                    name = toUpperCaseFirst(name , suffix);
                    attribute.setType("List<" + name + ">");


                    // 递归生成对象的类
                    if(StringUtils.equals(SCOPE_RESPONSE,scope)) {
                        generateVo(properties.getName(),this.responseParentPropertiesMap.get(properties.getId()),scope);
                    } else {
                        generateVo(properties.getName(),this.requestParentPropertiesMap.get(properties.getId()),scope);
                    }

                }
                hasList = true;


            }
            else if (StringUtils.equals("Object", type)) { //object
                String name = properties.getName();
                name = toUpperCaseFirst(name , suffix);
                attribute.setType(name);

                // 递归生成对象的类
                if(StringUtils.equals(SCOPE_RESPONSE,scope)) {
                    generateVo(properties.getName(),this.responseParentPropertiesMap.get(properties.getId()),scope);
                } else {
                    generateVo(properties.getName(),this.requestParentPropertiesMap.get(properties.getId()),scope);
                }

            }   else {
                // 这里应该是不支持的类型
                // 暂时兼容一下，进行剔除这些字段
              //  attribute.setType(type);
                continue;
            }

            annotations = new ArrayList<>();
            if(Objects.nonNull(properties.getRequired()) && properties.getRequired()) {
                // 可以放到模板中
                String notNull = "@NotNull(message = \"" + properties.getDescription() + "不能为空\")";
                annotations.add(notNull);
                hasValid = true;
            }
            attribute.setAnnotations(annotations);
            attributes.add(attribute);
        }

        if(hasValid) {
            imports.add("import javax.validation.constraints.*");
        }

        if(hasList) {
            imports.add("import java.util.List");
        }
        classInfo.setAttributes(attributes);

        freeMarker.process(classInfo);


   }


}
