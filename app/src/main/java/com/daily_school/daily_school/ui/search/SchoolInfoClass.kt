package com.daily_school.daily_school.ui.search
import com.google.gson.annotations.SerializedName

// 학교 정보 api에 필요한 데이터 모델 분류
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
    @SerializedName("ATPT_OFCDC_SC_CODE")
    val aTPTOFCDCSCCODE : String,
    @SerializedName("SD_SCHUL_CODE")
    val sDSCHULCODE : String,
    @SerializedName("SCHUL_KND_SC_NM")
    val sCHULKNDSCNM : String
)