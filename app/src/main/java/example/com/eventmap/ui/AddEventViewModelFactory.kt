package example.com.eventmap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import example.com.eventmap.MapsActivityViewModel
import example.com.eventmap.data.MainRepository

class AddEventViewModelFactory (private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddEventViewModel(repository) as T
    }
}