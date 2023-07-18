package com.daily_school.daily_school.ui.meal.launch

import FirebaseManager
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.daily_school.daily_school.R
import com.daily_school.daily_school.databinding.FragmentMealLaunchTodayDialogBinding
import com.daily_school.daily_school.model.network.RetrofitApi
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastTodayDialogRvAdapter
import com.daily_school.daily_school.ui.meal.breakfast.MealBreakfastTodayModel
import com.daily_school.daily_school.utils.KakaoRef
import com.daily_school.daily_school.utils.NeisRef
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MealLaunchTodayDialogFragment : DialogFragment() {

    private var TAG = MealLaunchTodayDialogFragment::class.java.simpleName

    private lateinit var binding : FragmentMealLaunchTodayDialogBinding

    private var firebaseManager = FirebaseManager()

    private var dateValue : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_launch_today_dialog, container, false)

        loadMealDate()

        todayMeal()

        fragmentFinish()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params : ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y

        params?.width = (deviceWidth * 0.7).toInt()
        params?.height = (deviceHeight * 0.6).toInt()

        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun todayMeal(){

        val service = RetrofitApi.mealInfoService


        KakaoSdk.init(requireContext(), KakaoRef.APP_KEY)

        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Log.e(TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null){
                lifecycleScope.launch{
                    try {
                        val userInfo = firebaseManager.readSchoolInfoData(tokenInfo.id.toString())
                        if (userInfo != null) {
                            val userCityCode = userInfo["cityCode"]
                            val userSchoolCode = userInfo["schoolCode"]

                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "사용자 정보 요청 실패 $error")
                                } else if (user != null) {
                                    Log.e(TAG, "사용자 정보 요청 성공 : $user")

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val response = service.todayMealData( "json", 1,100, userCityCode.toString(), userSchoolCode.toString()
                                            , dateValue, dateValue, NeisRef.api_key)

                                        withContext(Dispatchers.Main) {
                                            if (response.isSuccessful) {

                                                if(response.body()?.mealServiceDietInfo?.get(1)?.row?.get(0)?.mMEALSCNM.toString() == "중식"){
                                                    val result = response.body()?.mealServiceDietInfo?.get(1)?.row?.get(0)?.dDISHNM.toString()

                                                    var todayLaunchMenu = result

                                                    val arr = todayLaunchMenu.split("<br/>")

                                                    if(arr.size <= 1){
                                                        binding.mealLaunchDialogNotionIc.visibility = View.VISIBLE
                                                        binding.mealLaunchNotionDialogTextView.visibility = View.VISIBLE
                                                        binding.mealLaunchTodayDialogRv.visibility = View.INVISIBLE
                                                    }else{
                                                        val todayItems = ArrayList<MealLaunchTodayModel>()

                                                        for (element in arr) {
                                                            todayItems.add(MealLaunchTodayModel(element))
                                                        }

                                                        binding.mealLaunchDialogNotionIc.visibility = View.INVISIBLE
                                                        binding.mealLaunchNotionDialogTextView.visibility = View.INVISIBLE
                                                        binding.mealLaunchTodayDialogRv.visibility = View.VISIBLE

                                                        val todayLaunchDialogRvAdapter = MealLaunchTodayDialogRvAdapter(todayItems)
                                                        val todayLaunchDialogRv = binding.mealLaunchTodayDialogRv
                                                        todayLaunchDialogRv.adapter = todayLaunchDialogRvAdapter
                                                        todayLaunchDialogRv.layoutManager = GridLayoutManager(requireContext(), 1)
                                                    }
                                                }

                                            } else {
                                                Log.e(TAG, response.code().toString())
                                            }
                                        }
                                    }
                                }
                            }


                        } else {
                            Log.d(TAG, "TodoList is Null")
                        }
                    }
                    catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }
    }

    private fun loadMealDate(){
        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
            if (error != null){
                Log.e(TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null){

                lifecycleScope.launch {
                    try {
                        val dateInfo = firebaseManager.readDateInfoData(tokenInfo.id.toString())
                        if(dateInfo != null){
                            val dateText = dateInfo["dateText"]
                            val mealDateInfo = dateInfo["mealDateInfo"]

                            binding.mealLaunchTodayDialogDateTxt.text = dateText.toString()
                            dateValue = mealDateInfo.toString()
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to Read", e)
                    }
                }

            }
        }
    }

    private fun fragmentFinish(){
        binding.mealLaunchIcFinish.setOnClickListener {
            dismiss()
        }
    }

}