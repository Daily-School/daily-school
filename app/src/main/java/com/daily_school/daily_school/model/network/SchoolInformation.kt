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

interface SchoolInfoApi {
    @GET("schoolInfo")
    fun getSchoolInfo(
        @Query("ATPT_OFCDC_SC_CODE") officeCode: String,
        @Query("SD_SCHUL_CODE") schoolCode: String,
        @Query("KEY") apiKey: String,
        @Query("Type") type: String = "json",
        @Query("pIndex") pageIndex: Int = 1,
        @Query("pSize") pageSize: Int = 10,
    ): Call<SchoolInfoResponse>
}

data class SchoolInfoResponse(
    var head: List<Any>? = null,
    var row: List<SchoolInfo>? = null
)

data class SchoolInfo(
    val SCHUL_NM: String,
    val ENG_SCHUL_NM: String,
    val SCHUL_KND_SC_NM: String,
    val ESTBLSH_YMD: String,
    val ORG_RDNMA: String,
    val ORG_RDNDA: String,
    val ORG_TELNO: String,
    val HMPG_ADRES: String,
    val FOND_SC_NM: String,
    val HS_SC_NM: String,
    val SPCLY_PURPS_HS_ORD_NM: String,
    val ENE_BFE_SEHF_SC_NM: String,
    val DGHT_SC_NM: String,
    val FOND_YMD: String,
    val FOAS_MEMRD: String,
    val LOAD_DTM: String
)

class SchoolInformation {
    private val TAG = SchoolInformation::class.java.simpleName

    private val api: SchoolInfoApi

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://open.neis.go.kr/hub/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(SchoolInfoApi::class.java)
    }

    fun getSchoolInfo(
        officeCode: String,
        schoolCode: String,
        apiKey: String,
        onSuccess: (SchoolInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        api.getSchoolInfo(officeCode, schoolCode, apiKey).enqueue(object : Callback<SchoolInfoResponse> {
            override fun onResponse(
                call: Call<SchoolInfoResponse>,
                response: Response<SchoolInfoResponse>
            ) {
                Log.d(TAG, "Response response: ${response}")

                if (response.isSuccessful) {
                    val schoolInfo = response.body()?.row?.getOrNull(0)

                    if (schoolInfo != null) {
                        onSuccess(schoolInfo)
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

            override fun onFailure(call: Call<SchoolInfoResponse>, t: Throwable) {
                Log.e(TAG, "API Fail $t")

                Log.d(TAG, "Response code: ${call}")

                onFailure(t)
            }
        })
    }
}