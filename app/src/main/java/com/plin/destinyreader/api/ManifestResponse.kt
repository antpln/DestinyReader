package com.plin.destinyreader.api

import com.google.gson.annotations.SerializedName

data class ManifestResponse(
    @SerializedName("Response")
    val response: Response
)

data class Response(
    val version: String,
    val mobileWorldContentPaths: MobileWorldContentPathhs
)

data class MobileWorldContentPathhs(
    val en: String,
    val fr: String,
    val es: String,
    val de: String,
    val it: String,
    val ja: String,
    val ru: String,
    val pl: String,
    val ko: String,
    @SerializedName("es-mx") val es_mx: String,
    @SerializedName("pt-br") val pt_br: String,
    @SerializedName("zh-cht") val zh_cht: String,
    @SerializedName("zh-chs") val zh_chs: String
)