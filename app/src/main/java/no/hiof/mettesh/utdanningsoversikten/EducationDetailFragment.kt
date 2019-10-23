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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_education_detail.view.*
import no.hiof.mettesh.utdanningsoversikten.model.Education

class EducationDetailFragment : Fragment() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var favouritesCollectionReference: CollectionReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Henter inn layout for dette Fragmentet
        return inflater.inflate(R.layout.fragment_education_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments?.let { EducationDetailFragmentArgs.fromBundle(it) }
        val education = Education.educationlist[arguments!!.id]

        firebaseAuth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()

        val firebaseCurrentUser = firebaseAuth.currentUser
        favouritesCollectionReference = firestoreDb.collection("favourites")

        val detailEducationTitleTextView : TextView = view.detailEducationTitle
        val detailSchoolNameTextView : TextView = view.detailSchoolName
        val sendToWebImgView : ImageView = view.sendToWebImgView
        val detailEducationDescriptionTextView : TextView = view.detailEducationDescription
        val poenggrenseTextView : TextView = view.detailPoenggrense
        val kravkodeTextView : TextView = view.detailKravkode
        val favFloatingButton : FloatingActionButton = view.floatingButton_fav
        val schoolUrl : String = education.school.web


        detailEducationTitleTextView.text = education.title
        detailSchoolNameTextView.text = education.school.schoolTitle

        detailSchoolNameTextView.setOnClickListener {
            openWebBroser(schoolUrl)
        }

        sendToWebImgView.setOnClickListener {
            openWebBroser(schoolUrl)
        }

        kravkodeTextView.text = education.kravkode
        poenggrenseTextView.text = education.poenggrense.toString()
        detailEducationDescriptionTextView.text = education.descriptionLong



        // Setter bilde på floatingButton etter om den finnes i favoritter
        if(Education.favouriteEducationlist.contains(education)) {
            favFloatingButton.setImageResource(R.drawable.ic_floating_button_fill)
        } else {
            favFloatingButton.setImageResource(R.drawable.ic_floating_button_stroke)
        }


        // Logikk for hva som skal skje når floatingButton trykkes på
        favFloatingButton.setOnClickListener {
            if (firebaseCurrentUser == null) {
                Toast.makeText(
                    context,
                    "Du må være innlogget for å kunne legge til utdanninger i favoritter",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                if (Education.favouriteEducationlist.contains(education)) {
                    favFloatingButton.setImageResource(R.drawable.ic_floating_button_stroke)

                    Education.favouriteEducationlist.remove(education)

                    updateFavToFirestore(firebaseCurrentUser)

                    Toast.makeText(
                        context,
                        education.title + " er fjernet fra favoritter",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    favFloatingButton.setImageResource(R.drawable.ic_floating_button_fill)

                    //Trenger ikke legge til i listen her. Listen skal fylles fra Firestore
                    Education.favouriteEducationlist.add(education)

                    updateFavToFirestore(firebaseCurrentUser)

                    Toast.makeText(
                        context,
                        education.title + " er lagt til i favoritter",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }

    private fun updateFavToFirestore(firebaseCurrentUser : FirebaseUser?) {


        val fav = hashMapOf(
            "fav" to Education.favouriteEducationlist
        )

        firestoreDb.collection("favourites").document(firebaseCurrentUser!!.email.toString())
            .set(fav)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }

    fun openWebBroser(url : String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
