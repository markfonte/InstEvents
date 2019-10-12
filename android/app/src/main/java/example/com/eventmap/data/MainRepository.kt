package example.com.eventmap.data

class MainRepository {
    private var firebaseService: FirebaseService = FirebaseService.getInstance()


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MainRepository().also { instance = it }
            }
    }
}