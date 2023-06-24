import android.util.Log
import com.daily_school.daily_school.ui.schedule.AddSubjectActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

interface ReadDataCallback {
    fun onSuccess(value: Any?)
    fun onFailure(exception: Exception)
}
class FirebaseManager {
    private val TAG = FirebaseManager::class.java.simpleName

    private val usersCollection = "users"
    private val timeField = "time"

    private val dataCollection = "data"
    private val subjectDocument = "subject"

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

    private fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun saveSubjectData(subjectName: String, subjectColor: String) {
        val uid = getUserId()

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

    fun readSubjectData(subjectName: String, callback: ReadDataCallback) {
        val uid = getUserId()

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
                            if (subjectData != null) {
                                val value = subjectData[subjectName]
                                callback.onSuccess(value)
                            } else {
                                callback.onSuccess(null)
                            }
                        } else {
                            callback.onSuccess(null)
                        }
                    } else {
                        callback.onFailure(task.exception!!)
                    }
                }
        } else {
            callback.onFailure(Exception("User UID is empty"))
        }
    }
}