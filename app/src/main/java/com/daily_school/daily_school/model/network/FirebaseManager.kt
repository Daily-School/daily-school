import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class FirebaseManager {
    private val TAG = "FirebaseManager"

    private val usersCollection = "users"

    fun saveCurrentUser(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection(usersCollection).document(uid)

        val currentTime = Calendar.getInstance().time as Any

        val data = mutableMapOf<String, Any>()
        data["time"] = currentTime

        userDocRef.set(data)
            .addOnSuccessListener {
                Log.d(TAG, "Time saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save time", e)
            }
    }
}