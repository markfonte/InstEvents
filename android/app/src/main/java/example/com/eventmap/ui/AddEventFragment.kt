package example.com.eventmap.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import example.com.eventmap.R
import example.com.eventmap.databinding.FragmentAddEventBinding
import example.com.eventmap.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_add_event.*


class AddEventFragment : Fragment() {

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
            0,
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
            0,
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
        enter_event_submit.setOnClickListener{
            vm.addEvent(enter_event_title.text.toString(), enter_event_description.text.toString(), enter_event_location.text.toString())
        }
    }
}