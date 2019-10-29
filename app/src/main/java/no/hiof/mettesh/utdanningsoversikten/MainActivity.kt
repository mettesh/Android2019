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
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Fyller lista med skoler og utdanninger (Midlertidig til vi få hentet data fra API)
        //School.fillSchoolList()
        //Education.fillEducationList()

        // Leser json fil som oppretter utdannings-objekter
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

                if( schoolPhoneNumber.equals(null) || schoolPhoneNumber.equals("")){
                    schoolPhoneNumber = 0
                }
                else {
                    schoolPhoneNumber = (jsonSchoolObject.get("Telefon") as String).toInt()
                }

                val webPage = jsonSchoolObject.get("Nettside").toString()

                println(jsonSchoolObject.get("Institusjonskode"))

                // Skal lage nye edu-obj
                val newSchool = School(
                    schoolCode,
                    schoolTitle,
                    schoolShortTitle,
                    "https://upload.wikimedia.org/wikipedia/en/d/da/Hi%C3%98_emblem.png", // Mangler i JSON
                    schoolAdress,
                    schoolZipCode,
                    schoolPhoneNumber,
                    "59.1288539,11.3532008,15", // Mangler i JSON
                    webPage
                )


                // Deretter legge de i listen!
                if(!School.schoolList.contains(newSchool)){
                    School.schoolList.add(newSchool)
                }

                println(newSchool)
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

            println(jsonArray.length())


            for(i in 0..jsonArray.length()-1){

                val jsonEducationObject = jsonArray.getJSONObject(i)

                val educationCode = jsonEducationObject.get("Studieprogramkode").toString()
                val QualificationCode = jsonEducationObject.get("Kvalifikasjonskode").toString()
                val educationTitle = jsonEducationObject.get("Programnavn").toString()
                val schoolCode = (jsonEducationObject.get("Institusjonskode") as String).toInt()
                val pointsAcquired = (jsonEducationObject.get("Studiepoeng") as String).toDouble()

                // Skal lage nye edu-obj
                val newEducation = Education(
                    i, // Mangler fra JSON
                    educationCode,
                    QualificationCode,
                    educationTitle,
                    "Kort beskrivelse ", // Mangler fra JSON
                    "Lang beskrivelse", // Mangler fra JSON
                    School.schoolList.find { it.schoolCode ==  schoolCode}!!,
                    "https://www.hiof.no/om/aktuelt/aktuelle-saker/arkiv/filer/sykepleierfotvaskBH_654.jpg", // Mangler fra JSON
                    44.0,  // Mangler fra JSON
                    pointsAcquired

                )

                // Deretter legge de i listen!
                Education.educationlist.add(newEducation)
            }

        }
        catch (e : IOException) {

        }

    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav?.setupWithNavController(navController)
    }
}
