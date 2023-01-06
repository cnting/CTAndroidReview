package com.cnting.apt_processor;

import com.cnting.apt_annotation.ViewCreator;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by cnting on 2023/1/6
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.cnting.apt_annotation.ViewCreator")
//RELEASE_11在jdk11中存在，可以运行的
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class MyViewCreatorProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<String> viewNameSet = readViewNameFromFile();
        if (viewNameSet == null || viewNameSet.isEmpty()) {
            return false;
        }
        //获取使用了注解的元素
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(ViewCreator.class);
        for (Element element : elementsAnnotatedWith) {
            startGenerateCode(viewNameSet);
            break;
        }
        return true;
    }

    /**
     * <code>
     * public class MyViewCreatorImpl implements IMyViewCreator {
     *
     * @Override public View createView(String name, Context context, AttributeSet attr) {
     * View view = null;
     * switch(name) {
     * case "androidx.core.widget.NestedScrollView":
     * view = new NestedScrollView(context,attr);
     * break;
     * case "androidx.constraintlayout.widget.ConstraintLayout":
     * view = new ConstraintLayout(context,attr);
     * break;
     * case "androidx.appcompat.widget.ButtonBarLayout":
     * view = new ButtonBarLayout(context,attr);
     * break;
     * //...
     * default:
     * break;
     * }
     * return view;
     * }
     * <p>
     * <code/>
     */
    private void startGenerateCode(Set<String> viewNameSet) {
        System.out.println("===> startGenerateCode");
        ClassName viewType = ClassName.get("android.view", "View");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("createView")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(viewType)
                .addParameter(String.class, "name")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(ClassName.get("android.util", "AttributeSet"), "attr");
        methodBuilder.addStatement("$T view = null", viewType);
        methodBuilder.beginControlFlow("switch(name)");
        for (String viewName : viewNameSet) {
            if (viewName.contains(".")) {
                //分离包名和控件名，如：androidx.constraintlayout.widget.ConstraintLayout
                String packageName = viewName.substring(0, viewName.lastIndexOf("."));
                String simpleViewName = viewName.substring(viewName.lastIndexOf(".") + 1);
                ClassName returnType = ClassName.get(packageName, simpleViewName);

                methodBuilder.addCode("case $S:\n", viewName);
                methodBuilder.addStatement("\tview = new $T(context,attr)", returnType);
                methodBuilder.addStatement("\tbreak");
            }
        }
        methodBuilder.addCode("default:\n");
        methodBuilder.addStatement("\tbreak");
        methodBuilder.endControlFlow();
        methodBuilder.addStatement("return view");

        MethodSpec createView = methodBuilder.build();

        TypeSpec myViewCreatorImpl = TypeSpec.classBuilder("MyViewCreatorImpl")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get("com.cnting.apt_api", "IMyViewCreator"))
                .addMethod(createView)
                .build();
        JavaFile javaFile = JavaFile.builder("com.cnting.gradledemo", myViewCreatorImpl).build();
        try {
            javaFile.writeTo(filer);
            System.out.println("===> 生成成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("===> 生成失败");
        }
    }

    private Set<String> readViewNameFromFile() {
        try {
            File file = new File("/Users/cnting/Documents/workspace_android/CTAndroidReview/GradleDemo/all_view_name.txt");
            Properties config = new Properties();
            config.load(new FileInputStream(file));
            return config.stringPropertyNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
