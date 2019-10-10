package no.hiof.mettesh.utdanningsoversikten.model

import no.hiof.mettesh.utdanningsoversikten.R

data class School(val schoolId : Int, var schoolTitle : String, var schoolIcon : String, var schoolAdress : String,
                  var longLat : String, var web : String ) {


    companion object {
        fun getSchools() : ArrayList<School> {
            val data = ArrayList<School>()


            for (i in 0..9) {
               val aSchool = School(
                   i,
                   "Høyskolen i Østfold",
                   "https://upload.wikimedia.org/wikipedia/en/d/da/Hi%C3%98_emblem.png",
                   "B R A Veien 4, 1783 Halden",
                   "59.1288539,11.3532008,15",
                   "www.hiof.no")

                data.add(aSchool)
            }

            return data
        }
    }

}