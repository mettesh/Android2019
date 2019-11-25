package no.hiof.mettesh.utdanningsoversikten.Models

data class Place(var zipCode : String = "",
                  var place : String = "")
{

    companion object {

        val zipCodeAndPlaceList = ArrayList<Place>()

    }
}