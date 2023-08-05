package com.daily_school.daily_school.ui.schedule

import com.google.gson.annotations.SerializedName

data class ElsScheduleInfoResponse(
    @SerializedName("elsTimetable")
    val scheduleInfo : List<ScheduleInfo>?
)

data class MisScheduleInfoResponse(
    @SerializedName("misTimetable")
    val scheduleInfo : List<ScheduleInfo>?
)

data class HisScheduleInfoResponse(
    @SerializedName("hisTimetable")
    val scheduleInfo : List<ScheduleInfo>?
)

data class SpsScheduleInfoResponse(
    @SerializedName("spsTimetable")
    val scheduleInfo : List<ScheduleInfo>?
)

data class ScheduleInfo(
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
    val cODE: String?,
    @SerializedName("MESSAGE")
    val mESSAGE: String?
)
data class Row(
    @SerializedName("ATPT_OFCDC_SC_CODE")
    val aTPTOFCDCSCCODE : String,
    @SerializedName("SD_SCHUL_CODE")
    val sDSCHULCODE : String,
    @SerializedName("ALL_TI_YMD")
    val aLLTIYMD : String,
    @SerializedName("PERIO")
    val pERIO : String,
    @SerializedName("ITRT_CNTNT")
    val iTRTCNTNT : String

)
