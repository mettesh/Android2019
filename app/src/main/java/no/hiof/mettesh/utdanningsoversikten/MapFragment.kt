package no.hiof.mettesh.utdanningsoversikten


import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import pub.devrel.easypermissions.EasyPermissions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
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
        placeMarkersOnMap()

        // Setter stil på kartet
        gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context!!, R.raw.style_json))
    }

    private fun placeMarkersOnMap() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_school_yellow_24dp)
        // Markerer alle skolene
        // TODO: Legge til flere skoler
        gmap.addMarker(MarkerOptions()
            .position(HIOFH)
            .title("Høgskolen i Østfold")
            .snippet("Høgskolen i Østfold er kjempestor og har masse elever og du kan gjøre masse ting her. Les mer her '<a href='hiof.no>Hiof.no</a>'")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIOFF)
            .title("Høgskolen i Østfold - avd. Fredrikstad")
            .snippet("Tralala")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))
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

        // Hvor kartet skal starte
        val latLng =LatLng(60.18523283, 10.16784668)
        val zoomLevel = 7.0f

        // Flytter kameraet til denne posisjonen
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))

        // Kompass og zoom-knapper på kartet
        gmap.uiSettings.isCompassEnabled = true
        gmap.uiSettings.isZoomControlsEnabled = true
    }

    companion object{
        private const val LOCATION_PERMISSION_ID = 1
        private val HIOFH = LatLng(59.12915265, 11.354157)
        private val HIOFF = LatLng(59.2127991, 10.93019032)
        private val HIOFHID = "ChIJbyRE0_ASREYRsVOymZzxR04"


    }

}


