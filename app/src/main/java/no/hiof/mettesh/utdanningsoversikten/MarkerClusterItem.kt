package no.hiof.mettesh.utdanningsoversikten

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem



class MarkerClusterItem(val latLng: LatLng, val markerTitle: String = "", val snippetText: String = "", val icons: BitmapDescriptor): ClusterItem{
    override fun getSnippet(): String {
        return snippetText
    }

    override fun getTitle(): String {
        return markerTitle
    }

    override fun getPosition(): LatLng {
        return latLng
    }

     fun getICon(): BitmapDescriptor {
        return icons
    }
}