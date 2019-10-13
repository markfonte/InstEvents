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
        context: Context,
        latitude: Double,
        longitude: Double,
        imageUrl: String
    ): MutableLiveData<Boolean> {
        return volleyService.addEvent(
            title,
            description,
            startDate,
            endDate,
            location,
            context,
            latitude,
            longitude,
            imageUrl
        )
    }

    fun getEvents(
        context: Context,
        today: Boolean
    ): MutableLiveData<ArrayList<HashMap<String, String>>> {
        return volleyService.getEvents(context, today)
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