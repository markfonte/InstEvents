package example.com.eventmap.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.com.eventmap.data.MainRepository

class BrowseEventsViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getAllEvents(context: Context) : MutableLiveData<ArrayList<HashMap<String, String>>> {
        return mainRepository.getEvents(context, today = false)
    }
}