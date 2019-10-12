package example.com.eventmap.data

import androidx.lifecycle.MutableLiveData

class MainRepository {
    private var firebaseService: FirebaseService = FirebaseService.getInstance()
    private var volleyService: VolleyService = VolleyService.getInstance()

    fun addEvent(title: String, description: String, startDate: String, endDate:String, location: String) : MutableLiveData<Boolean> {
        return firebaseService.addEvent(title, description, startDate, endDate, location)
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MainRepository().also { instance = it }
            }
    }
}