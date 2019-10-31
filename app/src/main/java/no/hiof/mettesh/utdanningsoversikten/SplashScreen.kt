package no.hiof.mettesh.utdanningsoversikten

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Vise startLayout
        setContentView(R.layout.activity_splash_screen)

        animateHeader()

        // TODO: addOnSuccessListene -> Laste ned data også her
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }, 2000)


        //Laste inn data fra API?
    }

    fun animateHeader(){
        val animationUtdanning = AnimationUtils.loadAnimation(this, R.anim.anim_utdanning)
        start_layout_text1.startAnimation(animationUtdanning)

        val animationOversikten = AnimationUtils.loadAnimation(this, R.anim.anim_oversikten)
        start_layout_text2.startAnimation(animationOversikten)


    }

}
