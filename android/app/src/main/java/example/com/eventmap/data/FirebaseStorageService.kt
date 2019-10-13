package example.com.eventmap.data

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*

class FirebaseStorageService {

    companion object {
        private val LOG_TAG: String = FirebaseStorageService::class.java.name

        @Volatile
        private var instance: FirebaseStorageService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FirebaseStorageService().also { instance = it }
            }
    }

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    fun getStorageRef(path: String): StorageReference {
        return storageRef.child(path);
    }

    fun uploadImage(ref: StorageReference, data: ByteArray): UploadTask {
        return ref.putBytes(data)
    }

    fun uploadImage(ref: StorageReference, bitmap: Bitmap): UploadTask {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        return uploadImage(ref, data)
    }
}