package com.example.asmdemo.transform.base

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.tree.ClassNode

/**
 * Created by cnting on 2022/8/2
 * 将 ClassVisitor 转成 ClassNode，会简单点
 */
abstract class ClassNodeAdapter(api: Int, private val classVisitor: ClassVisitor) :
    ClassVisitor(api, classVisitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.cv = ClassNode()
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitEnd() {
        super.visitEnd()
        val classNode = cv as ClassNode
        transform(classNode)
        classNode.accept(classVisitor)
    }

    abstract fun transform(classNode: ClassNode)
}