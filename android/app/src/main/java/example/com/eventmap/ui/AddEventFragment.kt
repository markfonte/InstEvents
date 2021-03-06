package example.com.eventmap.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import example.com.eventmap.MapsActivity
import example.com.eventmap.R
import example.com.eventmap.data.FirebaseStorageService
import example.com.eventmap.databinding.FragmentAddEventBinding
import example.com.eventmap.util.InjectorUtils
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_add_event.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*


class AddEventFragment : Fragment() {

    companion object {
        private final val PICK_IMAGE = 1
        private final val AUTOCOMPLETE_REQUEST_CODE = 2;
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

        Places.initialize(context!!, resources.getString(R.string.google_places_key))

        return binding.root
    }

    private fun openAddressAutocomplete() {
        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(context!!)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
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

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i(LOG_TAG, "Place: " + place.getName() + ", " + place.getId())
                Log.i(LOG_TAG, "Address: " + place.address)
                vm.address = place.address!!
                current_address.text = place.address
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i(LOG_TAG, status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
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
            val ref = firebaseStorage.getStorageRef("event-images/$imageName")
            firebaseStorage.uploadImage(ref, vm.image!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    vm.imageName = uri.toString()
                    sendData()
                }
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
        val locationEncoded = URLEncoder.encode(vm.address, "utf-8")
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

        enter_event_location_button.setOnClickListener {
            openAddressAutocomplete()
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