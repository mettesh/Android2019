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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
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

        // Settes direkte da vi ikke har dette tilgjengelig i datasett
        val studyImage : ImageView = view.studyImage
        val poenggrenseTextView: TextView = view.detailPoenggrense
        val favButton: Button = view.floatingButton_fav
        val schoolUrl: String = education.school.webPage

        Glide.with(view)
            .load(education.image)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(studyImage)

        detailEducationTitleTextView.text = education.educationTitle
        detailSchoolNameTextView.text = education.school!!.schoolTitle

        detailSchoolNameTextView.setOnClickListener {
            openWebBroser(schoolUrl)
        }

        sendToWebImgView.setOnClickListener {
            openWebBroser(schoolUrl)
        }

        poenggrenseTextView.text = education.pointsRequired.toString()


        //detailEducationDescriptionTextView.text = education.descriptionLong


        // Setter bilde på floatingButton etter om den finnes i favoritter
        if (Education.favouriteEducationlist.contains(education)) {
            favButton.text = "Fjern fra favoritter"
        } else {
            favButton.text = "Legg til i favoritter"
        }

        favButton.setOnClickListener {

            if(context!!.isConnectedToNetwork()){
                addEducationToFavouriteAndChangeHeart(firebaseCurrentUser, education, favButton)
            }
            else {
                showToast("Du må ha internettilkobling for å kunne legge til/fjerne utdanninger")
            }

        }
    }

    private fun addEducationToFavouriteAndChangeHeart(firebaseCurrentUser: FirebaseUser?, education: Education, favButton: Button) {
        if (firebaseCurrentUser == null) {

            showToast("Du må være innlogget for å kunne legge til/fjerne utdanninger")

        } else {

            if (Education.favouriteEducationlist.contains(education)) {

                removeFavFromFirestore(firebaseCurrentUser, education)

                favButton.text = "Legg til i favoritter"

                showToast(education.educationTitle + " er fjernet fra favoritter")

            } else {

                addFavToFirestore(firebaseCurrentUser, education)

                favButton.text = "Fjern fra favoritter"

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
            Toast.LENGTH_LONG
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
