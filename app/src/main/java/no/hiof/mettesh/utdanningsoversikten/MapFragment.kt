package no.hiof.mettesh.utdanningsoversikten


import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import pub.devrel.easypermissions.EasyPermissions
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*
import no.hiof.mettesh.utdanningsoversikten.MapFragment.Companion.LOCATION_PERMISSION_ID
import pub.devrel.easypermissions.AfterPermissionGranted
import java.util.jar.Manifest

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var gmap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        
        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap

        setupUISettings()

    }


    //Setter opp UI etter at bruker har godkjent bruk av lokasjon
    @AfterPermissionGranted(LOCATION_PERMISSION_ID)
    private fun setupUISettings(){
        if (EasyPermissions.hasPermissions(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            gmap.isMyLocationEnabled = true
            gmap.uiSettings.isMyLocationButtonEnabled = true
        }
        else {
            EasyPermissions.requestPermissions(this, "Vi trenger godkjenning for å vise din lokasjon på kartet",
                LOCATION_PERMISSION_ID, android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        // Kompass og zoom-knapper på kartet
        gmap.uiSettings.isCompassEnabled = true
        gmap.uiSettings.isZoomControlsEnabled = true
    }

    companion object{
        private const val LOCATION_PERMISSION_ID = 1
    }

}


