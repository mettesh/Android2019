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
