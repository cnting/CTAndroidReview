package com.example.ioc_processor

import com.example.ioc_annotation.ViewInjector
import com.squareup.javapoet.*
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * Created by cnting on 2022/7/27
 */
class ProxyInfo(
    elementUtils: Elements,
    //类信息
    typeElement: TypeElement
) {
    //被注解的变量和id映射表
    @JvmField
    val injectElements = mutableMapOf<Int, VariableElement>()

    val proxyClassFullName: String
        get() = "$packageName.$proxyClassName"

    companion object {
        private const val SUFFIX = "ViewInjector"
    }

    //包名
    private val packageName: String

    //代理类名
    private val proxyClassName: String

    private val classType:TypeName

    init {
        val packageElement = elementUtils.getPackageOf(typeElement)
        //得到包名
        packageName = packageElement.qualifiedName.toString()
        //得到类信息
        classType = TypeName.get(typeElement.asType())
        val className = getClassName(typeElement, packageName)
        proxyClassName = "$className$$$SUFFIX"
    }

    private fun getClassName(typeElement: TypeElement, packageName: String): String {
        val packageLength = packageName.length + 1
        //得到最后的类名
        return typeElement.qualifiedName.toString().substring(packageLength).replace('.', '$')
    }

    /**
     * 生成如下代码
     * import com.example.ioc.*;
     * public class MainActivity$$ViewInjector implements ViewInjector<com.example.cnting.aptdemo.MainActivity> {
     *
     *  @Override public void inject(com.example.cnting.aptdemo.MainActivity host, Object object) {
     *      if (object instanceof android.app.Activity) {
     *          host.textView = (android.widget.TextView) ((android.app.Activity) object).findViewById(2131165262);
     *      } else {
     *          host.textView = (android.widget.TextView) ((android.view.View) object).findViewById(2131165262);
     *      }
     *   }
     * }
     */
    fun generateJavaCode(filer: Filer) {
        val injectMethodBuilder = MethodSpec.methodBuilder("inject")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(Void.TYPE)
            .addParameter(classType, "host")
            .addParameter(Any::class.java, "object")

        injectElements.forEach { (id, variableElement) ->
            val name = variableElement.simpleName.toString()
            val type = variableElement.asType().toString()

            val code = """
                if (object instanceof android.app.Activity) {
                    host.${name} = (${type})((android.app.Activity) object).findViewById(${id});
                }else{
                    host.${name} = (${type})((android.view.View) object).findViewById(${id});
                }
            """
            injectMethodBuilder.addCode(code)
        }

        val classSpec = TypeSpec.classBuilder(proxyClassName)
            .addModifiers(Modifier.PUBLIC)
            .addMethod(injectMethodBuilder.build())
            .addSuperinterface(
                ParameterizedTypeName.get(
                    ClassName.get(ViewInjector::class.java),
                    classType
                )
            )
            .build()

        JavaFile.builder(packageName, classSpec)
            .build()
            .writeTo(filer)
    }


}