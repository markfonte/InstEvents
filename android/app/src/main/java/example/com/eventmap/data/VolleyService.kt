package example.com.eventmap.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class VolleyService {

    fun addEvent(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        location: String,
        context: Context
    ): MutableLiveData<Boolean> {
        val result: MutableLiveData<Boolean> = MutableLiveData()

        val queue = Volley.newRequestQueue(context)
        val addEventUrl = "https://us-central1-infinite-chain-255705.cloudfunctions.net/api/events"

        val sr =
            object : StringRequest(Method.POST, addEventUrl,
                Response.Listener { result.value = true },
                Response.ErrorListener { error -> Log.e("Tag", "That didnt work $error") }) {
                override fun getParams(): Map<String, String> {
                    val eventInfoMap = HashMap<String, String>()
                    eventInfoMap["start_date"] = startDate
                    eventInfoMap["end_date"] = endDate
                    eventInfoMap["description"] = description
                    eventInfoMap["location"] = location
                    eventInfoMap["title"] = title
                    eventInfoMap["image_url"] = ""
                    return eventInfoMap
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["Content-Type"] = "application/x-www-form-urlencoded"
                    return params
                }
            }

        // Add the request to the RequestQueue.
        queue.add(sr)

        return result
    }


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