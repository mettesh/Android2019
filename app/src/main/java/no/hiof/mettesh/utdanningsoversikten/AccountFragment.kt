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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.view.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

class AccountFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseAuth = FirebaseAuth.getInstance()

        return inflater.inflate(R.layout.fragment_account, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        viewCorrectElementsInLayout(view)

    }

    private fun viewCorrectElementsInLayout(view : View){

        val firebaseCurrentUser = firebaseAuth.currentUser

        val loginTextview : TextView = view.accountlogInText
        val usernameTextView : TextView = view.textView_userName
        val numOfFavtextView : TextView = view.textView_numOfFav
        val loginButton : Button = view.button_accountlogIn
        val logoutButton : Button = view.button_accountlogOut
        val notLogedInText : TextView = view.notLoggedInText

        usernameTextView.visibility = View.GONE
        numOfFavtextView.visibility = View.GONE
        loginButton.visibility = View.GONE
        logoutButton.visibility = View.GONE
        loginTextview.visibility = View.GONE
        notLogedInText.visibility = View.GONE

        if(firebaseCurrentUser == null){

            loginButton.visibility = View.VISIBLE

            notLogedInText.visibility = View.VISIBLE

            loginButton.setOnClickListener {

                if (context!!.isConnectedToNetwork()){
                    createAuthenticationListener()
                } else {
                    showToast("For å kunne logge inn må du være tilkoblet internett")
                }
            }

        } else {

            usernameTextView.visibility = View.VISIBLE
            numOfFavtextView.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
            loginTextview.visibility = View.VISIBLE

            usernameTextView.text = firebaseCurrentUser.displayName

            val numOfFav = Education.favouriteEducationlist.size

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
                viewCorrectElementsInLayout(view)

            }
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

                viewCorrectElementsInLayout(view!!)

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
