package com.example.asmdemo.transform

import com.example.asmdemo.AsmItem
import com.example.asmdemo.transform.base.ClassNodeAdapter
import com.quinn.hunter.transform.HunterTransform
import com.quinn.hunter.transform.asm.BaseWeaver
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

/**
 * Created by cnting on 2022/7/28
 * 注解解析
 */
class AnnotationParserClassTransform(project: Project) : HunterTransform(project) {

    companion object {
        const val AsmFieldDesc = "Lcom/example/asm_annotation/AsmMethodReplace;"
        var asmConfigs = mutableListOf<AsmItem>()
        var asmConfigsMap = HashMap<String, String>()
    }

    init {
        this.bytecodeWeaver = AnnotationParserClassWeaver(project.logger)
    }
}

class AnnotationParserClassWeaver(val logger: Logger) : BaseWeaver() {
    override fun wrapClassWriter(classWriter: ClassWriter): ClassVisitor {
        return AnnotationParserClassAdapter(classWriter, logger)
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

