package no.hiof.mettesh.utdanningsoversikten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.hiof.mettesh.utdanningsoversikten.model.Education

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Fyller lista med utdanninger (Midlertidig til vi få hentet data fra API)
        Education.fillEducationList()

        // Referanse til navController
        var navController = findNavController(R.id.listFragment)

        // Setter opp bottom navigation
        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav?.setupWithNavController(navController)
    }
}
