package no.hiof.mettesh.utdanningsoversikten

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import no.hiof.mettesh.utdanningsoversikten.model.Education

class FirebaseFunctions {

    companion object{

        fun addFavToFirestore(firebaseCurrentUser : FirebaseUser?, education : Education) {

            var firestoreDb = FirebaseFirestore.getInstance()

            firestoreDb.collection("favourites").document(firebaseCurrentUser!!.email.toString()).collection("favList")
                .document(education.id.toString())
                .set(education)
                .addOnSuccessListener { Log.d(ContentValues.TAG, "Education successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

            Education.favouriteEducationlist.add(education)
        }

        fun removeFavFromFirestore(firebaseCurrentUser: FirebaseUser, education: Education) {

            var firestoreDb = FirebaseFirestore.getInstance()

            firestoreDb.collection("favourites").document(firebaseCurrentUser.email.toString()).collection("favList").document(education.id.toString())
                .delete()
                .addOnSuccessListener { Log.d(ContentValues.TAG, "Education successfully deleted!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }

            Education.favouriteEducationlist.remove(education)
        }

        fun getDataFromFirestore(firebaseCurrentUser: FirebaseUser) {

            var firestoreDb = FirebaseFirestore.getInstance()

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
    }

}