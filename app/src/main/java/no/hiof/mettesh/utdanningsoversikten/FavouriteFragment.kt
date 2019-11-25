package no.hiof.mettesh.utdanningsoversikten

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
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
    private lateinit var firestoreDb: FirebaseFirestore

    private var favouriteEducationList: ArrayList<Education> = Education.favouriteEducationlist

    private lateinit var recyclerView : RecyclerView
    private lateinit var loginOrEmptylistTextview : TextView
    private lateinit var loginButton : Button
    private lateinit var filterFloatingButton : FloatingActionButton



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_education_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        recyclerView = view.educationRecyclerView
        loginOrEmptylistTextview = view.textView_login_or_empty
        loginButton = view.login_button
        filterFloatingButton = view.openFilterFloatingButton

        setViewContentAndLogic(view)

    }

    @SuppressLint("RestrictedApi")
    fun setViewContentAndLogic(view : View){

        val firebaseCurrentUser = firebaseAuth.currentUser

        recyclerView.visibility = View.GONE
        loginOrEmptylistTextview.visibility = View.GONE
        loginButton.visibility = View.GONE
        filterFloatingButton.visibility = View.GONE


        if (firebaseCurrentUser == null) {
            viewContentForUserNotLoggedIn()

        } else {
            viewContentForUserLoggedIn(firebaseCurrentUser)
        }
    }

    private fun viewContentForUserNotLoggedIn() {
        loginOrEmptylistTextview.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE

        loginOrEmptylistTextview.text = "Logg inn for å se dine lagrede favoritter"

        loginButton.setOnClickListener {

            if (context!!.isConnectedToNetwork()) {
                createAuthenticationListener()
            } else {
                showToast("Du må være tilkoblet internett for å kunne logge inn")
            }
        }
    }

    private fun viewContentForUserLoggedIn(firebaseCurrentUser: FirebaseUser) {
        if (!context!!.isConnectedToNetwork()) {
            // Ved første oppstart skal dette gis beskjed om i alertBox
            if (isFirstRun) {
                showToast("Du er ikke tilkoblet internett og ser kanskje ikke oppdatert informasjon")
                isFirstRun = false
            }
        }

        FirebaseFunctions.getDataFromFirestore(firebaseCurrentUser)

        if (Education.favouriteEducationlist.isEmpty()){

            loginOrEmptylistTextview.visibility = View.VISIBLE
            loginOrEmptylistTextview.text = "Du har ingen lagrede favoritter"

        } else {

            setUpRecycleView()
            recyclerView.visibility = View.VISIBLE
        }
    }


    private fun setUpRecycleView() {

        educationRecyclerView?.adapter = EducationAdapter(favouriteEducationList, View.OnClickListener { view ->

            val position = educationRecyclerView?.getChildAdapterPosition(view)
            val clickedEducation = favouriteEducationList[position!!]
            var action = FavouriteFragmentDirections.actionFavouriteDestToEducationDetailFragment(clickedEducation.id)

            findNavController().navigate(action)

        })

        educationRecyclerView?.layoutManager = GridLayoutManager(context, 1)

    }

    private fun createAuthenticationListener() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.loginTheme)
                .setAvailableProviders( arrayListOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(), RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                val user = firebaseAuth.currentUser

                showToast(user?.displayName + " er logget inn")

                setViewContentAndLogic(view!!)

            }
            else if (resultCode == RESULT_CANCELED) {
                showToast("Innlogging avbrutt")
            }
        }
    }

    /*
    private fun getDataFromFirestore(firebaseCurrentUser: FirebaseUser) {

        firestoreDb = FirebaseFirestore.getInstance()

        val docRef = firestoreDb.collection("favourites").document(firebaseCurrentUser.email.toString()).collection("favList")

        val eduList = ArrayList<Education>()

        docRef.get().addOnSuccessListener { documentSnapshot ->

            for (document: QueryDocumentSnapshot in documentSnapshot) {

                val education : Education = document.toObject(Education::class.java)

                eduList.add(education)
            }

            Education.favouriteEducationlist = eduList
        }
    }
    */


    private fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val RC_SIGN_IN = 1
        private var isFirstRun = true
    }
}
