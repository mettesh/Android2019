package no.hiof.mettesh.utdanningsoversikten

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import org.json.JSONArray
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import com.google.maps.android.clustering.ClusterManager

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var gmap: GoogleMap
    private lateinit var clusterManager: ClusterManager<ClusterItem>
    private lateinit var jsonArray: JSONArray
    private val markerList = mutableListOf<MarkerOptions>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        jsonArray = readAssets()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
            //val icon = jsonObject.getString("icon").toInt()
            val marker = MarkerOptions().position(LatLng(lat, lng)).title(name).snippet(snippet)
            markerList.add(marker)
        }
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerList[0].position, 10.0f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap

        clusterManager = ClusterManager(context, googleMap)

        addMarkers()

        setupUISettings()
        //placeMarkersOnMap()
        setUpClusterManager()

        // Setter stil på kartet
        gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context!!, R.raw.style_json))
    }

    private fun setUpClusterManager() { // Position the map.
        addClusters()
        // Point the map's listeners at the listeners implemented by the cluster
// manager.
        gmap.setOnCameraIdleListener(clusterManager)
        gmap.setOnMarkerClickListener(clusterManager)
        // Add cluster items (markers) to the cluster manager.
        clusterManager.cluster()
    }

    private fun addClusters() {
        for (marker in markerList) {
            val clusterItem = MarkerClusterItem(marker.position, marker.title)
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
            val latLng = LatLng(60.18523283, 10.16784668)
            val zoomLevel = 7.0f

            // Flytter kameraet til denne posisjonen
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))

            // Kompass og zoom-knapper på kartet
            gmap.uiSettings.isCompassEnabled = true
            gmap.uiSettings.isZoomControlsEnabled = true
        }

        companion object {
            private const val LOCATION_PERMISSION_ID = 1
        }
    }


