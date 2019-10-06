package no.hiof.mettesh.utdanningsoversikten

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_education_list.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_favourite_login_or_empty.view.*






class FavouriteFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener

    private var favouriteEducationList : ArrayList<Education> = Education.getFavouriteEducations()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseAuth = FirebaseAuth.getInstance()

        // Sjekker først om man er logget inn:
        val firebaseCurrentUser = firebaseAuth.currentUser
        if(firebaseCurrentUser == null || favouriteEducationList.isEmpty()){

            return inflater.inflate(R.layout.fragment_favourite_login_or_empty, container, false)
        }

        return inflater.inflate(R.layout.fragment_education_list, container, false)

        //createAuthenticationListener()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        val firebaseCurrentUser = firebaseAuth.currentUser

        val loginOrEmptylistTextview : TextView
        val loginButton : Button

        if(firebaseCurrentUser == null){

            loginOrEmptylistTextview = view.textView_login_or_empty
            loginButton = view.login_button

            loginOrEmptylistTextview.text = "Du må logge inn for å se dine lagrede favoritter"

            loginButton.setOnClickListener {

                createAuthenticationListener()
            }
        }

        else if (favouriteEducationList.isEmpty()){

            loginOrEmptylistTextview = view.textView_login_or_empty
            loginButton = view.login_button

            loginOrEmptylistTextview.text = "Du har ingen lagrede favoritter"

            loginButton.visibility = View.GONE

        }
        else{
            setUpRecycleView()
        }

    }

    override fun onResume() {
        super.onResume()

        //firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onPause() {
        super.onPause()

        //firebaseAuth.removeAuthStateListener(authStateListener)
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

        //authStateListener = FirebaseAuth.AuthStateListener {
                 startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders( arrayListOf(
                            AuthUI.IdpConfig.GoogleBuilder().build(),
                            AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(), RC_SIGN_IN
                )
        //}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
               val user = firebaseAuth.currentUser
                Toast.makeText(context, user?.displayName + " er logget inn", Toast.LENGTH_SHORT).show()
                //firebaseAuth.removeAuthStateListener(authStateListener)

                val ft = fragmentManager!!.beginTransaction()
                ft.detach(this).attach(this).commit()

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
