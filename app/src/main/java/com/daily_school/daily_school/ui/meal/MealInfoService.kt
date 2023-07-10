package com.daily_school.daily_school.ui.meal

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealInfoService {
    @GET("mealServiceDietInfo")
    suspend fun todayMealData(
        @Query("Type") Type: String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("ATPT_OFCDC_SC_CODE") ATPT_OFCDC_SC_CODE : String,
        @Query("SD_SCHUL_CODE") SD_SCHUL_CODE : String,
        @Query("MLSV_FROM_YMD") MLSV_FROM_YMD: String,
        @Query("MLSV_TO_YMD") MLSV_TO_YMD : String,
        @Query("KEY") KEY: String) : Response<MealInfoResponse>
}