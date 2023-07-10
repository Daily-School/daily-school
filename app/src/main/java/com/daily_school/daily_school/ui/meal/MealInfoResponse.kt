package com.daily_school.daily_school.ui.meal
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MealInfoResponse (
    @SerializedName("mealServiceDietInfo")
    val mealServiceDietInfo : List<MealServiceDietInfo>?
)
data class MealServiceDietInfo(
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
    @SerializedName("MMEAL_SC_NM")
    val mMEALSCNM : String?,
    @SerializedName("MLSV_YMD")
    val mLSVYMD : String?,
    @SerializedName("DDISH_NM")
    val dDISHNM : String?,
    @SerializedName("CAL_INFO")
    val cALINFO : String
)