package example.com.eventmap.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.com.eventmap.data.MainRepository

class SupportMapsViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getEventsToday(context: Context): MutableLiveData<ArrayList<HashMap<String, String>>> {
        return mainRepository.getEvents(context, today = true)
    }
}