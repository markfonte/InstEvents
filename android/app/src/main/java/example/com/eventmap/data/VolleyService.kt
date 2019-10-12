package example.com.eventmap.data

class VolleyService {


    companion object {
        private val LOG_TAG: String = VolleyService::class.java.name

        // For Singleton instantiation
        @Volatile
        private var instance: VolleyService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: VolleyService().also { instance = it }
            }
    }
}