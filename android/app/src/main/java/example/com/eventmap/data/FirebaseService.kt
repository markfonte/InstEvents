package example.com.eventmap.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseService {

    private var fsDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance()


    fun addEvent(title: String, description: String, startDate: String, endDate:String, location: String) : MutableLiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()
        val eventInfoMap = HashMap<String, String>()
        eventInfoMap["start_date"] = startDate
        eventInfoMap["end_date"] = endDate
        eventInfoMap["description"] = description
        eventInfoMap["location"] = location
        eventInfoMap["title"] = title
        eventInfoMap["image_url"] = ""
        fsDb.collection("events").add(eventInfoMap).addOnCompleteListener{ task ->
            result.value = task.isSuccessful
        }
        return result
    }


    companion object {
        private val LOG_TAG: String = FirebaseService::class.java.name

        // For Singleton instantiation
        @Volatile
        private var instance: FirebaseService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseService().also { instance = it }
            }
    }
}