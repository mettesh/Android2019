package no.hiof.mettesh.utdanningsoversikten.model

import no.hiof.mettesh.utdanningsoversikten.R

data class Education(val id : Int, var title : String, var description : String, /*var school : School,*/ var image : Int) {


    companion object {
        fun getEducation() : ArrayList<Education> {
            val data = ArrayList<Education>()

            val image = intArrayOf(
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background

            )

            val titles = arrayOf(
                "Captain America Civil War",
                "Donnie Darko"
            )

            titles.forEachIndexed { index, title ->
                val aEducation = Education(index, title, title + " er en utdanning", image.get(index))

                data.add(aEducation)
            }

            return data
        }
    }

}