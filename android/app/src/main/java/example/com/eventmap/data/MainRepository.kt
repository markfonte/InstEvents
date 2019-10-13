package example.com.eventmap.data

import android.content.Context
import androidx.lifecycle.MutableLiveData

class MainRepository {
    private var firebaseService: FirebaseService = FirebaseService.getInstance()
    private var volleyService: VolleyService = VolleyService.getInstance()

    fun addEvent(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        location: String,
        context: Context
    ): MutableLiveData<Boolean> {
        return volleyService.addEvent(title, description, startDate, endDate, location, context)
    }

    fun getTodaysEvents(context: Context) : MutableLiveData<ArrayList<HashMap<String, String>>> {
        return volleyService.getTodaysEvents(context)
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