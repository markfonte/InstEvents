package example.com.eventmap.util

import example.com.eventmap.MapsActivityViewModelFactory
import example.com.eventmap.data.MainRepository
import example.com.eventmap.ui.AddEventViewModelFactory
import example.com.eventmap.ui.BrowseEventsViewModelFactory
import example.com.eventmap.ui.SupportMapsViewModelFactory

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

    fun provideBrowseEventsViewModelFactory(): BrowseEventsViewModelFactory {
        val repository: MainRepository = getMainRepositorySingleton()
        return BrowseEventsViewModelFactory(repository)
    }

    fun provideSupportMapsViewModelFactory(): SupportMapsViewModelFactory {
        val repository: MainRepository = getMainRepositorySingleton()
        return SupportMapsViewModelFactory(repository)
    }
}