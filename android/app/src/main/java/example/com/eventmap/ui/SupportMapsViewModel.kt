package example.com.eventmap.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.com.eventmap.data.MainRepository

class SupportMapsViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun getTodaysEvents(context: Context) : MutableLiveData<ArrayList<HashMap<String, String>>> {
        return mainRepository.getTodaysEvents(context)
    }
}