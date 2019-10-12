package example.com.eventmap.util

import example.com.eventmap.MapsActivityViewModelFactory
import example.com.eventmap.data.MainRepository
import example.com.eventmap.ui.AddEventViewModelFactory

object InjectorUtils {

    private fun getMainRepositorySingleton(): MainRepository {
        return MainRepository.getInstance()
    }

    fun provideMapsActivityViewModelFactory(): MapsActivityViewModelFactory {
        val repository: MainRepository = getMainRepositorySingleton()
        return MapsActivityViewModelFactory(repository)
    }

    fun provideAddEventViewModelFactory(): AddEventViewModelFactory {
        val repository: MainRepository = getMainRepositorySingleton()
        return AddEventViewModelFactory(repository)
    }
}