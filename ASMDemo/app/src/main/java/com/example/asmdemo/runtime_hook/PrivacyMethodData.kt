package com.example.asmdemo.runtime_hook

/**
 * Created by cnting on 2022/8/1
 *
 */
data class PrivacyMethod(
    val methods: List<PrivacyMethodData>
)


data class PrivacyMethodData(
    val name_regex: String,
    val message: String
)
