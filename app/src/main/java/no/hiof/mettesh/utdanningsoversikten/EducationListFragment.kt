package no.hiof.mettesh.utdanningsoversikten

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
import android.R.id.edit
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.bottom_sheet.*
import no.hiof.mettesh.utdanningsoversikten.model.School
import java.util.Collections.replaceAll




class EducationListFragment : Fragment() {

    lateinit var adapter: EducationAdapter

    // Henter inn liste med utdanninger fra Education-klassen
    private var educationList : ArrayList<Education> = Education.educationlist


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //adapter = EducationAdapter(context!!, educationList
        // Henter inn layout for dette Fragmentet
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
    }



    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }

    private fun setUpRecycleView(educationList: List<Education>) {

            // Bruker adapteren for å binde layout og RecyclerView
        educationRecyclerView.adapter = EducationAdapter(educationList, View.OnClickListener { view ->


                // Får posisjonen til elementet i lista som er trykket på
                val position = educationRecyclerView.getChildAdapterPosition(view)

                // Får deretter riktig utdannning paserte på denne posisjonen
                val clickedEducation = educationList[position]

                // Oppretter navigasjonen (utifra nav_graph.xml) og sender med id til utdanningen.
                val action = EducationListFragmentDirections.actionEducationListFragmentToEducationDetailFragment(clickedEducation.id)

                // Navigerer til EducationDetailFragment
                findNavController().navigate(action)


            })

        adapter = educationRecyclerView.adapter as EducationAdapter

        // Etter at alt er satt setter vi dette RecyclerView med det layouten vi ønsker.
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

        fillSpinners(spinnerLevel, spinnerStudyField, spinnerPlace)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(searchInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredModelList = filterFromSearch(educationList, searchInput.toString())
                setUpRecycleView(filteredModelList)
            }

            override fun onTextChanged(searchInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredModelList = filterFromSearch(educationList, searchInput.toString())
                setUpRecycleView(filteredModelList)
            }

        })

        searchButton.setOnClickListener {

            // Om det er noe valgt i nedtrekkslisten blir dette hetet ut, ellers hentes "" ut
            val chosenLevel = if(spinnerLevel.selectedItem.toString().equals("Nivå")) "" else spinnerLevel.selectedItem.toString()
            val chosenStudyField = if(spinnerStudyField.selectedItem.toString().equals("Fagområde")) "" else spinnerStudyField.selectedItem.toString()
            val chosenPlace = if(spinnerPlace.selectedItem.toString().equals("Sted")) "" else spinnerPlace.selectedItem.toString()

            val filteredList = filterFromSpinners(chosenLevel, chosenStudyField, chosenPlace)

            setUpRecycleView(filteredList)

            dialog.hide()
        }

        resetText.setOnClickListener {
            setUpRecycleView(educationList)
            dialog.hide()
        }
    }

    private fun filterFromSpinners(chosenLevel : String, chosenStudyField : String, chosenPlace : String): List<Education>{

        val filteredList = ArrayList<Education>()

        for(education : Education in educationList){

            if(educationInfoContainsChosenSpinnersInfo(education, chosenPlace, chosenLevel, chosenStudyField)){
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

    fun educationContainsString(edu : Education, charSearch : String): Boolean {
        return edu.educationTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                edu.school.schoolTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                edu.descriptionLong.toLowerCase().contains(charSearch.toLowerCase()) ||
                edu.descriptionShort.toLowerCase().contains(charSearch.toLowerCase())
    }

    private fun educationInfoContainsChosenSpinnersInfo(education: Education, chosenPlace: String, chosenLevel: String, chosenStudyField: String): Boolean {

        // TODO: Finne steder basert på zip-code!!
        return education.school.place.contains(chosenPlace)
                && education.level.contains(chosenLevel)
                && education.studyField.contains(chosenStudyField)

    }

    private fun fillSpinners(spinnerLevel : Spinner, spinnerStudyField : Spinner, spinnerPlace : Spinner) {

//        val levelList = arrayOf("Nivå", "Videregående", "Årsenhet", "Bachelorgrad", "Mastergrad", "Profesjonsstudium", "Forskerutdanning", "Andre")

//        val studyField = arrayOf("Fagområde", "Lærer", "Journalistikk","Pedagogikk","Matematisk-naturvitenskapelig/informatikk",
//            "Historisk-filosofi","Design","Videregående","Samfunnsvitenskap","Helsefag","Barnevern","Økonomi","Idrett","Juss",
//            "Sosionom","Ernæring","Ergoterapi","Fysioterapi","Radiografi","Ingeniør","Teknologi","Døvetolk","Scene- og visuellkunst",
//            "Musikk","Maritim","Landbruk","Psykologi","Miljø","Tannpleie","Odontologi","Medisin","Farmasi","Teologi","Fiskeri","Kunst",
//            "Arkitektur","Audiograf","Veterinær og dyrepleie","Reseptar","Bibliotekar","Politi","Militær","Militær","Andre"
//        )

        // Fyller listene etter dataen som er tilgjengelig:

        val levelList = ArrayList<String>()
        val studyField = ArrayList<String>()
        val place = ArrayList<String>()

        levelList.add("Nivå")
        studyField.add("Fagområde")
        place.add("Sted")

        for(education in Education.educationlist){

            if(!studyField.contains(education.studyField)){
                studyField.add(education.studyField)
            }

            if(!levelList.contains(education.level)){
                levelList.add(education.level)
            }
        }

        for(school in School.schoolList){
            if(!place.contains(school.place)){
                place.add(school.place)
            }
        }

        val levelAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, levelList)
        spinnerLevel.adapter = levelAdapter

        val fieldAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, studyField)
        spinnerStudyField.adapter = fieldAdapter

        val placeAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, place)
        spinnerPlace.adapter = placeAdapter

    }
}
