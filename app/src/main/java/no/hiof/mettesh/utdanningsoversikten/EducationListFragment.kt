package no.hiof.mettesh.utdanningsoversikten

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_education_list.*
import kotlinx.android.synthetic.main.fragment_education_list.view.*
import no.hiof.mettesh.utdanningsoversikten.adapter.EducationAdapter
import no.hiof.mettesh.utdanningsoversikten.model.Education
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import no.hiof.mettesh.utdanningsoversikten.model.School


class EducationListFragment : Fragment() {

    private lateinit var adapter: EducationAdapter

    // Henter inn liste med utdanninger fra Education-klassen
    private var educationList : ArrayList<Education> = Education.educationlist

    private var rememberedSearch = ""
    private var rememberedLevelSelection = 0
    private var rememberedStudyFieldSelection = 0
    private var rememberedplaceSelection = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_education_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        val loginOrEmptylistTextview : TextView = view.textView_login_or_empty
        val loginButton : Button = view.login_button
        val openFilterFloatingButton : FloatingActionButton = view.openFilterFloatingButton


        // Skjuler disse da de kun brukes for favouriteFragment (Og samme layout benyttes)
        loginOrEmptylistTextview.visibility = View.GONE
        loginButton.visibility = View.GONE

        openFilterFloatingButton.setOnClickListener {
            viewBottomSheet(view)
        }


        setUpRecycleView(educationList)

