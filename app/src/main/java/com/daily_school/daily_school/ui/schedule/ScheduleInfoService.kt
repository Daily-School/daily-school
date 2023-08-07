package com.daily_school.daily_school.ui.schedule

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleInfoService {
    @GET("elsTimetable")
    suspend fun elsTimetable(
        @Query("KEY") KEY: String,
        @Query("Type") Type : String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("ATPT_OFCDC_SC_CODE") ATPT_OFCDC_SC_CODE: String,
        @Query("SD_SCHUL_CODE") SD_SCHUL_CODE : String,
        @Query("GRADE") GRADE : String,
        @Query("CLASS_NM") CLASS_NM : String,
        @Query("ALL_TI_YMD") ALL_TI_YMD : String) : Response<ElsScheduleInfoResponse>

    @GET("misTimetable")
    suspend fun misTimetable(
        @Query("KEY") KEY: String,
        @Query("Type") Type : String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("ATPT_OFCDC_SC_CODE") ATPT_OFCDC_SC_CODE: String,
        @Query("SD_SCHUL_CODE") SD_SCHUL_CODE : String,
        @Query("GRADE") GRADE : String,
        @Query("CLASS_NM") CLASS_NM : String,
        @Query("ALL_TI_YMD") ALL_TI_YMD : String) : Response<MisScheduleInfoResponse>

    @GET("hisTimetable")
    suspend fun hisTimetable(
        @Query("KEY") KEY: String,
        @Query("Type") Type : String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("ATPT_OFCDC_SC_CODE") ATPT_OFCDC_SC_CODE: String,
        @Query("SD_SCHUL_CODE") SD_SCHUL_CODE : String,
        @Query("GRADE") GRADE : String,
        @Query("CLASS_NM") CLASS_NM : String,
        @Query("ALL_TI_YMD") ALL_TI_YMD : String) : Response<HisScheduleInfoResponse>

    @GET("spsTimetable")
    suspend fun spsTimetable(
        @Query("KEY") KEY: String,
        @Query("Type") Type : String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("ATPT_OFCDC_SC_CODE") ATPT_OFCDC_SC_CODE: String,
        @Query("SD_SCHUL_CODE") SD_SCHUL_CODE : String,
        @Query("GRADE") GRADE : String,
        @Query("CLASS_NM") CLASS_NM : String,
        @Query("ALL_TI_YMD") ALL_TI_YMD : String) : Response<SpsScheduleInfoResponse>
}