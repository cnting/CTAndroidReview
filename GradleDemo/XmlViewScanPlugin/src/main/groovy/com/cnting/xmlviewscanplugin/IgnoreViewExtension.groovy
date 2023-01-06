package com.cnting.xmlviewscanplugin
/**
 * 配置黑名单：一些View不需要被扫描
 * 主要针对一些特殊的View：构造方法是私有的，不能被new出来
 */
class IgnoreViewExtension{
    //是否开启黑名单功能，默认开启
    boolean isEnable = true

    List<String> ignoreViewList
}