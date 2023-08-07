package com.daily_school.daily_school.model.network

import com.daily_school.daily_school.ui.meal.MealInfoService
import com.daily_school.daily_school.ui.schedule.ScheduleInfoService
import com.daily_school.daily_school.ui.search.SchoolInfoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    private const val BASE_URL = "https://open.neis.go.kr/hub/"

    private val okHttpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }
    val schoolInfoService : SchoolInfoService by lazy {
        retrofit.create(SchoolInfoService::class.java)
    }
    val mealInfoService : MealInfoService by lazy{
        retrofit.create(MealInfoService::class.java)
    }

    val scheduleService : ScheduleInfoService by lazy{
        retrofit.create(ScheduleInfoService::class.java)
    }
}