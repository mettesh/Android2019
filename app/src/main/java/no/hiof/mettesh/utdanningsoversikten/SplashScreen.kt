package no.hiof.mettesh.utdanningsoversikten

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash_screen.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

class SplashScreen : AppCompatActivity() {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseCurrentUser = firebaseAuth.currentUser

        //Vise startLayout
        setContentView(R.layout.activity_splash_screen)

        animateHeader()

        // TODO: addOnSuccessListene -> Laste ned data også her

        if (firebaseCurrentUser != null) {
            loadDataFromFirebase(firebaseCurrentUser)
        }

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }, 2000)


        //Laste inn data fra API?
    }

    private fun loadDataFromFirebase(firebaseCurrentUser : FirebaseUser) {

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

    }

    fun animateHeader(){
        val animationUtdanning = AnimationUtils.loadAnimation(this, R.anim.anim_utdanning)
        start_layout_text1.startAnimation(animationUtdanning)

        val animationOversikten = AnimationUtils.loadAnimation(this, R.anim.anim_oversikten)
        start_layout_text2.startAnimation(animationOversikten)


    }

}
