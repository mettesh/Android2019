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

        gmap.addMarker(MarkerOptions()
            .position(NTNU)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NTNUG)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NTNUAA)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NMBU)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UIB)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UIO)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UIS)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UIT)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UIAK)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UIAG)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NORD)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(OSLOMETK)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(OSLOMETP)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISB)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISD)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISK)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISN)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISP)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISR)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(UISV)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(AHO)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(KHIO)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NHH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NIH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(NMH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIM)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(SAM)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HVLB)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HVLF)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HVLH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HVLSO)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HVLST)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIHMB)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIHMELV)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIHME)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIHMH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIHML)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIHMR)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIV)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIOH)
            .title("")
            .snippet("")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_school_black_36)))

        gmap.addMarker(MarkerOptions()
            .position(HIOF)
            .title("")
            .snippet("")
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
        private val NTNU = LatLng(63.417844, 10.403797)
        private val NTNUG = LatLng(60.789962, 10.682665)
        private val NTNUAA = LatLng(62.472123, 6.235385)

        private val NMBU = LatLng(59.666416, 10.767846)

        private val UIB = LatLng(60.387668, 5.321744)

        private val UIO = LatLng(59.939925, 10.721873)

        private val UIS = LatLng(58.937476, 5.697469)

        private val UIT = LatLng(69.679777, 18.971241)

        private val UIAK = LatLng(58.163611, 8.002846)
        private val UIAG = LatLng(58.334340, 8.576818)

        private val NORD = LatLng(67.289168, 14.560699)

        private val OSLOMETK = LatLng(59.976376, 11.044570)
        private val OSLOMETP = LatLng(59.921130, 10.733170)

        private val UISB = LatLng(59.408895, 9.059572)
        private val UISD = LatLng(59.743784, 10.193702)
        private val UISK = LatLng(59.664448, 9.644371)
        private val UISN = LatLng(59.568442, 9.279572)
        private val UISP = LatLng(59.138689, 9.672193)
        private val UISR = LatLng(59.696377, 8.042888)
        private val UISH = LatLng(60.152887, 10.262749)
        private val UISV = LatLng(59.368648, 10.441417)

        private val AHO = LatLng(59.924515, 10.750983)

        private val KHIO = LatLng(59.918011, 10.737524)

        private val NHH = LatLng(60.422795, 5.302613)

        private val NIH = LatLng(59.966753, 10.731697)

        private val NMH = LatLng(59.932358, 10.714163)

        private val HIM = LatLng(62.736480, 7.110027)

        private val SAM = LatLng(69.013691, 23.038820)

        private val HVLB = LatLng(60.369099, 5.349931)
        private val HVLF = LatLng(61.458922, 5.888601)
        private val HVLH = LatLng(59.409055, 5.276031)
        private val HVLSO = LatLng(61.230305, 7.088607)
        private val HVLST = LatLng(59.816295, 5.526238)

        private val HIHMB = LatLng(60.819569, 11.180326)
        private val HIHMELV = LatLng(60.881167, 11.536417)
        private val HIHME = LatLng(61.425459, 11.078007)
        private val HIHMH = LatLng(60.796368, 11.074072)
        private val HIHML = LatLng(61.150066, 10.422659)
        private val HIHMR = LatLng(61.136061, 11.373893)

        private val HIV = LatLng(62.147994, 6.082185)

        private val HIOH = LatLng(59.128834, 11.353271)
        private val HIOF = LatLng(59.212841, 10.930491)



    }

}


