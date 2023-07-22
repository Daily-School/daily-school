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
    private val todoNameKey = "todoName"
    private val todoRepeatKey = "todoRepeat"
    private val todoColorKey = "todoColor"

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

    fun saveTodoListData(todoName: String, todoRepeat: TodoRepeat, todoColor: TodoColor) {
        getUserId { uid ->
            if (uid.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val subjectRef = db.collection(usersCollection)
                    .document(uid)
                    .collection(dataCollection)
                    .document(todoListDocument)

                val subjectData = mutableMapOf<String, Any>()
                subjectData[todoNameKey] = todoName
                subjectData[todoRepeatKey] = todoRepeat.name
                subjectData[todoColorKey] = todoColor.name

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

    suspend fun readTodoListData(): Map<String, Any>? {
        var uid = ""
        getUserId { id ->
            uid = id
        }

        return if (uid.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val subjectRef = db.collection(usersCollection)
                .document(uid)
                .collection(dataCollection)
                .document(todoListDocument)

            try {
                val document = subjectRef.get().await()
                if (document != null && document.exists()) {
                    val todoData = document.data
                    todoData as? Map<String, Any>
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
}