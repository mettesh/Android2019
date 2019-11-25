package no.hiof.mettesh.utdanningsoversikten.Models

data class Education(val id : Int = 0,
                     val educationCode : String = "",
                     var educationTitle : String = "",
                     var descriptionShort : String = "",
                     var descriptionLong : String = "",
                     var school : School = School(),
                     var image : String = "",
                     var pointsRequired : Double = 0.0,
                     var pointsAcquired : Double = 0.0,
                     var level : String = "",
                     var studyField : String = "")
{

    companion object {

        val educationlist = ArrayList<Education>()
        var favouriteEducationlist = ArrayList<Education>()


        fun getNumbersOfEducationInList() : Int {
            return educationlist.size
        }

        fun getNumbersOfEducationInFavouriteList() : Int {
            return favouriteEducationlist.size
        }

    }
}