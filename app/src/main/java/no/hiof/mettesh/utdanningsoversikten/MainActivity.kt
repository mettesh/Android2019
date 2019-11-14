package no.hiof.mettesh.utdanningsoversikten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.hiof.mettesh.utdanningsoversikten.model.Education
import no.hiof.mettesh.utdanningsoversikten.model.School
import org.json.JSONArray
import com.opencsv.CSVReader
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readSchoolJsonFile()
        readEducationJsonFile()

        // Referanse til navController
        var navController = findNavController(R.id.listFragment)

        // Setter opp bottom navigation
        setupBottomNavMenu(navController)
    }

    private fun readSchoolJsonFile() {

        var json : String? = null

        try {
            val inputStream : InputStream = assets.open("institusjon.json")
            json = inputStream.bufferedReader().use { it.readText()  }

            var jsonArray = JSONArray(json)

            for(i in 0..jsonArray.length()-1){

                val jsonSchoolObject = jsonArray.getJSONObject(i)

                val schoolCode = (jsonSchoolObject.get("Institusjonskode") as String).toInt()
                val schoolTitle = jsonSchoolObject.get("Institusjonsnavn").toString()
                val schoolShortTitle = jsonSchoolObject.get("Kortnavn").toString()
                val schoolAdress = jsonSchoolObject.get("Adresse").toString()
                val schoolZipCode = jsonSchoolObject.get("Postnummer").toString()
                var schoolPhoneNumber = jsonSchoolObject.get("Telefon")
                val webPage = jsonSchoolObject.get("Nettside").toString()
                var place = getPlaceCSVFileBasedOnZipCode(schoolZipCode)

                if( schoolPhoneNumber.equals(null) || schoolPhoneNumber.equals("")){
                    schoolPhoneNumber = 0
                }
                else {
                    schoolPhoneNumber = (jsonSchoolObject.get("Telefon") as String).toInt()
                }


                val newSchool = School(
                    schoolCode,
                    schoolTitle,
                    schoolShortTitle,
                    schoolAdress,
                    schoolZipCode,
                    schoolPhoneNumber,
                    "59.1288539,11.3532008,15", // Mangler i JSON
                    webPage,
                    place
                )

                if(!School.schoolList.contains(newSchool)){
                    School.schoolList.add(newSchool)
                }
            }

        }
        catch (e : IOException) {

        }
    }

    private fun readEducationJsonFile() {
        var json : String? = null

        try {
            val inputStream : InputStream = assets.open("studieprogram.json")
            json = inputStream.bufferedReader().use { it.readText()  }

            val jsonArray = JSONArray(json)

            for(i in 0..jsonArray.length()-1){

                val jsonEducationObject = jsonArray.getJSONObject(i)

                val educationCode = jsonEducationObject.get("Studieprogramkode").toString()
                val QualificationCode = jsonEducationObject.get("Kvalifikasjonskode").toString()
                val educationTitle = jsonEducationObject.get("Programnavn").toString()
                val schoolCode = (jsonEducationObject.get("Institusjonskode") as String).toInt()
                val school = School.schoolList.find { it.schoolCode ==  schoolCode}!!
                val pointsAcquired = (jsonEducationObject.get("Studiepoeng") as String).toDouble()
                var studyField = convertStudyFieldToString((jsonEducationObject.get("Studiumkode").toString()))
                val level = convertLevelCodeToString((jsonEducationObject.get("Nivåkode").toString()))
                val studyImage = "https://firebasestorage.googleapis.com/v0/b/utdanningsoversikten-b0b8a.appspot.com/o/" + studyField +".jpg?alt=media"

                val newEducation = Education(
                    i, // Mangler fra JSON
                    educationCode,
                    QualificationCode,
                    educationTitle,
                    "Fagområde: $studyField\nNivå: $level\nSted: " + school.place, // Mangler fra JSON
                    "Fagområde: $studyField\nNivå: $level", // Mangler fra JSON
                    school,
                    studyImage, // Mangler fra JSON
                    44.0,  // Mangler fra JSON
                    pointsAcquired,
                    level,
                    studyField
                )

                // Deretter legge de i listen!
                Education.educationlist.add(newEducation)
            }

        }
        catch (e : IOException) {

        }

    }

    private fun convertLevelCodeToString(pointsAcquired: String): String {

        when (pointsAcquired) {
            "VS" -> return "Videregående"
            "AR" -> return "Årsenhet"
            "B3", "B4" -> return "Bachelorgrad"
            "M2", "ME", "MX", "HN", "M5" -> return "Mastergrad"
            "PR" -> return "Profesjonsstudium"
            "FU" -> return "Forskerutdanning"
            else -> return "Andre"
        }
    }

    private fun convertStudyFieldToString(studyField: String): String {

        when (studyField) {
            "BLU", "GLU1-7", "GLU5-10", "IMALU1-7", "IMALU5-10", "FAG", "FLU", "ALU", "INTMASTER" -> return "Lærer"
            "JOU" -> return "Journalistikk"
            "UVFAG", "PRAKTPED" -> return "Pedagogikk"
            "REALFAG"-> return "Matematisk-naturvitenskapelig-informatikk"
            "HUMAN", "EXPHIL" -> return "Historisk-filosofi"
            "DESIGN", "ID" -> return "Design"
            "VIDEREG" -> return "Videregående"
            "SAMVIT" -> return "Samfunnsvitenskap"
            "HELSEFAG", "SYK" -> return "Helsefag"
            "BAR" -> return "Barnevern"
            "VER" -> return "Vernepleier"
            "ØA" -> return "Økonomi"
            "IU" -> return "Idrett"
            "JUS" -> return "Juss"
            "SOS" -> return "Sosionom"
            "ERNÆRING" -> return "Ernæring"
            "ERG" -> return "Ergoterapi"
            "FYS" -> return "Fysioterapi"
            "RAD" -> return "Radiografi"
            "ING", "BIO", "ORT" -> return "Ingeniør"
            "TEKNOLOGI" -> return "Teknologi"
            "DOV" -> return "Døvetolk"
            "BILDEKUNST", "VISKUN", "KUNST" -> return "Kunst"
            "SCEKUN" -> return "Scenekunst"
            "MUSIKK" -> return "Musikk"
            "MAR" -> return "Maritim"
            "KU", "LU" -> return "Landbruk"
            "PSYKOLOGI" -> return "Psykologi"
            "UTVMILJØ" -> return "Miljø"
            "TANNPL", "TANNTEKN" -> return "Tannpleie"
            "ODO" -> return "Odontologi"
            "MEDISIN" -> return "Medisin"
            "FARMASI", "RES" -> return "Farmasi"
            "TEOLOGI" -> return "Teologi"
            "FISKERIFAG" -> return "Fiskerifag"
            "ARKITEKTUR" -> return "Arkitektur"
            "AUD" -> return "Audiograf"
            "VET", "DYR" -> return "Dyrepleie"
            "BIB" -> return "Bibliotekar"
            "POLITI" -> return "Politi"
            "MILITÆR" -> return "Militær"
            "YRKESFAG" -> return "Yrkesfag"
            else -> return "Andre"
        }
    }

    private fun getPlaceCSVFileBasedOnZipCode(schoolZipCode: String): String {

        try {

            val fileReader = InputStreamReader(getAssets().open("steder.csv"))
            val bufferedReader = BufferedReader(fileReader)

            bufferedReader.readLine()
            var line : String

            while ((bufferedReader.readLine() != null)) {

                line = bufferedReader.readLine()
                val word = line.split("\t")
                if(word[0].equals(schoolZipCode)){
                    return word[1]
                }
                if(word[2].equals(schoolZipCode)){
                    return word[3]
                }

            }


            bufferedReader.close()
        }
        catch (e : IOException) {

        }

        return "Ukjent"
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav?.setupWithNavController(navController)
    }
}
