package no.hiof.mettesh.utdanningsoversikten.model

data class Place(var zipCode : String = "",
                  var place : String = "") {


    companion object {

        val zipCodeAndPlaceList = ArrayList<Place>()

    }

}