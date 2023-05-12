package com.cnting.mvi.model

/**
 * Created by cnting on 2023/4/17
 *
 */
data class Article(
 var curPage: Int,
 val datas: List<ArticleItem>
)

data class ArticleItem(
 val title: String,
 val link: String,
 val userId: Int,
 val niceDate: String
)