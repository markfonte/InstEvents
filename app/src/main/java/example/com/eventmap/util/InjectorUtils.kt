package example.com.eventmap.util

import example.com.eventmap.MapsActivityViewModelFactory
import example.com.eventmap.data.MainRepository

object InjectorUtils {

    private fun getMainRepositorySingleton(): MainRepository {
        return MainRepository.getInstance()
    }

    fun provideMapsActivityViewModelFactory(): MapsActivityViewModelFactory {
        val repository: MainRepository = getMainRepositorySingleton()
        return MapsActivityViewModelFactory(repository)
    }
}