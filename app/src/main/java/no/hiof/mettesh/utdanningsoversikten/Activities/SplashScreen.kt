package no.hiof.mettesh.utdanningsoversikten.Activities

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash_screen.*
import no.hiof.mettesh.utdanningsoversikten.Utils.FirebaseFunctions
import no.hiof.mettesh.utdanningsoversikten.R
import no.hiof.mettesh.utdanningsoversikten.Models.Education
import no.hiof.mettesh.utdanningsoversikten.Models.Place
import no.hiof.mettesh.utdanningsoversikten.Models.School
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class SplashScreen : AppCompatActivity() {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        progressBar.visibility = View.GONE

        Handler().postDelayed({
            // Laster inn data i egen tråd. Gå videre til MainActivity når denne er ferdig
            AsyncTaskWorker().execute()
        }, 1000)
    }

    inner class AsyncTaskWorker : AsyncTask<Void, String, Boolean>() {
        override fun doInBackground(vararg voids: Void): Boolean? {
            val firebaseCurrentUser = firebaseAuth.currentUser

            publishProgress("Loading data")

            readPlaceCSVFileAndMakePlaceObjects()
            readSchoolJsonFileAndMakeSchoolObjects()
            readEducationJsonFileAndMakeEducationObjects()

            if (firebaseCurrentUser != null) {
                FirebaseFunctions.getDataFromFirestore(
                    firebaseCurrentUser
                )
            }

            publishProgress("Data loading finished")

            return true
        }

        override fun onProgressUpdate(vararg messages: String) {
            progressBar.visibility = View.VISIBLE
            println(messages[0])
        }

        override fun onPostExecute(result: Boolean) {
            if (result) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                // For å hindre at man går tilbake til splashscreen ved trykk på tilbakepil kalles finish()
                finish()
            } else {
                runOnUiThread { showErrorDialogAndStartMainActivity() }
            }
        }
    }

    private fun showErrorDialogAndStartMainActivity() {
        val alertBox = AlertDialog.Builder(this@SplashScreen)

        alertBox.setTitle("Feil ved lasting av data")
        alertBox.setMessage("Det oppstod dessverre en feil ved lasting av data.\nApplikasjonen kan derfor mangle funksjonalitet.")

        alertBox.setPositiveButton("OK") { _, _ ->
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        }
        val alert = alertBox.create()
        alert.show()
    }

    /*private fun loadDataFromFirebase(firebaseCurrentUser : FirebaseUser) {

        firestoreDb = FirebaseFirestore.getInstance()

        val docRef = firestoreDb.collection("favourites").document(firebaseCurrentUser.email.toString()).collection("favList")

        val eduList = ArrayList<Education>()

        docRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot) {

                val education: Education = document.toObject(Education::class.java)

                eduList.add(education)
            }

            Education.favouriteEducationlist = eduList
        }

    }*/

    private fun readPlaceCSVFileAndMakePlaceObjects() {

        try {
            val fileReader = InputStreamReader(getAssets().open("steder.csv"))
            val bufferedReader = BufferedReader(fileReader)

            var line : String

            val iterator = bufferedReader.lineSequence().iterator()

            while(iterator.hasNext()) {

                line = iterator.next()

                val word = line.split("\t")

                // CSV_filen inneholder 2 postkode/nummer per linje. Dette gjør at færre linjer trengs å leses.
                Place.zipCodeAndPlaceList.add(Place(word[0], word[1]))
                Place.zipCodeAndPlaceList.add(Place(word[2], word[3]))
            }
            bufferedReader.close()
        }
        catch (e : IOException) {
            println("IOException when reading steder.csv :$e")
        }
    }

    private fun readSchoolJsonFileAndMakeSchoolObjects() {

        var json: String?

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

                var place = "Ukjent"
                val placeFromZipCode = Place.zipCodeAndPlaceList.find { it.zipCode == schoolZipCode }?.place
                if(placeFromZipCode != null){ place = placeFromZipCode }

                if( schoolPhoneNumber.equals(null) || schoolPhoneNumber.equals("")){
                    schoolPhoneNumber = 0
                } else {
                    schoolPhoneNumber = (jsonSchoolObject.get("Telefon") as String).toInt()
                }

                val newSchool = School(
                    schoolCode,
                    schoolTitle,
                    schoolShortTitle,
                    schoolAdress,
                    schoolZipCode,
                    schoolPhoneNumber,
                    "59.1288539,11.3532008,15",
                    webPage,
                    place
                )

                if(!School.schoolList.contains(newSchool)){
                    School.schoolList.add(newSchool)
                }
            }

        }
        catch (e : IOException) {
            println("IOException when reading institusjon.json :$e")
        }
    }

    private fun readEducationJsonFileAndMakeEducationObjects() {
        var json: String?

        try {
            val inputStream : InputStream = assets.open("studieprogram.json")
            json = inputStream.bufferedReader().use { it.readText()  }

            val jsonArray = JSONArray(json)

            for(i in 0..jsonArray.length()-1){

                val jsonEducationObject = jsonArray.getJSONObject(i)

                val educationCode = jsonEducationObject.get("Studieprogramkode").toString()
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
            println("IOException when reading studieprogram.json :$e")
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
            "VIDEREG" -> return "Videregående"
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


}
