package no.hiof.mettesh.utdanningsoversikten.model

data class Education(val id : Int = 0,
                     val educationCode : String = "",
                     val QualificationCode : String = "",
                     var educationTitle : String = "",
                     var descriptionShort : String = "",
                     var descriptionLong : String = "",
                     var school : School = School(),
                     var image : String = "",
                     var pointsRequired : Double = 0.0,
                     var pointsAcquired : Double = 0.0)
{

    companion object {

        val educationlist = ArrayList<Education>()
        var favouriteEducationlist = ArrayList<Education>()

        /*
        fun fillEducationList() {

            for (i in 0..9){

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
                     School.schoolList.get(1),
                     "https://www.hiof.no/om/aktuelt/aktuelle-saker/arkiv/filer/sykepleierfotvaskBH_654.jpg",
                     34.5
                 )

                educationlist.add(aEducation)

            }
        }
         */
    }

}