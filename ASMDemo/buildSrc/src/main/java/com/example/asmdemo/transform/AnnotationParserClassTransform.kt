package com.example.asmdemo.transform

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.example.asmdemo.AsmItem
import com.example.asmdemo.transform.base.ClassNodeAdapter
import org.gradle.api.logging.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.tree.ClassNode

/**
 * Created by cnting on 2022/7/28
 * 注解解析
 */
abstract class AnnotationParserClassTransform :
    AsmClassVisitorFactory<InstrumentationParameters.None> {

    companion object {
        const val AsmFieldDesc = "Lcom/example/asm_annotation/AsmMethodReplace;"
        var asmConfigs = mutableListOf<AsmItem>()
        var asmConfigsMap = HashMap<String, String>()
    }

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val classNode = ClassNode(Opcodes.ASM7)
        classNode.accept(nextClassVisitor)
        classNode.methods.forEach { method ->
            method.invisibleAnnotations?.forEach { node ->
                if (node.desc == AsmFieldDesc) {
                    val asmItem = AsmItem(classNode.name, method, node)
                    println("===>$asmItem")
                    if (!asmConfigs.contains(asmItem)) {
                        asmConfigs.add(asmItem)
                        asmConfigsMap.put(
                            classNode.name,
                            classNode.name
                        )
                    }
                }
            }
        }
        return classNode
    }

    /**
     * 控制我们自定义的Visitor是否处理这个类
     */
    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }
}


class AnnotationParserClassAdapter(
    classVisitor: ClassVisitor,
    private val logger: Logger
) : ClassNodeAdapter(Opcodes.ASM7, classVisitor) {

    override fun transform(classNode: ClassNode) {
        classNode.methods.forEach { method ->
            method.invisibleAnnotations?.forEach { node ->
                if (node.desc == AnnotationParserClassTransform.AsmFieldDesc) {
                    val asmItem = AsmItem(classNode.name, method, node)
                    logger.error("===>$asmItem")
                    if (!AnnotationParserClassTransform.asmConfigs.contains(asmItem)) {
                        AnnotationParserClassTransform.asmConfigs.add(asmItem)
                        AnnotationParserClassTransform.asmConfigsMap.put(
                            classNode.name,
                            classNode.name
                        )
                    }
                }
            }
        }
    }

}

