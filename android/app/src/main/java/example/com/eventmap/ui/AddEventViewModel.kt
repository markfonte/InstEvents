package example.com.eventmap.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.com.eventmap.data.MainRepository

class AddEventViewModel(private val mainRepository: MainRepository) : ViewModel() {

    var isPickerLoading: MutableLiveData<Boolean> = MutableLiveData()
    var startTimeHour: Int = 0
    var startTimeMinute: Int = 0
    var endTimeHour: Int = 0
    var endTimeMinute: Int = 0

    init {
        isPickerLoading.value = false
    }

}