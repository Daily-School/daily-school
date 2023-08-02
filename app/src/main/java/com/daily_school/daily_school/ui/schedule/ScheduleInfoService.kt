package com.daily_school.daily_school.ui.schedule

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleInfoService {

    @GET("misTimetable")
    suspend fun misScheduleData(
        @Query("KEY") KEY: String,
        @Query("Type") Type : String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("ATPT_OFCDC_SC_CODE") ATPT_OFCDC_SC_CODE: String,
        @Query("SD_SCHUL_CODE") SD_SCHUL_CODE : String,
        @Query("ALL_TI_YMD") ALL_TI_YMD : String) : Response<ScheduleInfoResponse>
}