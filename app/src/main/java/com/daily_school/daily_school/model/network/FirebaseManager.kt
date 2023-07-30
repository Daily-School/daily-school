import android.util.Log
import com.daily_school.daily_school.ui.plan.TodoColor
import com.daily_school.daily_school.ui.plan.TodoRepeat
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseManager {
    private val TAG = FirebaseManager::class.java.simpleName

    private val usersCollection = "users"
    private val timeField = "time"

    private val dataCollection = "data"

    private val subjectDocument = "subject"
    private val todoListDocument = "todoList"

    private val todoDateKey = "todoDate"
    private val todoNameKey = "todoName"
    private val todoRepeatKey = "todoRepeat"
    private val todoColorKey = "todoColor"
    private val todoCompleteKey = "todoComplete"
    private val todoCollection = "todos"

    // 학교 정보 DB 모델
    private val schoolInfoDocument = "schoolInfo"
    private val schoolNameKey = "schoolName"
    private val gradeKey = "grade"
    private val classKey = "class"
    private val cityCodeKey = "cityCode"
    private val schoolCodeKey = "schoolCode"

    // 날짜 정조 DB 모델
    private val dateInfoDocument = "dateInfo"
    private val dateTextKey = "dateText"
    private val mealDateInfoKey = "mealDateInfo"

    private val dDayDateInfoDocument = "dDayDateInfo"
    private val midDayKey = "midDay"
    private val finalDayKey = "finalDay"

    fun saveCurrentUser(uid: String) {
        if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection(usersCollection).document(uid)

            val currentTime = Calendar.getInstance().time as Any

            val data = mutableMapOf<String, Any>()
            data[timeField] = currentTime

            userDocRef.set(data)
                .addOnSuccessListener {
                    Log.d(TAG, "User saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to User Data", e)
                }
        }
        else {
            Log.e(TAG, "Uid is Null $uid")
        }
    }

    private fun getUserId(callback: (String) -> Unit) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                callback("")
            } else {
                val id = tokenInfo?.id.toString()
                callback(id ?: "")
            }
        }
    }

    fun saveSubjectData(subjectName: String, subjectColor: String) {
        getUserId { uid ->
            if (uid.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val subjectRef = db.collection(usersCollection)
                    .document(uid)
                    .collection(dataCollection)
                    .document(subjectDocument)

                val subjectData = mutableMapOf<String, Any>()
                subjectData[subjectName] = subjectColor

                subjectRef.get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                subjectRef.update(subjectData)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Subject Data Saved Successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Failed to Save Subject Data", e)
                                    }
                            } else {
                                subjectRef.set(subjectData)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "Subject Data Saved Successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Failed to Save Subject Data", e)
                                    }
                            }
                        } else {
                            Log.e(TAG, "Failed to Access Document", task.exception)
                        }
                    }
            } else {
                Log.e(TAG, "User UID is empty")
            }
        }
    }

    suspend fun readSubjectData(subjectName: String): Any? {
        var uid = ""
        getUserId { id ->
            uid = id
        }

        return if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val subjectRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(subjectDocument)

            try {
                val document = subjectRef.get().await()
                if (document != null && document.exists()) {
                    val subjectData = document.data
                    subjectData?.get(subjectName)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun saveTodoListData(todoDate: String, todoName: String, todoRepeat: String, todoColor: String) {
        getUserId { uid ->
            if (uid.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val todoListRef = db.collection(usersCollection)
                    .document(uid)
                    .collection(dataCollection)
                    .document(todoListDocument)

                val task = hashMapOf(
                    todoDateKey to todoDate,
                    todoNameKey to todoName,
                    todoRepeatKey to todoRepeat,
                    todoColorKey to todoColor,
                    todoCompleteKey to false
                )

                todoListRef.collection(todoCollection).add(task)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "할 일 추가 완료: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "할 일 추가 실패", e)
                    }
            } else {
                Log.e(TAG, "User UID is empty")
            }
        }
    }

    fun updateTodoComplete(todoName: String) {
        getUserId { uid ->
            if (uid.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val todoListRef = db.collection(usersCollection)
                    .document(uid)
                    .collection(dataCollection)
                    .document(todoListDocument)

                todoListRef.collection(todoCollection)
                    .whereEqualTo(todoNameKey, todoName)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val documentId = querySnapshot.documents[0].id
                            todoListRef.collection(todoCollection)
                                .document(documentId)
                                .update(todoCompleteKey, true)
                                .addOnSuccessListener {
                                    Log.d(TAG, "할 일 완료 처리 완료")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "할 일 완료 처리 실패", e)
                                }
                        } else {
                            Log.e(TAG, "일치하는 할 일을 찾을 수 없음")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "할 일 조회 실패", e)
                    }
            } else {
                Log.e(TAG, "User UID is empty")
            }
        }
    }

    suspend fun readTodoListData(uid: String): List<Map<String, Any>>? {
        return if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val todoListRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(todoListDocument)

            try {
                val querySnapshot = todoListRef.collection(todoCollection).get().await()
                return querySnapshot.documents.mapNotNull { document ->
                    document.data
                }
            } catch (e: Exception) {
                Log.e(TAG, "할 일 목록 보기 실패.", e)
                null
            }
        } else {
            Log.e(TAG, "토큰 정보 보기 실패.")
            null
        }
    }

    fun deleteSubjectData(subjectName: String) {
        getUserId { uid ->
            if (uid.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val subjectRef = db.collection(usersCollection)
                    .document(uid)
                    .collection(dataCollection)
                    .document(subjectDocument)

                subjectRef.get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                val subjectData = document.data
                                if (subjectData?.containsKey(subjectName) == true) {
                                    subjectData.remove(subjectName)

                                    subjectRef.set(subjectData)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "Subject Data Deleted Successfully")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Failed to Delete Subject Data", e)
                                        }
                                } else {
                                    Log.d(TAG, "Subject Data with key $subjectName does not exist")
                                }
                            } else {
                                Log.d(TAG, "Subject Data does not exist")
                            }
                        } else {
                            Log.e(TAG, "Failed to Access Document", task.exception)
                        }
                    }
            } else {
                Log.e(TAG, "User UID is empty")
            }
        }
    }

    // schoolInfo 저장하는 함수
    fun saveSchoolInfoData(uid: String, schoolName : String, gradeInfo : String, classInfo : String, cityCodeInfo : String, schoolCodeInfo : String){

        // uid가 존재할 때
        if(uid.isNotEmpty()){
            val db = FirebaseFirestore.getInstance()
            val schoolInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(schoolInfoDocument)

            val schoolInfoData = mutableMapOf<String, Any>()
            schoolInfoData[schoolNameKey] = schoolName
            schoolInfoData[gradeKey] = gradeInfo
            schoolInfoData[classKey] = classInfo
            schoolInfoData[cityCodeKey] = cityCodeInfo
            schoolInfoData[schoolCodeKey] = schoolCodeInfo

            schoolInfoRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            schoolInfoRef.update(schoolInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        } else {
                            schoolInfoRef.set(schoolInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        }
                    } else {
                        Log.e(TAG, "Failed to Access Document", task.exception)
                    }
                }
        }
        // uid가 존재하지 않을 때
        else {
            Log.e(TAG, "User UID is empty")
        }
    }

    // 학교 정보를 읽어오는 함수
    suspend fun readSchoolInfoData(uid : String): Map<String, Any>? {

        return if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val schoolInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(schoolInfoDocument)

            try {
                val document = schoolInfoRef.get().await()
                if (document != null && document.exists()) {
                    val schoolInfo = document.data
                    schoolInfo as? Map<String, Any>
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    // 급식 날짜를 저장하는 함수
    fun saveDateInfoData(uid: String, dateText : String, mealDateInfo: String){

        // uid가 존재할 때
        if(uid.isNotEmpty()){
            val db = FirebaseFirestore.getInstance()
            val dateInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(dateInfoDocument)

            val dateInfoData = mutableMapOf<String, Any>()
            dateInfoData[dateTextKey] = dateText
            dateInfoData[mealDateInfoKey] = mealDateInfo

            dateInfoRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            dateInfoRef.update(dateInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        } else {
                            dateInfoRef.set(dateInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        }
                    } else {
                        Log.e(TAG, "Failed to Access Document", task.exception)
                    }
                }
        }
        // uid가 존재하지 않을 때
        else {
            Log.e(TAG, "User UID is empty")
        }
    }

    // 급식 날짜를 불러오는 함수
    suspend fun readDateInfoData(uid : String): Map<String, Any>? {

        return if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val dateInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(dateInfoDocument)

            try {
                val document = dateInfoRef.get().await()
                if (document != null && document.exists()) {
                    val dateInfo = document.data
                    dateInfo as? Map<String, Any>
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    // 디데이 날짜를 저장하는 함수
    fun saveMidDDayDateInfoData(uid: String, midDay : String){

        // uid가 존재할 때
        if(uid.isNotEmpty()){
            val db = FirebaseFirestore.getInstance()
            val dDayDateInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(dDayDateInfoDocument)

            val dDayDateInfoData = mutableMapOf<String, Any>()
            dDayDateInfoData[midDayKey] = midDay

            dDayDateInfoRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            dDayDateInfoRef.update(dDayDateInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        } else {
                            dDayDateInfoRef.set(dDayDateInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        }
                    } else {
                        Log.e(TAG, "Failed to Access Document", task.exception)
                    }
                }
        }
        // uid가 존재하지 않을 때
        else {
            Log.e(TAG, "User UID is empty")
        }
    }


    fun saveFinalDDayDateInfoData(uid: String, finalDay : String){

        // uid가 존재할 때
        if(uid.isNotEmpty()){
            val db = FirebaseFirestore.getInstance()
            val dDayDateInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(dDayDateInfoDocument)

            val dDayDateInfoData = mutableMapOf<String, Any>()
            dDayDateInfoData[finalDayKey] = finalDay

            dDayDateInfoRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            dDayDateInfoRef.update(dDayDateInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        } else {
                            dDayDateInfoRef.set(dDayDateInfoData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "SchoolInfo Data Saved Successfully")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Failed to Save SchoolInfo Data", e)
                                }
                        }
                    } else {
                        Log.e(TAG, "Failed to Access Document", task.exception)
                    }
                }
        }
        // uid가 존재하지 않을 때
        else {
            Log.e(TAG, "User UID is empty")
        }
    }

    // 디데이 날짜를 불러오는 함수
    suspend fun readDDayDateInfoData(uid : String): Map<String, Any>? {

        return if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val dDayDateInfoRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(dDayDateInfoDocument)

            try {
                val document = dDayDateInfoRef.get().await()
                if (document != null && document.exists()) {
                    val dateInfo = document.data
                    dateInfo as? Map<String, Any>
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}