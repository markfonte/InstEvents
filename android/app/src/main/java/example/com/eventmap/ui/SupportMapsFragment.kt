package example.com.eventmap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import example.com.eventmap.R
import example.com.eventmap.databinding.FragmentSupportMapBinding
import example.com.eventmap.util.InjectorUtils

class SupportMapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var vm: SupportMapsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val factory: SupportMapsViewModelFactory =
            InjectorUtils.provideSupportMapsViewModelFactory()
        vm = ViewModelProviders.of(this, factory).get(SupportMapsViewModel::class.java)
        val binding: FragmentSupportMapBinding = DataBindingUtil.inflate<FragmentSupportMapBinding>(
            inflater,
            R.layout.fragment_support_map,
            container,
            false
        ).apply {
            viewModel = vm
            lifecycleOwner = this@SupportMapsFragment
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        (childFragmentManager.fragments[0] as SupportMapFragment).getMapAsync(this)
    }

    private fun addMarker(lat: Double, lng: Double, title: String, description: String) {
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title(title)
                .snippet(description)
        )
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        vm.getEventsToday(context!!).observe(this, Observer { response ->
            for (i in response) {
                i["latitude"]?.toDouble()?.let {
                    i["longitude"]?.toDouble()?.let { it1 ->
                        i["title"]?.let { it2 ->
                            i["description"]?.let { it3 ->
                                addMarker(
                                    it,
                                    it1, it2, it3
                                )
                            }
                        }
                    }
                }

            }
        })
        // Add a marker in Sydney and move the camera


        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mMap.isMyLocationEnabled = true
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                // Got last known location. In some rare situations this can be null.
                val curLocation = location?.latitude?.let { LatLng(it, location.longitude) }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 14.6f))
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    mMap.isMyLocationEnabled = true
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}