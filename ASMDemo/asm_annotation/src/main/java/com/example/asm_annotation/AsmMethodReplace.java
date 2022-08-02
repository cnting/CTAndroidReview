package com.example.asm_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AsmMethodReplace {
    Class oriClass();

    String oriMethod() default "";

    int oriAccess() default AsmMethodOpcodes.INVOKESTATIC;
}