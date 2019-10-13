package example.com.eventmap.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.protobuf.Parser
import org.json.JSONObject


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

    fun getTodaysEvents(context: Context): MutableLiveData<ArrayList<HashMap<String, String>>> {
        val result: MutableLiveData<ArrayList<HashMap<String, String>>> = MutableLiveData()

        val queue = Volley.newRequestQueue(context)
        val startDate = "2019-10-15-00-00"
        val endDate = "2019-10-17-23-59"

        val addEventUrl = "https://us-central1-infinite-chain-255705.cloudfunctions.net/api/events?start_date=$startDate&end_date=$endDate"
        val events : ArrayList<HashMap<String, String>> = arrayListOf()
        val or = JsonObjectRequest(Request.Method.GET, addEventUrl, null,
                Response.Listener {response ->
                    Log.d(LOG_TAG, "Response: %s".format(response.toString()))
                    for (i in response.keys()) {
                        val event : HashMap<String, String> = HashMap()
                        event["start_date"] = (response[i] as JSONObject)["start_date"].toString()
                        event["end_date"] = (response[i] as JSONObject)["end_date"].toString()
                        event["location"] = (response[i] as JSONObject)["location"].toString()
                        event["title"] = (response[i] as JSONObject)["title"].toString()
                        event["image_url"] = (response[i] as JSONObject)["image_url"].toString()
                        event["description"] = (response[i] as JSONObject)["description"].toString()
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