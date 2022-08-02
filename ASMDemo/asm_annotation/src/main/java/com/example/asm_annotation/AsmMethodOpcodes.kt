package com.example.asm_annotation

import org.objectweb.asm.Opcodes

/**
 * Opcodes 转换，业务可以不依赖 'org.ow2.asm:asm:7.1'
 */
object AsmMethodOpcodes {
    const val INVOKESTATIC = Opcodes.INVOKESTATIC
    const val INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL
}