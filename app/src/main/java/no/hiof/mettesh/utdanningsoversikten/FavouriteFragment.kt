package no.hiof.mettesh.utdanningsoversikten

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_education_list.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education
import com.firebase.ui.auth.AuthUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_education_list.view.*
import com.google.firebase.firestore.FirebaseFirestore


class FavouriteFragment : Fragment() {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var firestoreDb: FirebaseFirestore

    private var favouriteEducationList : ArrayList<Education> = Education.favouriteEducationlist

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_education_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        viewCorrectElementsInLayout(view)

    }

    @SuppressLint("RestrictedApi")
    fun viewCorrectElementsInLayout(view : View){

        val firebaseCurrentUser = firebaseAuth.currentUser

        val recyclerView : RecyclerView = view.educationRecyclerView
        val loginOrEmptylistTextview : TextView = view.textView_login_or_empty
        val loginButton : Button = view.login_button
        val filterFloatingButton : FloatingActionButton = view.openFilterFloatingButton

        recyclerView.visibility = View.GONE
        loginOrEmptylistTextview.visibility = View.GONE
        loginButton.visibility = View.GONE
        filterFloatingButton.visibility = View.GONE

        if(firebaseCurrentUser == null){

            loginOrEmptylistTextview.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE

            loginOrEmptylistTextview.text = "Du må logge inn for å se dine lagrede favoritter"

            loginButton.setOnClickListener {

                createAuthenticationListener()
            }

        } else {

            // TODO: Må vente på kallet. Rekker mest sannsynlig ikke å hente inn data før den går videre i kallet.
            getDataFromFirestore(firebaseCurrentUser)

            if (favouriteEducationList.isEmpty()){

                loginOrEmptylistTextview.visibility = View.VISIBLE
                loginOrEmptylistTextview.text = "Du har ingen lagrede favoritter"

            } else {

                setUpRecycleView()
                recyclerView.visibility = View.VISIBLE
            }
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


        educationRecyclerView.adapter = EducationAdapter(favouriteEducationList, View.OnClickListener { view ->

            val position = educationRecyclerView.getChildAdapterPosition(view)
            val clickedEducation = favouriteEducationList[position]
            var action = FavouriteFragmentDirections.actionFavouriteDestToEducationDetailFragment(clickedEducation.id)

            findNavController().navigate(action)


        })

        educationRecyclerView.layoutManager = GridLayoutManager(context, 1)
    }


    private fun createAuthenticationListener() {

        authStateListener = FirebaseAuth.AuthStateListener {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
               val user = firebaseAuth.currentUser
                Toast.makeText(context, user?.displayName + " er logget inn", Toast.LENGTH_SHORT).show()

                // !! = Non-null assertion. Gir beskjed om at denne ikke er null (Vet dette da denne metoden kun bli kalt etter at onCreat er kalt)
                viewCorrectElementsInLayout(view!!)

            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "Innlogging avbrutt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDataFromFirestore(firebaseCurrentUser: FirebaseUser) {

        firestoreDb = FirebaseFirestore.getInstance()

        val docRef = firestoreDb.collection("favourites").document(firebaseCurrentUser.email.toString()).collection("favList")

        val eduList = ArrayList<Education>()

        docRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot) {

                val education : Education = document.toObject(Education::class.java)

                eduList.add(education)
            }
        }

        Education.favouriteEducationlist = eduList
    }

    companion object {
        const val RC_SIGN_IN = 1
    }
}
