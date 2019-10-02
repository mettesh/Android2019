package no.hiof.mettesh.utdanningsoversikten.model

import no.hiof.mettesh.utdanningsoversikten.R

data class Education(val id : Int, var title : String, var descriptionShort : String, var descriptionLong : String, var school : School, var image : Int) {


    companion object {
        fun getEducation() : ArrayList<Education> {
            val data = ArrayList<Education>()

            /*
            val image = intArrayOf(
                R.drawable.sykepleie_background,
                R.drawable.ic_launcher_background

            )
             */

            for (i in 1..7){

                 val aEducation = Education(
                     i,
                     "Bachelor i sykepleie",
                     "En kort beskrivelse på utdanning",
                     "En laaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaang beskrivelse på utdanning",
                     School.getSchools().get(1),
                     R.drawable.sykepleie_background
                 )

                data.add(aEducation)

            }

            return data
        }
    }

}