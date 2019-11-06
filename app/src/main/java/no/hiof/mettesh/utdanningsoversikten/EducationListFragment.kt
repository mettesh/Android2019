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



        // TODO: AutoOnComplete?? 

        //Sender med contexten, som jeg evt er opprettet. Derav !!

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

        searchButton.setOnClickListener {

            if(spinnerLevel.selectedItem.toString() != "Nivå"){
               // adapter.filter.filter(spinnerLevel.selectedItem.toString())
            }
            if(spinnerStudyField.selectedItem.toString() != "Fagområde"){
               // adapter.filter.filter(spinnerStudyField.selectedItem.toString())
            }
            if(spinnerPlace.selectedItem.toString() != "Sted"){
               // adapter.filter.filter(spinnerPlace.selectedItem.toString())
            }

            dialog.hide()
        }

        resetText.setOnClickListener {
            // Skal nullstille alle feltene!

            setUpRecycleView(educationList)
            dialog.hide()
        }



        // TODO: Endre til vanlig inputfelt.

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(searchInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredModelList = filter(educationList, searchInput.toString())
                setUpRecycleView(filteredModelList)
            }

            override fun onTextChanged(searchInput: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filteredModelList = filter(educationList, searchInput.toString())
                setUpRecycleView(filteredModelList)
            }

        })

    }

    private fun filter(educationList: List<Education>, searchInput: String): List<Education> {

        val filteredList = ArrayList<Education>()
        if(searchInput.isEmpty()){
            return educationList

        } else {
            for(row : Education in educationList){
                if(educationContainsString(row, searchInput.toLowerCase())) {
                    filteredList.add(row)
                }
            }
        }
        return filteredList
    }

    fun educationContainsString(row : Education, charSearch : String): Boolean {
        return row.educationTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                row.school.schoolTitle.toLowerCase().contains(charSearch.toLowerCase()) ||
                row.descriptionLong.toLowerCase().contains(charSearch.toLowerCase()) ||
                row.descriptionShort.toLowerCase().contains(charSearch.toLowerCase())
    }

    private fun fillSpinners(spinnerLevel : Spinner, spinnerStudyField : Spinner, spinnerPlace : Spinner) {

        val levelList = arrayOf("Nivå", "Årsstudium", "Bachelor", "Master")
        val studyField = arrayOf("Fagområde", "Helse", "Historie", "Håndverk", "Idrett", "Kjemi", "Kultur")
        val levelPlace = arrayOf("Sted", "Oslo", "Halden", "Fredrikstad")


        val levelAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, levelList)
        spinnerLevel.adapter = levelAdapter

        val fieldAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, studyField)
        spinnerStudyField.adapter = fieldAdapter

        val placeAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, levelPlace)
        spinnerPlace.adapter = placeAdapter

/*        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }*/



        // Fylle lister!
    }
}
