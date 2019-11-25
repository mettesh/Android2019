package no.hiof.mettesh.utdanningsoversikten.Models

data class School(val schoolCode : Int = 0,
                  var schoolTitle : String = "",
                  var schoolShortTitle : String = "",
                  var schoolAdress : String = "",
                  var schoolZipCode : String = "",
                  var schoolPhoneNumber : Int = 0,
                  var longLat : String = "",
                  var webPage : String = "",
                  var place : String = "")
{

    companion object {

        val schoolList = ArrayList<School>()

    }
}