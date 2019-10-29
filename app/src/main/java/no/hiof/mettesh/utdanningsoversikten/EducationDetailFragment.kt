package no.hiof.mettesh.utdanningsoversikten

import android.content.ContentValues.TAG
import android.content.Intent
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

        kravkodeTextView.text = education.QualificationCode
        poenggrenseTextView.text = education.pointsRequired.toString()
        detailEducationDescriptionTextView.text = education.descriptionLong


        // Setter bilde på floatingButton etter om den finnes i favoritter
        if (Education.favouriteEducationlist.contains(education)) {
            favFloatingButton.setImageResource(R.drawable.ic_floating_button_stroke)
        } else {
            favFloatingButton.setImageResource(R.drawable.ic_floating_button_fill)
        }


        favFloatingButton.setOnClickListener {
            if (firebaseCurrentUser == null) {
                Toast.makeText(
                    context,
                    "Du må være innlogget for å kunne legge til utdanninger i favoritter",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                if (Education.favouriteEducationlist.contains(education)) {
                    favFloatingButton.setImageResource(R.drawable.ic_floating_button_fill)

                    removeFavFromFirestore(firebaseCurrentUser, education)

                    Toast.makeText(
                        context,
                        education.educationTitle + " er fjernet fra favoritter",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    favFloatingButton.setImageResource(R.drawable.ic_floating_button_stroke)

                    addFavToFirestore(firebaseCurrentUser, education)

                    Toast.makeText(
                        context,
                        education.educationTitle + " er lagt til i favoritter",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun addFavToFirestore(firebaseCurrentUser : FirebaseUser?, education : Education) {

        firestoreDb.collection("favourites").document(firebaseCurrentUser!!.email.toString()).collection("favList")
            .document(education.id.toString())
            .set(education)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    private fun removeFavFromFirestore(firebaseCurrentUser: FirebaseUser, education: Education) {

        firestoreDb.collection("favourites").document(firebaseCurrentUser.email.toString()).collection("favList").document(education.id.toString())
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

    }

    fun openWebBroser(url : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
