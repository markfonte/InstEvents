package example.com.eventmap.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class VolleyService {

    fun addEvent(
        title: String,
        description: String,
        startDate: String,
        endDate: String,
        location: String,
        context: Context,
        latitude: Double,
        longitude: Double
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
                    eventInfoMap["latitude"] = latitude.toString()
                    eventInfoMap["longitude"] = longitude.toString()
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

    fun getTodaysEvents(context: Context): MutableLiveData<ArrayList<HashMap<String, String>>> {
        val result: MutableLiveData<ArrayList<HashMap<String, String>>> = MutableLiveData()

        val queue = Volley.newRequestQueue(context)
        val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.getDefault())
        val startDate = sdf.format(Date())
        val endDate = startDate.substring(0, 10) + "-23-59"

        val addEventUrl =
            "https://us-central1-infinite-chain-255705.cloudfunctions.net/api/events?start_date=$startDate&end_date=$endDate"
        val events: ArrayList<HashMap<String, String>> = arrayListOf()
        val or = JsonObjectRequest(Request.Method.GET, addEventUrl, null,
            Response.Listener { response ->
                Log.d(LOG_TAG, "Response: %s".format(response.toString()))
                for (i in response.keys()) {
                    val event: HashMap<String, String> = HashMap()
                    event["start_date"] = (response[i] as JSONObject)["start_date"].toString()
                    event["end_date"] = (response[i] as JSONObject)["end_date"].toString()
                    event["location"] = (response[i] as JSONObject)["location"].toString()
                    event["title"] = (response[i] as JSONObject)["title"].toString()
                    event["image_url"] = (response[i] as JSONObject)["image_url"].toString()
                    event["description"] = (response[i] as JSONObject)["description"].toString()
                    event["latitude"] = (response[i] as JSONObject)["latitude"].toString()
                    event["longitude"] = (response[i] as JSONObject)["longitude"].toString()
                    events.add(event)
                }
                result.value = events
            },
            Response.ErrorListener { error -> Log.e("Tag", "That didnt work $error") })

        // Add the request to the RequestQueue.
        queue.add(or)

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