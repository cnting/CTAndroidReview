package com.example.hotfix

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.nio.file.Files

/**
 * Created by cnting on 2022/8/8
 *
 */
object PatchUtil {

    private const val patchName = "hotfix.dex"

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadPatch(context: Context): String {
        val file = getPatchFile(context)
        Files.copy(context.assets.open(patchName), file.toPath())
        return file.absolutePath
    }

    private fun getPatchFile(context: Context): File {
        return File(context.cacheDir, patchName)
    }

    fun hasPatch(context: Context): Boolean {
        return getPatchFile(context).exists()
    }

    fun deletePatch(context: Context) {
        val file = getPatchFile(context)
        file.delete()
    }

    fun loadPatch(context: Context) {
        val newClassLoader = DexClassLoader(
            getPatchFile(context).absolutePath,
            context.cacheDir.absolutePath,
            null,
            getPathClassLoader()
        )
        val basePathList = getPathList(getPathClassLoader())
        val baseDexElements = getDexElements(basePathList)
        val newDexElements = getDexElements(getPathList(newClassLoader))
        //newDexElements要放在前面
        val allDexElements = combineArray(newDexElements, baseDexElements)
        ReflectionUtil.setField(basePathList, basePathList.javaClass, "dexElements", allDexElements)
    }

    private fun getPathClassLoader(): PathClassLoader {
        return this.javaClass.classLoader as PathClassLoader
    }

    private fun getPathList(baseDexClassLoader: BaseDexClassLoader): Any {
        return ReflectionUtil.getField(baseDexClassLoader, BaseDexClassLoader::class.java, "pathList")
    }

    private fun getDexElements(pathList: Any): Any {
        return ReflectionUtil.getField(pathList, pathList.javaClass, "dexElements")
    }

    /**
     * 使用反射构建数组
     */
    private fun combineArray(firstArray: Any, secondArray: Any): Any {
        val clazz = firstArray.javaClass.componentType
        val firstLength = Array.getLength(firstArray)
        val secondLength = Array.getLength(secondArray)
        val concatDexElementObject = Array.newInstance(clazz, firstLength + secondLength)
        (0 until firstLength).forEach {
            Array.set(concatDexElementObject, it, Array.get(firstArray, it))
        }
        (0 until secondLength).forEach {
            Array.set(concatDexElementObject, firstLength + it, Array.get(secondArray, it))
        }
        return concatDexElementObject
    }
}