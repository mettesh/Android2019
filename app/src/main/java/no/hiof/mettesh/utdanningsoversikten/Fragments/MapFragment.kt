package no.hiof.mettesh.utdanningsoversikten.Fragments

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterItem
import org.json.JSONArray
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import com.google.maps.android.clustering.ClusterManager
import no.hiof.mettesh.utdanningsoversikten.Utils.MarkerClusterItem
import no.hiof.mettesh.utdanningsoversikten.R

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var gmap: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterItem>
    private lateinit var jsonArray: JSONArray
    private val markerList = mutableListOf<MarkerOptions>()

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private var hasLocation = false
    private var isFirstRun = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        jsonArray = readAssets()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        return view
    }

    private fun readAssets(): JSONArray {
        val places = "places.json"
        val json_string = context?.assets?.open(places)
            ?.bufferedReader()
            ?.readText()
        return JSONArray(json_string)
    }

    private fun addMarkers() {
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            val name = jsonObject.getString("name")
            val lat = jsonObject.getDouble("lat")
            val lng = jsonObject.getDouble("lng")
            val snippet = jsonObject.getString("snippet")
            val icon = jsonObject.getString("icon")

            val resID = resources.getIdentifier(
                icon,
                "drawable", activity!!.packageName
            )

            val marker = MarkerOptions().position(LatLng(lat, lng)).title(name).snippet(snippet).icon(
                BitmapDescriptorFactory.fromResource(resID))

            markerList.add(marker)
        }
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerList[0].position, 6.0f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap

        clusterManager = ClusterManager(context, googleMap)

        addMarkers()

        setupUISettings()
        setUpClusterManager()

        checkPosition()

        // Setter stil på kartet
        gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context!!,
            R.raw.style_json
        ))
    }

    private fun checkPosition() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {

                hasLocation = true

            } else {
                showAlertBox("Finner ikke lokasjon", "Posisjon kan ikke hentes", "Ok")
            }
        }
    }

    private fun setUpClusterManager() {
        addClusters()
        gmap.setOnCameraIdleListener(clusterManager)
        gmap.setOnMarkerClickListener(clusterManager)

        clusterManager.cluster()
    }

    private fun addClusters() {
        for (marker in markerList) {
            val clusterItem = MarkerClusterItem(
                marker.position,
                marker.title,
                marker.snippet,
                marker.icon
            )
            clusterManager.addItem(clusterItem)
        }
    }

        //Setter opp UI etter at bruker har godkjent bruk av lokasjon
        @AfterPermissionGranted(LOCATION_PERMISSION_ID)
        private fun setupUISettings() {
            if (EasyPermissions.hasPermissions(
                    context!!,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                gmap.isMyLocationEnabled = true
                gmap.uiSettings.isMyLocationButtonEnabled = true

            } else {
                EasyPermissions.requestPermissions(
                    this, "Vi trenger godkjenning for å vise din lokasjon på kartet",
                    LOCATION_PERMISSION_ID, android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

            // Hvor kartet skal starte
            val start = LatLng(68.437418, 17.428516)
            val zoomLevel = 3.6f
            // Flytter kameraet til denne posisjonen
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoomLevel))

            // Kompass og zoom-knapper på kartet
            gmap.uiSettings.isCompassEnabled = true
            gmap.uiSettings.isZoomControlsEnabled = true
        }

    private fun showAlertBox(title: String, message: String, buttonText: String) {
        val alertBox = AlertDialog.Builder(this.context!!,
            R.style.AlertDialogTheme
        )

        alertBox.setTitle(title)
        alertBox.setMessage(message)

        alertBox.setPositiveButton(buttonText) { dialog, which ->
            isFirstRun = false
        }
        val alert = alertBox.create()
        alert.show()
    }

        companion object {
            private const val LOCATION_PERMISSION_ID = 1
        }
    }


