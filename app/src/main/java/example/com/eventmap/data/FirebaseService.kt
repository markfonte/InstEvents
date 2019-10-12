package example.com.eventmap.data

class FirebaseService {

    companion object {
        private val LOG_TAG: String = FirebaseService::class.java.name

        // For Singleton instantiation
        @Volatile
        private var instance: FirebaseService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseService().also { instance = it }
            }
    }
}