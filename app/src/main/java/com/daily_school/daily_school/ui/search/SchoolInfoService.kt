package com.daily_school.daily_school.ui.search


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// api를 호출할 때 필요한 쿼리를 담아놓은 인터페이스
interface SchoolInfoService {
    @GET("schoolInfo")
    suspend fun getDataCoroutine(
        @Query("KEY") KEY: String,
        @Query("Type") Type : String,
        @Query("pIndex") pIndex : Int,
        @Query("pSize") pSize : Int,
        @Query("SCHUL_NM") SCHUL_NM : String): Response<SchoolInfoResponse>
}