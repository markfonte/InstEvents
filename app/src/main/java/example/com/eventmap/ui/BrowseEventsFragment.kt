package example.com.eventmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import example.com.eventmap.R
import example.com.eventmap.databinding.FragmentBrowseEventsBinding
import example.com.eventmap.util.InjectorUtils

class BrowseEventsFragment : Fragment() {

    private lateinit var vm: BrowseEventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory: BrowseEventsViewModelFactory = InjectorUtils.provideBrowseEventsViewModelFactory()
        vm = ViewModelProviders.of(this, factory).get(BrowseEventsViewModel::class.java)
        val binding: FragmentBrowseEventsBinding = DataBindingUtil.inflate<FragmentBrowseEventsBinding>(
            inflater,
            R.layout.fragment_browse_events,
            container,
            false
        ).apply {
            viewModel = vm
            lifecycleOwner = this@BrowseEventsFragment
        }
        return binding.root
    }
}