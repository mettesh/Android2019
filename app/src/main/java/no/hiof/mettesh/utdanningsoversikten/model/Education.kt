package no.hiof.mettesh.utdanningsoversikten.model

import no.hiof.mettesh.utdanningsoversikten.R
import java.net.URL

data class Education(val id : Int, val kravkode : String, var title : String, var descriptionShort : String, var descriptionLong : String, var school : School, var image : String, var poenggrense : Double) {


    companion object {
        fun getEducation() : ArrayList<Education> {
            val educationlist = ArrayList<Education>()

            for (i in 1..10){

                 val aEducation = Education(
                     i,
                     "NO3MA3",
                     "Bachelor i sykepleie",
                     "Er du glad i å samarbeide med mennesker? Liker du variasjon og utfordringer " +
                             "og ønsker å være sikret en trygg og meningsfylt jobb?",
                     "Sykepleie handler om å møte mennesker i ulike stadier av helse; mennesker som trenger " +
                             "hjelp og støtte til å mestre helseutfordringer, sykdom og å ivareta sine grunnleggende behov. " +
                             "Viktige temaer i utdanningen er helse, sykdom, ivaretakelse av grunnleggende behov, mestring, " +
                             "kommunikasjon, samarbeid, og omsorg.",
                     School.getSchools().get(1),
                     "https://www.hiof.no/om/aktuelt/aktuelle-saker/arkiv/filer/sykepleierfotvaskBH_654.jpg",
                     34.5
                 )

                educationlist.add(aEducation)

            }

            return educationlist
        }


        fun getFavouriteEducations() : ArrayList<Education> {
            val educationlist = ArrayList<Education>()

            educationlist.add(getEducation().get(0))
            educationlist.add(getEducation().get(1))
            educationlist.add(getEducation().get(2))

            return educationlist
        }
    }

}