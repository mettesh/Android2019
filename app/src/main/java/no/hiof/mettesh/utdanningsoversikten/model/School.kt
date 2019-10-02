package no.hiof.mettesh.utdanningsoversikten.model

import no.hiof.mettesh.utdanningsoversikten.R

data class School(val schoolId : Int, var schoolTitle : String, var schoolIcon : Int, var schoolAdress : String,
                  var longLat : String, var web : String ) {


    companion object {
        fun getSchools() : ArrayList<School> {
            val data = ArrayList<School>()


            for (i in 1..10) {
               val aSchool = School(
                   i,
                   "Høyskolen i Østfold",
                   R.drawable.hiof_icon_background,
                   "B R A Veien 4, 1783 Halden",
                   "59.1288539,11.3532008,15",
                   "www.hiof.no")

                data.add(aSchool)
            }

            return data
        }
    }

}