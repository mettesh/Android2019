package no.hiof.mettesh.utdanningsoversikten

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_education_list.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education
import com.firebase.ui.auth.AuthUI


class FavouriteFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener


    // Henter inn liste med utdanninger fra Education-klassen
    private var favouriteEducationList : ArrayList<Education> = Education.getFavouriteEducations()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseAuth = FirebaseAuth.getInstance()


        // Sjekker først om man er logget inn:
        createAuthenticationListener()

        // Henter inn layout for dette Fragmentet
        return inflater.inflate(R.layout.fragment_education_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()

        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun setUpRecycleView() {

        // Bruker adapteren for å binde layout og RecyclerView
        educationRecyclerView.adapter = EducationAdapter(favouriteEducationList, View.OnClickListener { view ->

            // Får posisjonen til elementet i lista som er trykket på
            val position = educationRecyclerView.getChildAdapterPosition(view)

            // Får deretter riktig utdannning paserte på denne posisjonen
            val clickedEducation = favouriteEducationList[position]

            // Oppretter navigasjonen (utifra nav_graph.xml) og sender med id til utdanningen.
            var action = FavouriteFragmentDirections.actionFavouriteDestToEducationDetailFragment(clickedEducation.id)

            // Navigerer til EducationDetailFragment
            findNavController().navigate(action)


        })

        // Etter at alt er satt setter vi dette RecyclerView med det layouten vi ønsker.
        educationRecyclerView.layoutManager = GridLayoutManager(context, 1)

    }


    private fun createAuthenticationListener() {

        authStateListener = FirebaseAuth.AuthStateListener {
            val firebaseCurrentUser = firebaseAuth.currentUser
            if(firebaseCurrentUser == null){

                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders( arrayListOf(
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(), RC_SIGN_IN
                )
            }
            else{
                Toast.makeText(context, firebaseCurrentUser.displayName + " er logget inn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
               val user = firebaseAuth.currentUser
                Toast.makeText(context, user?.displayName + " er logget inn", Toast.LENGTH_SHORT).show()
                firebaseAuth.removeAuthStateListener(authStateListener)
                setUpRecycleView()
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "Innlogging avbrutt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1
    }
}
