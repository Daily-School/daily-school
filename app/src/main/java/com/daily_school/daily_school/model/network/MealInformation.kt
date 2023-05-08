package com.daily_school.daily_school.model.network

import android.util.Log
import com.daily_school.daily_school.ui.LoginActivity
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi  {
    @GET("mealServiceDietInfo")
    fun getMeal(
        @Query("ATPT_OFCDC_SC_CODE") officeCode: String,
        @Query("SD_SCHUL_CODE") schoolCode: String,
        @Query("KEY") apiKey: String,
        @Query("MLSV_YMD") date: String,
        @Query("MMEAL_SC_CODE") mealType: String = "2",
        @Query("Type") type: String = "json",
        @Query("pIndex") pageIndex: Int = 1,
        @Query("pSize") pageSize: Int = 10,
    ): Call<MealResponse>
}

data class MealResponse(
    var row: List<MealInfo>? = null
)

data class MealInfo(
    val ATPT_OFCDC_SC_CODE: String,
    val ATPT_OFCDC_SC_NM: String,
    val SD_SCHUL_CODE: String,
    val SCHUL_NM: String,
    val MMEAL_SC_CODE: String,
    val MMEAL_SC_NM: String,
    val MLSV_YMD: String,
    val MLSV_FGR: String,
    val DDISH_NM: String?,
    val ORPLC_INFO: String,
    val CAL_INFO: String,
    val NTR_INFO: String,
    val MLSV_FROM_YMD: String,
    val MLSV_TO_YMD: String,
    val MLSV_YMD_DOW: String
)

class MealInformation {
    private val TAG = MealInformation::class.java.simpleName

    private val api: MealApi

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://open.neis.go.kr/hub/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(MealApi::class.java)
    }

    fun getMealInfo(
        officeCode: String,
        schoolCode: String,
        apiKey: String,
        date: String,
        onSuccess: (MealInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        api.getMeal(officeCode, schoolCode, apiKey, date).enqueue(object : Callback<MealResponse> {
            override fun onResponse(
                call: Call<MealResponse>,
                response: Response<MealResponse>
            ) {
                Log.d(TAG, "Response response: ${response}")

                if (response.isSuccessful) {
                    val mealInfo = response.body()?.row?.getOrNull(0)

                    if (mealInfo != null) {
                        onSuccess(mealInfo)
                    }
                    else {
                        onFailure(Throwable("No school info found"))
                    }
                }
                else {
                    Log.e(TAG, "API Fail $response.message()")
                    onFailure(Throwable(response.message()))
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Log.e(TAG, "API Fail $t")

                Log.d(TAG, "Response code: ${call}")

                onFailure(t)
            }
        })
    }
}