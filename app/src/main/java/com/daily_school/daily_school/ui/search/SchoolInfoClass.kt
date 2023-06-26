package com.daily_school.daily_school.ui.search

import com.google.gson.annotations.SerializedName

data class SchoolInfoResponse(
    @SerializedName("schoolInfo")
    val schoolInfo : List<SchoolInfo>?
)
data class SchoolInfo(
    val head : List<Head>?,
    val row : List<Row>?
)
data class Head(
    @SerializedName("list_total_count")
    val listTotalCount: Int?,
    @SerializedName("RESULT")
    val rESULT: RESULT?
)
data class RESULT(
    @SerializedName("CODE")
    val cODE: String?, // INFO-000
    @SerializedName("MESSAGE")
    val mESSAGE: String? // 정상 처리되었습니다.
)
data class Row(
    @SerializedName("SCHUL_NM")
    val sCHULNM : String?,
    @SerializedName("ORG_RDNMA")
    val oRGRDNMA : String?,
)