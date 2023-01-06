package com.cnting.xmlviewscanplugin

import com.android.build.api.variant.Variant
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Stream

/**
 * 扫描merger.xml
 */
class XmlViewScanTask extends DefaultTask {
    private Set<String> xmlScanViewSet = new HashSet<>()

    //当前变体，如果属性没有使用private修饰，也需要使用@Inject注解标识
    //BaseVariant已被废弃，看https://stackoverflow.com/questions/71082663/applicationvariant-basevariantoutput-are-deprecated
    private Variant variant

    //构造方法必须使用@Inject注解标识
    @Inject
    XmlViewScanTask(Variant variant) {
        this.variant = variant
    }

    //使用@TaskAction注解标识，这个方法会在Gradle的执行阶段去执行
    @TaskAction
    void performXmlScanTask() {
        println 'performXmlScanTask start...'
        //创建需要输出View的文件路径
        File outputFile = new File(project.buildDir.path + "/${variant.name}_xml_scan_view/xml_scan_view.txt")
        if (!outputFile.parentFile.exists()) {
            outputFile.parentFile.mkdirs()
        }
        if (outputFile.exists()) {
            outputFile.delete()
        }
        outputFile.createNewFile()
        println 'file create success...'
        xmlScanViewSet.clear()

        //获取merger.xml文件
        Task mergeResourcesTask = variant.mergeResources
        String mergerPath = "${project.buildDir.path}/intermediates/incremental/${mergeResourcesTask.name}/merger.xml"
        File mergeFile = new File(mergerPath)

        //开始解析 merger.xml
        XmlSlurper xmlSlurper = new XmlSlurper()
        GPathResult result = xmlSlurper.parse(mergeFile)
        if (result.children()) {
            result.childNodes().forEachRemaining(new Consumer() {
                @Override
                void accept(Object o) {
                    parseNode(o)
                }
            })
        }
        println 'merger.xml parsing success...'

        //过滤黑名单中的View
        Stream<String> viewNameStream
        if (project.ignore.isEnable) {
            println 'blacklist enable...'
            viewNameStream = filterBlackList()

            if (viewNameStream == null) {
                viewNameStream = xmlScanViewSet.stream()
            }
        } else {
            println 'blacklist disable...'
            viewNameStream = xmlScanViewSet.stream()
        }

        //将viewName写入文件中
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile))
        viewNameStream.forEach(new Consumer<String>() {
            @Override
            void accept(String viewName) {
                printWriter.println(viewName)
            }
        })
        printWriter.flush()
        printWriter.close()
        println 'write all viewName to file success...'
    }

    /**
     * 过滤黑名单中的View
     */
    private Stream<String> filterBlackList() {
        List<String> ignoreViewList = project.ignore.ignoreViewList
        Stream<String> viewNameStream = null
        if (ignoreViewList) {
            println "ignoreViewList:$ignoreViewList"
            viewNameStream = xmlScanViewSet.stream().filter(new Predicate<String>() {
                @Override
                boolean test(String viewName) {
                    for (String name : ignoreViewList) {
                        if (viewName == name) return false
                    }
                    return true
                }
            })
        } else {
            println 'ignoreViewList is null,no filter...'
        }
        return viewNameStream
    }
    /**
     * 递归解析 merger.xml 中的 Node 节点
     * merger.xml文件中的布局文件标签如下：
     * <file name="activity_main"
     *       path="/Users/zhouying/learning/GradleDemo/app/src/main/res/layout/activity_main.xml"
     *       qualifiers=""
     *       type="layout"/>
     */
    private void parseNode(Object obj) {
        if (obj instanceof Node) {
            Node node = obj
            if (node) {
                if ("file" == node.name() && "layout" == node.attributes().get("type")) {
                    //获取布局文件
                    String layoutPath = node.attributes().get("path")
                    File layoutFile = new File(layoutPath)

                    //开始解析布局文件
                    XmlSlurper xmlSlurper = new XmlSlurper()
                    GPathResult result = xmlSlurper.parse(layoutFile)
                    String viewName = result.name()
                    xmlScanViewSet.add(viewName)

                    if (result.children()) {
                        result.childNodes().forEachRemaining(new Consumer() {
                            @Override
                            void accept(Object o) {
                                //递归解析子节点
                                parseLayoutNode(o)
                            }
                        })
                    }
                } else {
                    //如果不是布局文件，递归调用
                    node.childNodes().forEachRemaining(new Consumer() {
                        @Override
                        void accept(Object o) {
                            parseNode(o)
                        }
                    })
                }
            }
        }
    }

    /**
     * 递归解析layout布局子节点
     * @param obj
     */
    private void parseLayoutNode(Object obj) {
        if (obj instanceof Node) {
            Node node = obj
            if (node) {
                xmlScanViewSet.add(node.name())
                node.childNodes().findAll {
                    parseLayoutNode(it)
                }
            }
        }
    }
}
