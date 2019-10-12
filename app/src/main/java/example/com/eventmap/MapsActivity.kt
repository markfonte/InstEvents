package example.com.eventmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import example.com.eventmap.databinding.ActivityMapsBinding
import example.com.eventmap.util.InjectorUtils

class MapsActivity : AppCompatActivity() {

    private lateinit var vm: MapsActivityViewModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory: MapsActivityViewModelFactory =
            InjectorUtils.provideMapsActivityViewModelFactory()
        vm = ViewModelProviders.of(this, factory).get(MapsActivityViewModel::class.java)
        val binding: ActivityMapsBinding =
            DataBindingUtil.setContentView<ActivityMapsBinding>(this, R.layout.activity_maps)
                .apply {
                    viewModel = vm
                    lifecycleOwner = this@MapsActivity
                }

        val navHost: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment?
                ?: return

        navController = navHost.navController
    }


}
