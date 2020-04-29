package com.github.geekercui.generator.freemarker;

import com.github.geekercui.generator.freemarker.classmeata.ClassInfo;
import com.github.geekercui.generator.util.PropertiesUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EntriyFreeMarker {
    private static String BASEPAKAGE_PATH;
    private Configuration configuration;

    public EntriyFreeMarker() {
        configuration = new Configuration(Configuration.getVersion());
        configuration.setTemplateLoader(new ClassTemplateLoader(EntriyFreeMarker.class.getClassLoader(), "templates/"));
    }

    public void process(ClassInfo classInfo) {
        Writer out = null;
        try {
            Template template = configuration.getTemplate("clazz.ftl");
            File folder = new File(BASEPAKAGE_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File docFile = new File(BASEPAKAGE_PATH + classInfo.getClassName() + ".java");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            Map<String, Object> root = new HashMap<>();
            root.put("classInfo", classInfo);
            template.process(root, out);
            System.out.println(BASEPAKAGE_PATH + classInfo.getClassName() + ".java");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    static {
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String srcPatch = StringUtils.substringBefore(classPath, "/target") + "/src/main/java/";
        String relativePkgPath = StringUtils.replace(PropertiesUtils.get("package"), ".", "/") + "/";
        BASEPAKAGE_PATH = srcPatch + relativePkgPath;
    }
}
