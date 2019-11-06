package no.hiof.mettesh.utdanningsoversikten.model

data class School(val schoolCode : Int = 0,
                  var schoolTitle : String = "",
                  var schoolShortTitle : String = "",
                  var schoolIcon : String = "",
                  var schoolAdress : String = "",
                  var schoolZipCode : String = "",
                  var schoolPhoneNumber : Int = 0,
                  var longLat : String = "",
                  var webPage : String = "",
                  var place : String = "") {


    companion object {

        val schoolList = ArrayList<School>()


        fun fillSchoolList() {
            val data = ArrayList<School>()


            for (i in 0..9) {
               val aSchool = School(
                   i,
                   "Høyskolen i Østfold",
                   "https://upload.wikimedia.org/wikipedia/en/d/da/Hi%C3%98_emblem.png",
                   "B R A Veien 4, 1783 Halden",
                   "59.1288539,11.3532008,15",
                   "http://www.hiof.no")

                schoolList.add(aSchool)
            }
        }
    }

}