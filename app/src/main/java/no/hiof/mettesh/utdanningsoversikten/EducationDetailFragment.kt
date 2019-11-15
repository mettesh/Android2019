package no.hiof.mettesh.utdanningsoversikten

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_education_detail.view.*
import kotlinx.android.synthetic.main.fragment_education_list.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationDetailFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_education_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()

        val arguments = arguments?.let { EducationDetailFragmentArgs.fromBundle(it) }
        val education = Education.educationlist.find { it.id == arguments!!.id }
        val firebaseCurrentUser = firebaseAuth.currentUser

        setViewContentAndLogic(view, education!!, firebaseCurrentUser)
    }

    private fun setViewContentAndLogic(view: View, education: Education, firebaseCurrentUser: FirebaseUser?) {

        val detailEducationTitleTextView: TextView = view.detailEducationTitle
        val detailSchoolNameTextView: TextView = view.detailSchoolName
        val sendToWebImgView: ImageView = view.sendToWebImgView
        val detailEducationDescriptionTextView: TextView = view.detailEducationDescription
        val poenggrenseTextView: TextView = view.detailPoenggrense
        val kravkodeTextView: TextView = view.detailKravkode
        val favFloatingButton: FloatingActionButton = view.floatingButton_fav
        val schoolUrl: String = education.school.webPage


        detailEducationTitleTextView.text = education.educationTitle
        detailSchoolNameTextView.text = education.school!!.schoolTitle

        detailSchoolNameTextView.setOnClickListener {
            openWebBroser(schoolUrl)
        }

        sendToWebImgView.setOnClickListener {
            openWebBroser(schoolUrl)
        }

        kravkodeTextView.text = education.qualificationCode
        poenggrenseTextView.text = education.pointsRequired.toString()
        detailEducationDescriptionTextView.text = education.descriptionLong


        // Setter bilde på floatingButton etter om den finnes i favoritter
        if (Education.favouriteEducationlist.contains(education)) {
            favFloatingButton.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            favFloatingButton.setImageResource(R.drawable.ic_favorite_border)
        }

        favFloatingButton.setOnClickListener {

            if(context!!.isConnectedToNetwork()){
                addEducationToFavouriteAndChangeHeart(firebaseCurrentUser, education, favFloatingButton)
            }
            else {
                showToast("Du må ha internettilkobling for å kunne legge til utdanninger i favoritter")
            }

        }
    }

    private fun addEducationToFavouriteAndChangeHeart(firebaseCurrentUser: FirebaseUser?, education: Education, favFloatingButton: FloatingActionButton) {
        if (firebaseCurrentUser == null) {

            showToast("Du må være innlogget for å kunne legge til utdanninger i favoritter")

        } else {

            if (Education.favouriteEducationlist.contains(education)) {
                favFloatingButton.setImageResource(R.drawable.ic_favorite_border)

                removeFavFromFirestore(firebaseCurrentUser, education)

                showToast(education.educationTitle + " er fjernet fra favoritter")

            } else {

                favFloatingButton.setImageResource(R.drawable.ic_favorite_filled)

                addFavToFirestore(firebaseCurrentUser, education)

                showToast(education.educationTitle + " er lagt til i favoritter")

            }
        }
    }

    fun openWebBroser(url : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    // TODO: Duplikat! Bør legges til en felles!
    private fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }

    companion object{

        // TODO: Brukes i både adapter og her!

        fun addFavToFirestore(firebaseCurrentUser : FirebaseUser?, education : Education) {

            var firestoreDb = FirebaseFirestore.getInstance()

            firestoreDb.collection("favourites").document(firebaseCurrentUser!!.email.toString()).collection("favList")
                .document(education.id.toString())
                .set(education)
                .addOnSuccessListener { Log.d(TAG, "Education successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            Education.favouriteEducationlist.add(education)
        }

        fun removeFavFromFirestore(firebaseCurrentUser: FirebaseUser, education: Education) {

            var firestoreDb = FirebaseFirestore.getInstance()

            firestoreDb.collection("favourites").document(firebaseCurrentUser.email.toString()).collection("favList").document(education.id.toString())
                .delete()
                .addOnSuccessListener { Log.d(TAG, "Education successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

            Education.favouriteEducationlist.remove(education)

        }
    }
}
