package no.hiof.mettesh.utdanningsoversikten

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_account.view.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

class AccountFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var loginTextview : TextView
    private lateinit var usernameTextView : TextView
    private lateinit var numOfFavtextView : TextView
    private lateinit var loginButton : Button
    private lateinit var logoutButton : Button
    private lateinit var notLogedInText : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)
        setViewContentAndLogic(view)
    }

    private fun setViewContentAndLogic(view : View){

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseCurrentUser = firebaseAuth.currentUser

        loginTextview = view.accountlogInText
        usernameTextView = view.textView_userName
        numOfFavtextView = view.textView_numOfFav
        loginButton = view.button_accountlogIn
        logoutButton = view.button_accountlogOut
        notLogedInText = view.notLoggedInText

        usernameTextView.visibility = View.GONE
        numOfFavtextView.visibility = View.GONE
        loginButton.visibility = View.GONE
        logoutButton.visibility = View.GONE
        loginTextview.visibility = View.GONE
        notLogedInText.visibility = View.GONE

        if(firebaseCurrentUser == null){
            viewContentForUserNotLoggedIn()

        } else {
            FirebaseFunctions.getDataFromFirestore(firebaseCurrentUser)
            // TODO: Må vente til data er lastet inn!
            viewContentForUserLoggedIn(firebaseCurrentUser, view)
        }

    }

    private fun viewContentForUserNotLoggedIn() {
        loginButton.visibility = View.VISIBLE
        notLogedInText.visibility = View.VISIBLE

        loginButton.setOnClickListener {

            if (context!!.isConnectedToNetwork()) {
                createAuthenticationListener()
            } else {
                showToast("For å kunne logge inn må du være tilkoblet internett")
            }
        }
    }

    private fun viewContentForUserLoggedIn(firebaseCurrentUser: FirebaseUser, view: View) {
        usernameTextView.visibility = View.VISIBLE
        numOfFavtextView.visibility = View.VISIBLE
        logoutButton.visibility = View.VISIBLE
        loginTextview.visibility = View.VISIBLE

        usernameTextView.text = firebaseCurrentUser.displayName

        val numOfFav = Education.getNumbersOfEducationInFavouriteList()

        if (numOfFav == 0) {
            numOfFavtextView.text = "Du har ingen lagrede favoritter"
        } else if (numOfFav > 1) {
            numOfFavtextView.text =
                numOfFav.toString() + " favoritter lagret"
        } else {
            numOfFavtextView.text =
                numOfFav.toString() + " favoritt lagret"
        }

        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            showToast(firebaseCurrentUser.displayName + " er logget ut")
            setViewContentAndLogic(view)

        }
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
            if (resultCode == Activity.RESULT_OK) {
                val user = firebaseAuth.currentUser

                showToast(user?.displayName + " er logget inn")

                setViewContentAndLogic(view!!)

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                showToast("Innlogging avbrutt")
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }

    companion object {
        const val RC_SIGN_IN = 1
    }

}
