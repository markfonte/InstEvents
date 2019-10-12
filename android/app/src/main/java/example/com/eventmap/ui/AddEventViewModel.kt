package example.com.eventmap.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import example.com.eventmap.data.MainRepository

class AddEventViewModel(private val mainRepository: MainRepository) : ViewModel() {

    var isPickerLoading: MutableLiveData<Boolean> = MutableLiveData()
    var startHour: Int = 0
    var startMinute: Int = 0
    var endHour: Int = 0
    var endMinute: Int = 0
    var startYear: Int = 0
    var startMonth: Int = 0
    var startDay: Int = 0
    var endYear: Int = 0
    var endMonth: Int = 0
    var endDay: Int = 0

    fun addEvent(title: String, description: String, location: String) : MutableLiveData<Boolean>{
        var startDate = ""
        var endDate = ""
        startDate += startYear
        startDate += "-"
        if (startMonth < 10) {
            startDate += "0"
        }
        startDate += startMonth
        startDate += "-"
        if (startDay < 10) {
            startDate += "0"
        }
        startDate += startDay
        startDate += "-"
        if (startHour < 10) {
            startDate += "0"
        }
        startDate += startHour
        startDate += "-"
        if (startMinute < 10) {
            startDate += "0"
        }
        startDate += startMinute
        endDate += endYear
        endDate += "-"
        if (endMonth < 10) {
            endDate += "0"
        }
        endDate += endMonth
        endDate += "-"
        if (endDay < 10) {
            endDate += "0"
        }
        endDate += endDay
        endDate += "-"
        if (endHour < 10) {
            endDate += "0"
        }
        endDate += endHour
        endDate += "-"
        if (endMinute < 10) {
            endDate += "0"
        }
        endDate += endMinute

        return mainRepository.addEvent(title, description, startDate, endDate, location)

    }
    init {
        isPickerLoading.value = false
    }

}