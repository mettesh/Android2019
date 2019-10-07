package no.hiof.mettesh.utdanningsoversikten


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.fragment_favourite_login_or_empty.view.*
import no.hiof.mettesh.utdanningsoversikten.FavouriteFragment.Companion.RC_SIGN_IN
import no.hiof.mettesh.utdanningsoversikten.model.Education

class AccountFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseAuth = FirebaseAuth.getInstance()

        // Sjekker først om man er logget inn:
        val firebaseCurrentUser = firebaseAuth.currentUser
        if(firebaseCurrentUser == null){

            return inflater.inflate(R.layout.fragment_favourite_login_or_empty, container, false)
        }

        return inflater.inflate(R.layout.fragment_account, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        val firebaseCurrentUser = firebaseAuth.currentUser

        val loginOrEmptylistTextview : TextView
        val loginButton : Button
        val usernameTextView : TextView
        val numOfFavtextView : TextView
        val logoutButton : Button

        if(firebaseCurrentUser == null){

            loginOrEmptylistTextview = view.textView_login_or_empty
            loginButton = view.login_button

            loginOrEmptylistTextview.text = "Logg inn eller registrer deg. Med konto kan du lagre utdanninger, søk og skoler"

            loginButton.setOnClickListener {

                createAuthenticationListener()
            }
        }
        else {

            usernameTextView = view.textView_userName
            numOfFavtextView = view.textView_numOfFav
            logoutButton = view.button_accountLogOut
            usernameTextView.text = firebaseCurrentUser.displayName

            var numOfFav = Education.getFavouriteEducations().size

            if (numOfFav == 0) {
                numOfFavtextView.text = "Du har ingen lagrede favoritter"
            } else if (numOfFav > 1) {
                numOfFavtextView.text =
                    Education.getFavouriteEducations().size.toString() + " favoritter lagret"
            } else {
                numOfFavtextView.text =
                    Education.getFavouriteEducations().size.toString() + " favoritt lagret"
            }

            logoutButton.setOnClickListener {
                firebaseAuth.signOut()
                Toast.makeText(context, firebaseCurrentUser.displayName + " logget ut", Toast.LENGTH_SHORT).show()
                val ft = fragmentManager!!.beginTransaction()
                ft.detach(this).attach(this).commit()

            }
        }

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
            if (resultCode == Activity.RESULT_OK) {
                val user = firebaseAuth.currentUser
                Toast.makeText(context, user?.displayName + " er logget inn", Toast.LENGTH_SHORT).show()
                //firebaseAuth.removeAuthStateListener(authStateListener)

                val ft = fragmentManager!!.beginTransaction()
                ft.detach(this).attach(this).commit()

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Innlogging avbrutt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1
    }

}
