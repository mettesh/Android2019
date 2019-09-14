package no.hiof.mettesh.utdanningsoversikten

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Vise startLayout
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, FirstActivity::class.java))
            finish()
        }, 4000)


        //Laste inn data fra API?
    }

}
