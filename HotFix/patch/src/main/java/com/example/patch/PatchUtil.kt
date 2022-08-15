package com.example.patch

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.reflect.Array
import java.nio.file.Files

/**
 * Created by cnting on 2022/8/8
 *
 */
object PatchUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadPatch(context: Context, patchName: String): String {
        val file = getPatchFile(context, patchName)
        if(file.exists()){
            file.delete()
        }
        Files.copy(context.assets.open(patchName), file.toPath())
        return file.absolutePath
    }

    fun getPatchFile(context: Context, patchName: String): File {
        return File(context.cacheDir, patchName)
    }

    fun hasPatch(context: Context, patchName: String): Boolean {
        return getPatchFile(context, patchName).exists()
    }

    fun deletePatch(context: Context, patchName: String) {
        val file = getPatchFile(context, patchName)
        file.delete()
    }

    fun loadPatch(context: Context, patchName: String) {
        val newClassLoader = DexClassLoader(
            getPatchFile(context, patchName).absolutePath,
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
        return ReflectionUtil.getField(
            baseDexClassLoader,
            BaseDexClassLoader::class.java,
            "pathList"
        )
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