        // Sjekk om ingen netttilgang:
        if (!context!!.isConnectedToNetwork()){
            Toast.makeText(context, "OBS! Du er ikke koblet til internett og ser kanskje ikke oppdatert informasjon", Toast.LENGTH_LONG).show()
        }
    }


    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }

    private fun setUpRecycleView(educationList: List<Education>) {

        educationRecyclerView.adapter = EducationAdapter(educationList, View.OnClickListener { view ->

            val position = educationRecyclerView.getChildAdapterPosition(view)
            val clickedEducation = educationList[position]
            val action = EducationListFragmentDirections.actionEducationListFragmentToEducationDetailFragment(clickedEducation.id)
            findNavController().navigate(action)
        })

        adapter = educationRecyclerView.adapter as EducationAdapter

        educationRecyclerView.layoutManager = GridLayoutManager(context, 1)

    }

    private fun viewBottomSheet(view : View) {

        val dialog = BottomSheetDialog(context!!)

        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

        dialog.setContentView(view)
        dialog.show()

        val searchInput : TextInputEditText = view.searchInput
        val spinnerLevel : Spinner = view.spinnerLevel
        val spinnerStudyField : Spinner = view.spinnerFieldStudy
        val spinnerPlace : Spinner = view.spinnerPlace
        val searchButton : Button = view.filtrerButton
        val resetText : TextView = view.resetTextView

        // For å huske hva som er skrevet inn fra tidligere
        if(!rememberedSearch.equals("")){
            searchInput.text = Editable.Factory.getInstance().newEditable(rememberedSearch)
        }

        fillSpinners(spinnerLevel, spinnerStudyField, spinnerPlace)

        // TODO: Setter valgte items for å huske eventuelle valg gjort tidligere OK?
        spinnerLevel.setSelection(rememberedLevelSelection)
        spinnerStudyField.setSelection(rememberedStudyFieldSelection)
        spinnerPlace.setSelection(rememberedplaceSelection)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(searchInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredModelList = filterFromSearch(educationList, searchInput.toString())
                setUpRecycleView(filteredModelList)

                //TODO: Riktig? Tar vare på tekst som er tastet inn
                rememberedSearch = searchInput.toString()
            }

            override fun onTextChanged(searchInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredModelList = filterFromSearch(educationList, searchInput.toString())
                setUpRecycleView(filteredModelList)
                rememberedSearch = searchInput.toString()
            }

        })

        searchButton.setOnClickListener {

            // Om det er noe valgt i nedtrekkslisten blir dette hetet ut, ellers hentes "" ut
            val chosenLevel = if(spinnerLevel.selectedItem.toString().equals("Nivå")) "" else spinnerLevel.selectedItem.toString()
            val chosenStudyField = if(spinnerStudyField.selectedItem.toString().equals("Fagområde")) "" else spinnerStudyField.selectedItem.toString()
            val chosenPlace = if(spinnerPlace.selectedItem.toString().equals("Sted")) "" else spinnerPlace.selectedItem.toString()

            val filteredList = filterFromSpinners(chosenLevel, chosenStudyField, chosenPlace, searchInput.text.toString())

            setUpRecycleView(filteredList)

            rememberedSearch = searchInput.text.toString()
            rememberedLevelSelection = spinnerLevel.selectedItemPosition
            rememberedStudyFieldSelection = spinnerStudyField.selectedItemPosition
            rememberedplaceSelection = spinnerPlace.selectedItemPosition

            dialog.hide()
        }

        resetText.setOnClickListener {

            // Tilbakestiller alle felter
            rememberedSearch = ""
            rememberedLevelSelection = 0
            rememberedStudyFieldSelection = 0
            rememberedplaceSelection = 0

            setUpRecycleView(educationList)
            dialog.hide()
        }
    }

    private fun filterFromSpinners(chosenLevel : String, chosenStudyField : String, chosenPlace : String, searchInput: String): List<Education>{

        val filteredList = ArrayList<Education>()

        for(education : Education in educationList){

            if(educationInfoContainsChosenSpinnersInfo(education, chosenPlace, chosenLevel, chosenStudyField)
                && educationContainsString(education, searchInput)){
                filteredList.add(education)
            }
        }

        return filteredList

    }

    private fun filterFromSearch(educationList: List<Education>, searchInput: String): List<Education> {

        val filteredList = ArrayList<Education>()
        if(searchInput.isEmpty()){
            return educationList

        } else {
            for(education : Education in educationList){
                if(educationContainsString(education, searchInput.toLowerCase())) {
                    filteredList.add(education)
                }
            }
        }
        return filteredList
    }

    private fun educationContainsString(edu : Education, charSearch : String): Boolean {
        return edu.educationTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                edu.school.schoolTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                edu.descriptionLong.toLowerCase().contains(charSearch.toLowerCase()) ||
                edu.descriptionShort.toLowerCase().contains(charSearch.toLowerCase())
    }

    private fun educationInfoContainsChosenSpinnersInfo(education: Education, chosenPlace: String, chosenLevel: String, chosenStudyField: String): Boolean {

        return education.school.place.contains(chosenPlace)
                && education.level.contains(chosenLevel)
                && education.studyField.contains(chosenStudyField)

    }

    private fun fillSpinners(spinnerLevel : Spinner, spinnerStudyField : Spinner, spinnerPlace : Spinner) {

        // Fyller nå isteden listene etter dataen som er tilgjengelig:

        val levelList = ArrayList<String>()
        val studyField = ArrayList<String>()
        val place = ArrayList<String>()


        // Fyller nedtrekkslistene etter de objektene vi har
        for(education in Education.educationlist){

            if(!studyField.contains(education.studyField)){
                studyField.add(education.studyField)
            }

            if(!levelList.contains(education.level)){
                levelList.add(education.level)
            }

            if(!place.contains(education.school.place) && !education.school.place.equals("Ukjent")){
                place.add(education.school.place)
            }
        }

        studyField.sort()
        place.sort()

        place.add(0, "Sted")
        studyField.add(0, "Fagområde")
        levelList.add(0, "Nivå")

        val levelAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, levelList)
        spinnerLevel.adapter = levelAdapter

        val fieldAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, studyField)
        spinnerStudyField.adapter = fieldAdapter

        val placeAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, place)
        spinnerPlace.adapter = placeAdapter

    }

    private fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }
}
