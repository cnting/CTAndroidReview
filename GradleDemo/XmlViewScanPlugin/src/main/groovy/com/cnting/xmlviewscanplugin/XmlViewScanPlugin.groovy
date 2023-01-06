package com.cnting.xmlviewscanplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * 扫描Xml Plugin
 *
 * mergeXXXResources 这个Task会对所有的资源进行合并，并将合并文件输出到：
 * /build/intermediates/incremental/mergeReleaseResources/merger.xml
 *
 * 我们要做的是 将自定义Task接到 mergeXXXResources后面，然后遍历merger.xml这个文件，把它里面所有的xml中的View输出到一个.txt文件中
 */
class XmlViewScanPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println 'Hello XmlViewScanPlugin'
        //添加黑名单扩展配置
        project.extensions.create('ignore', IgnoreViewExtension)

        project.afterEvaluate {
            //是否是Android插件
            def isAppPlugin = project.plugins.hasPlugin('com.android.application')
            //获取变体
            def variants
            if (isAppPlugin) {
                variants = project.android.applicationVariants
            } else {
                variants = project.android.libraryVariants
            }

            variants.each { variant ->
                //通过变体获取对应的 mergeXXXResources 这个Task
                Task mergeResourcesTask = variant.mergeResources

                def prefix = variant.name
                //这里遇到问题，拿到的variant类型不一致，可能需要按 https://juejin.cn/post/7111118062262321189 这种方式改，
                // 但还拿不到 merged_res 的中间产物

                //创建自定义的Task
                Task xmlViewScanTask = project.tasks.create("${prefix}XmlViewScanTask", XmlViewScanTask, variant)
                //将我们自定义的 Task 放到 mergeResourcesTask 后面
                mergeResourcesTask.finalizedBy(xmlViewScanTask)
            }
        }
    }
}