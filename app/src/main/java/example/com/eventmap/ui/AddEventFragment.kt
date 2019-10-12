package example.com.eventmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import example.com.eventmap.R
import example.com.eventmap.databinding.FragmentAddEventBinding
import example.com.eventmap.util.InjectorUtils

class AddEventFragment : Fragment() {

    private lateinit var vm: AddEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory: AddEventViewModelFactory = InjectorUtils.provideAddEventViewModelFactory()
        vm = ViewModelProviders.of(this, factory).get(AddEventViewModel::class.java)
        val binding: FragmentAddEventBinding = DataBindingUtil.inflate<FragmentAddEventBinding>(inflater, R.layout.fragment_add_event, container, false).apply {
            viewModel = vm
            lifecycleOwner = this@AddEventFragment
        }
        return binding.root
    }
}