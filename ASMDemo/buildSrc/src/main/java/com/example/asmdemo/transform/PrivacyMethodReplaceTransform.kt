package com.example.asmdemo.transform

import com.example.asmdemo.transform.base.ClassNodeAdapter
import com.quinn.hunter.transform.HunterTransform
import com.quinn.hunter.transform.asm.BaseWeaver
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

/**
 * Created by cnting on 2022/8/1
 * 替换方法
 */
class PrivacyMethodReplaceTransform(project: Project) : HunterTransform(project) {
    init {
        this.bytecodeWeaver = MethodReplaceWeaver(project.logger)
    }
}

class MethodReplaceWeaver(private val logger: Logger) : BaseWeaver() {
    override fun wrapClassWriter(classWriter: ClassWriter): ClassVisitor {
        return MethodReplaceClassAdapter(classWriter, logger)
    }
}

class MethodReplaceClassAdapter(
    classVisitor: ClassVisitor,
    private val logger: Logger
) : ClassNodeAdapter(Opcodes.ASM7, classVisitor) {
    override fun transform(classNode: ClassNode) {
        if (AnnotationParserClassTransform.asmConfigsMap.containsKey(classNode.name)) {
            logger.info("===>忽略注解类:${classNode.name}")
            return
        }
        classNode.methods.forEach { methodNode ->
            //instructions表示指令
            //tip：为什么要遍历每个方法的字节码指令？因为需要hook的方法是系统的方法，没有被打包到apk中， 单纯遍历方法名是找不到的，必须遍历每个方法里面调用的字节码指令。
            methodNode.instructions?.iterator()?.forEach { insnNode ->
                if (insnNode is MethodInsnNode) {
                    AnnotationParserClassTransform.asmConfigs.forEach { asmItem ->
                        if (asmItem.oriDesc == insnNode.desc
                            && asmItem.oriMethod == insnNode.name
                            && asmItem.oriAccess == insnNode.opcode
                            && (asmItem.oriClass == insnNode.owner || asmItem.oriClass == "java/lang/Object")
                        ) {
                            logger.error(
                                "\nhook:\n" +
                                        "opcode=${insnNode.opcode},owner=${insnNode.owner},desc=${insnNode.desc},name=${classNode.name}#${insnNode.name} ->\n" +
                                        "opcode=${asmItem.targetAccess},owner=${asmItem.targetClass},desc=${asmItem.targetDesc},name=${asmItem.targetMethod}\n"
                            )
                            insnNode.opcode = asmItem.targetAccess
                            insnNode.desc = asmItem.targetDesc
                            insnNode.owner = asmItem.targetClass
                            insnNode.name = asmItem.targetMethod
                        }
                    }
                }
            }
        }
    }
}