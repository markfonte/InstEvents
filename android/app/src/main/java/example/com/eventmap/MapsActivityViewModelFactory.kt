package example.com.eventmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import example.com.eventmap.data.MainRepository

class MapsActivityViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapsActivityViewModel(repository) as T
    }
}