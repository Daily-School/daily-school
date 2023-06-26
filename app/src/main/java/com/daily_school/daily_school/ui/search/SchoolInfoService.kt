package com.daily_school.daily_school.ui.search


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolInfoService {
    @GET("schoolInfo")
    fun getSchoolInfoData(@Query("KEY") KEY: String,
                          @Query("Type") Type : String) : Call<SchoolInfoResponse>

//    @GET("schoolInfo")
//    suspend fun getDataCoroutine(
//        @Query("KEY") KEY: String,
//        @Query("Type") Type : String) : Response<SchoolInfoResponse>
}