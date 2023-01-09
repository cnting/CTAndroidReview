### 1.从xml收集View，使用new的方式创建
是对 [Gradle系列(四)、Gradle插件实战应用](https://juejin.cn/post/6989877607126794247) 的实践

相关module:
1. app
2. apt-annotation、apt-api、apt-processor
3. XmlViewScanPlugin

##### 做的事情：
1. 通过编写 Android 插件获取 Xml 布局中的所有控件
    * 具体看`XmlViewScanPlugin.groovy`

2. 拿到控件后，通过 APT 生成用 new 的方式创建 View 的类
3. 最后通过反射获取当前类并在基类里面完成替换

##### 遇到问题
1. 想要在app module中直接引用`XmlViewScanPlugin`源码插件，在project的build.gradle中配置了如下
```
//调试时用
dependencies {
    project(":XmlViewScanPlugin")
}
```
但在app的build.gradle中使用`apply plugin: "com.cnting.xmlviewscanplugin"`引用会报错`Plugin with id 'com.cnting.xmlviewscanplugin' not found`

### 2. hook gradle 插件
是对[如何简单方便地Hook Gradle插件?](https://juejin.cn/post/7095511659925471240)的实践

相关module:
1. app
2. buildSrc