package com.example.patch

/**
 * Created by cnting on 2022/8/8
 *
 */
object ReflectionUtil {
    fun getField(obj: Any, clazz: Class<out Any>, fieldName: String): Any {
        val field = clazz.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(obj)
    }

    fun setField(obj: Any, clazz: Class<Any>, fieldName: String, value: Any) {
        val field = clazz.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(obj, value)
    }
}