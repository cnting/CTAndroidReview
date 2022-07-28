package com.example.ioc_processor;

import com.example.ioc_annotation.BindView;
import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.Map;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

//帮助我们生成META-INF信息
@AutoService(Processor.class)
//要处理的注解类型
@SupportedAnnotationTypes("com.example.ioc_annotation.BindView")
public class BindViewProcessor extends AbstractProcessor {

    //基于元素进行操作的工具方法
    private Elements elementUtils;
    //代码创建者
    private Filer filerCreator;
    //信息传递者，用来报告错误、警告等信息
    private Messager messager;
    private final Map<String, ProxyInfo> proxyInfoMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filerCreator = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    /**
     * 主要做两件事：
     * 1. 收集信息
     * （1）注解修饰变量所在的类名，便于和后缀拼接生成代理类
     * （2）类的完整包名
     * （3）类中被注解修饰的字段，以及对应的布局 id
     * 2. 生成代码
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE,"===>start process");
        //避免生成重复的代理类
        proxyInfoMap.clear();
        //拿到BindView修饰的元素集合，里面可以包含包、类、方法或元素
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        //1. 收集信息
        for (Element element : elements) {
            //因为BindView修饰的是变量，所以可以直接转成VariableElement
            VariableElement variableElement = (VariableElement) element;
            //拿到变量所在类的对象信息
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //得到类的完整名称
            String qualifiedName = typeElement.getQualifiedName().toString();
            //一个类对应一个 ProxyInfo
            ProxyInfo proxyInfo = proxyInfoMap.get(qualifiedName);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo(elementUtils, typeElement);
                proxyInfoMap.put(qualifiedName, proxyInfo);
            }
            BindView annotation = variableElement.getAnnotation(BindView.class);
            if (annotation != null) {
                int id = annotation.value();
                proxyInfo.injectElements.put(id, variableElement);
            }
        }
        //2.生成代理类
        for (String key : proxyInfoMap.keySet()) {
            ProxyInfo proxyInfo = proxyInfoMap.get(key);
                proxyInfo.generateJavaCode(filerCreator);
        }
        return true;
    }
}