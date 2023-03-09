//package com.example.asmdemo.transform.base
//
//import com.android.SdkConstants
//import com.android.build.api.transform.*
//import com.android.build.gradle.internal.pipeline.TransformManager
//import com.android.builder.utils.isValidZipEntryName
//import com.android.ide.common.internal.WaitableExecutor
//import com.android.utils.FileUtils
//import com.google.common.io.Files
//import java.io.*
//import java.util.zip.ZipEntry
//import java.util.zip.ZipInputStream
//import java.util.zip.ZipOutputStream
//
///**
// * Created by cnting on 2023/1/6
// * 处理增量和并发
// * https://juejin.cn/post/7159841721856032804
// */
//abstract class BaseTransform(private val enableLog: Boolean) : Transform() {
//
//    //线程池，可提升 80% 的执行速度
//    private var waitableExecutor: WaitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()
//
//    /**
//     * 此方法提供给上层进行字节码插桩
//     */
//    abstract fun providerFunction(): ((InputStream, OutputStream) -> Unit)?
//
//    /**
//     * 上层可重写该方法进行文件过滤
//     */
//    open fun classFilter(className: String) = className.endsWith(SdkConstants.DOT_CLASS)
//
//    /**
//     * 默认：获取输入的字节码文件
//     */
//    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
//        return TransformManager.CONTENT_CLASS
//    }
//
//    /**
//     * 默认：检索整个项目的内容
//     */
//    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
//        return TransformManager.SCOPE_FULL_PROJECT
//    }
//
//    override fun isIncremental(): Boolean {
//        return true
//    }
//
//    override fun transform(transformInvocation: TransformInvocation) {
//        super.transform(transformInvocation)
//        log("Transform start...")
//        //输入内容
//        val inputProvider = transformInvocation.inputs
//        //输出内容
//        val outputProvider = transformInvocation.outputProvider
//
//        //1.子类实现字节码插桩操作
//        val function = providerFunction()
//
//        //2.不是增量编译，删除所有旧的输出内容
//        if (!transformInvocation.isIncremental) {
//            outputProvider.deleteAll()
//        }
//
//        for (input in inputProvider) {
//            //3.Jar包处理
//            log("Transform jarInputs start")
//            for (jarInput in input.jarInputs) {
//                val inputJar = jarInput.file
//                val outputJar = outputProvider.getContentLocation(
//                    jarInput.name,
//                    jarInput.contentTypes,
//                    jarInput.scopes,
//                    Format.JAR
//                )
//                if (transformInvocation.isIncremental) {
//                    when (jarInput.status ?: Status.NOTCHANGED) {
//                        Status.NOTCHANGED -> {
//                            //Do nothing
//                        }
//                        Status.ADDED, Status.CHANGED -> {
//                            waitableExecutor.execute {
//                                doTransformJar(
//                                    inputJar,
//                                    outputJar,
//                                    function
//                                )
//                            }
//                        }
//                        Status.REMOVED -> {
//                            FileUtils.delete(outputJar)
//                        }
//                    }
//                } else {
//                    //非增量编译中处理Jar包逻辑
//                    waitableExecutor.execute { doTransformJar(inputJar, outputJar, function) }
//                }
//            }
//
//            //4.文件夹处理
//            log("Transform dirInput start")
//            for (dirInput in input.directoryInputs) {
//                val inputDir = dirInput.file
//                val outputDir = outputProvider.getContentLocation(
//                    dirInput.name,
//                    dirInput.contentTypes,
//                    dirInput.scopes,
//                    Format.DIRECTORY
//                )
//                if (transformInvocation.isIncremental) {
//                    for ((inputFile, status) in dirInput.changedFiles) {
//                        val outputFile = concatOutputFilePath(outputDir, inputFile)
//                        when (status ?: Status.NOTCHANGED) {
//                            Status.NOTCHANGED -> {
//                                //Do nothing
//                            }
//                            Status.ADDED, Status.CHANGED -> {
//                                waitableExecutor.execute {
//                                    doTransformFile(
//                                        inputFile,
//                                        outputFile,
//                                        function
//                                    )
//                                }
//                            }
//                            Status.REMOVED -> {
//                                FileUtils.delete(outputFile)
//                            }
//                        }
//                    }
//                } else {
//                    for (inputFile in FileUtils.getAllFiles(inputDir)) {
//                        waitableExecutor.execute {
//                            val outputFile = concatOutputFilePath(outputDir, inputFile)
//                            if (classFilter(inputFile.name)) {
//                                doTransformFile(inputFile, outputFile, function)
//                            } else {
//                                Files.createParentDirs(outputFile)
//                                FileUtils.copyFile(inputFile, outputFile)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        waitableExecutor.waitForTasksWithQuickFail<Any>(true)
//        log("Transform end")
//    }
//
//    private fun doTransformJar(
//        inputJar: File,
//        outputJar: File,
//        function: ((InputStream, OutputStream) -> Unit)?
//    ) {
//        Files.createParentDirs(outputJar)
//
//        val zipInputStream = ZipInputStream(FileInputStream(inputJar))
//        val zipOutputStream = ZipOutputStream(FileOutputStream(outputJar))
//        var entry = zipInputStream.nextEntry
//        while (entry != null && isValidZipEntryName(entry)) {
//            if (!entry.isDirectory) {
//                zipOutputStream.putNextEntry(ZipEntry(entry.name))
//                if (classFilter((entry.name))) {
//                    applyFunction(zipInputStream, zipOutputStream, function)
//                } else {
//                    zipInputStream.copyTo(zipOutputStream)
//                }
//            }
//            entry = zipInputStream.nextEntry
//        }
//    }
//
//    private fun doTransformFile(
//        inputFile: File,
//        outputFile: File,
//        function: ((InputStream, OutputStream) -> Unit)?
//    ) {
//        Files.createParentDirs(outputFile)
//        FileInputStream(inputFile).use { fis ->
//            FileOutputStream(outputFile).use { fos ->
//                applyFunction(fis, fos, function)
//            }
//        }
//    }
//
//    private fun applyFunction(
//        input: InputStream,
//        output: OutputStream,
//        function: ((InputStream, OutputStream) -> Unit)?
//    ) {
//        if (function != null) {
//            function.invoke(input, output)
//        } else {
//            input.copyTo(output)
//        }
//    }
//
//    /**
//     * 创建输出的文件
//     */
//    private fun concatOutputFilePath(outputDir: File, inputFile: File) =
//        File(outputDir, inputFile.name)
//
//    private fun log(logStr: String) {
//        if (enableLog) {
//            println("$name - $logStr")
//        }
//    }
//}