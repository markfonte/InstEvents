package example.com.eventmap.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import example.com.eventmap.MapsActivity
import example.com.eventmap.R
import example.com.eventmap.databinding.FragmentAddEventBinding
import example.com.eventmap.util.InjectorUtils
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_add_event.*
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import example.com.eventmap.data.FirebaseStorageService
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*


class AddEventFragment : Fragment() {

    companion object {
        private final val PICK_IMAGE = 1
        private final val LOG_TAG = "AddEventFragment"
    }

    private val firebaseStorage: FirebaseStorageService = FirebaseStorageService.getInstance()

    private lateinit var vm: AddEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory: AddEventViewModelFactory = InjectorUtils.provideAddEventViewModelFactory()
        vm = ViewModelProviders.of(this, factory).get(AddEventViewModel::class.java)
        val binding: FragmentAddEventBinding = DataBindingUtil.inflate<FragmentAddEventBinding>(
            inflater,
            R.layout.fragment_add_event,
            container,
            false
        ).apply {
            viewModel = vm
            lifecycleOwner = this@AddEventFragment
        }
        return binding.root
    }


    private fun showStartTimeDialog() {
        vm.isPickerLoading.value = true
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
                run {
                    vm.startHour = hourOfDay
                    vm.startMinute = minutes
                    var hourOutput: String = hourOfDay.toString()
                    var minuteOutput: String = minutes.toString()

                    if (hourOfDay > 12) {
                        hourOutput = (hourOfDay - 12).toString()
                    }
                    if (hourOfDay == 0) {
                        hourOutput = "12"
                    }
                    if (minutes < 10) { // single digit
                        minuteOutput = "0$minutes"
                    }
                    val suffix = if (hourOfDay >= 12) "p.m." else "a.m."
                    enter_event_start_time.setText("$hourOutput:$minuteOutput $suffix")
                }
            },
            12,
            0,
            false
        )
        timePickerDialog.setOnShowListener {
            vm.isPickerLoading.value = false
        }
        timePickerDialog.show()
    }

    private fun showEndTimeDialog() {
        vm.isPickerLoading.value = true
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
                run {
                    vm.endHour = hourOfDay
                    vm.endMinute = minutes
                    var hourOutput: String = hourOfDay.toString()
                    var minuteOutput: String = minutes.toString()

                    if (hourOfDay > 12) {
                        hourOutput = (hourOfDay - 12).toString()
                    }
                    if (hourOfDay == 0) {
                        hourOutput = "12"
                    }
                    if (minutes < 10) { // single digit
                        minuteOutput = "0$minutes"
                    }
                    val suffix = if (hourOfDay >= 12) "p.m." else "a.m."
                    enter_event_end_time.setText("$hourOutput:$minuteOutput $suffix")
                }
            },
            12,
            0,
            false
        )
        timePickerDialog.setOnShowListener {
            vm.isPickerLoading.value = false
        }
        timePickerDialog.show()
    }

    private fun showStartDateDialog() {
        vm.isPickerLoading.value = true
        val datePickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                run {
                    vm.startYear = year
                    vm.startMonth = month + 1
                    vm.startDay = day
                    enter_event_start_date.setText("${month + 1}/$day/$year")

                }
            },
            2019,
            9,
            13
        )
        datePickerDialog.setOnShowListener {
            vm.isPickerLoading.value = false
        }
        datePickerDialog.show()
    }

    private fun showEndDateDialog() {
        vm.isPickerLoading.value = true
        val datePickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                run {
                    vm.endYear = year
                    vm.endMonth = month + 1
                    vm.endDay = day
                    enter_event_end_date.setText("${month + 1}/$day/$year")

                }
            },
            2019,
            9,
            13
        )
        datePickerDialog.setOnShowListener {
            vm.isPickerLoading.value = false
        }
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Log.e(LOG_TAG, "Unable to select photo, returned data is null")
                return
            }
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.activity?.contentResolver, uri)
            new_event_image.setImageBitmap(bitmap)
            vm.image = bitmap
        }
    }

    private fun pickImage() {
        val getIntent = Intent(Intent.ACTION_PICK)
        getIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Choose image using")

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private fun submit() {
        if (vm.image != null) {
            val imageName = UUID.randomUUID().toString() + ".png"
            val path = "event-images/$imageName"
            firebaseStorage.uploadImage(path, vm.image!!).addOnSuccessListener { snapshot ->
                vm.imageName = snapshot.storage.downloadUrl.toString()
                sendData()

            }.addOnFailureListener {
                Log.e(LOG_TAG, "Unable to upload image!")
            }
        } else {
            sendData()
        }
    }

    private fun sendData() {
        val queue = Volley.newRequestQueue(context)
        val apiKey =
            URLEncoder.encode(getString(R.string.google_maps_geocoding_api_key), "utf-8")
        val locationEncoded = URLEncoder.encode(enter_event_location.text.toString(), "utf-8")
        val addEventUrl =
            "https://maps.googleapis.com/maps/api/geocode/json?address=$locationEncoded&key=$apiKey"

        val request = JsonObjectRequest(
            Request.Method.GET, addEventUrl, null,
            Response.Listener { response ->

                val lat: Double =
                    (((response["results"] as JSONArray).getJSONObject(0).get("geometry") as JSONObject).get(
                        "location"
                    ) as JSONObject).get("lat") as Double
                val lng: Double =
                    (((response["results"] as JSONArray).getJSONObject(0).get("geometry") as JSONObject).get(
                        "location"
                    ) as JSONObject).get("lng") as Double

                vm.addEvent(
                    enter_event_title.text.toString(),
                    enter_event_description.text.toString(),
                    enter_event_location.text.toString(),
                    context!!,
                    lat,
                    lng
                ).observe(this, Observer { success ->
                    if (success) {
                        main_nav_host_fragment.view?.let { it1 ->
                            Snackbar.make(
                                it1,
                                "Event created! Check it out on the map or in Browse.",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        vm.isPickerLoading.value = false
                        (activity as MapsActivity).navController.navigate(
                            R.id.action_addEventFragment_to_supportMapsFragment,
                            null
                        )
                    }
                    vm.isPickerLoading.value = false
                })

            },
            Response.ErrorListener { error -> Log.e("Tag", "That didnt work $error") })

        // Add the request to the RequestQueue.
        queue.add(request)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enter_event_start_time.setOnClickListener {
            showStartTimeDialog()
        }
        enter_event_start_time.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                showStartTimeDialog()
            }
        }
        enter_event_end_time.setOnClickListener {
            showEndTimeDialog()
        }
        enter_event_end_time.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                showEndTimeDialog()
            }
        }
        enter_event_start_date.setOnClickListener {
            showStartDateDialog()
        }
        enter_event_start_date.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                showStartDateDialog()
            }
        }
        enter_event_end_date.setOnClickListener {
            showEndDateDialog()
        }
        enter_event_end_date.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                showEndDateDialog()
            }
        }
        enter_event_submit.setOnClickListener {
            vm.isPickerLoading.value = true
            submit()
        }

        enter_event_choose_picture.setOnClickListener {
            pickImage()
        }
    }
}