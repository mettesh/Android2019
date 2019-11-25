package no.hiof.mettesh.utdanningsoversikten

import junit.framework.Assert.assertSame
import junit.framework.Assert.assertTrue
import no.hiof.mettesh.utdanningsoversikten.Fragments.EducationListFragment
import no.hiof.mettesh.utdanningsoversikten.Models.Education
import no.hiof.mettesh.utdanningsoversikten.Models.School
import org.junit.Test

class FilterTests{

    private var testSchool = School(1, "Test school", "TS", "School adress", "0", 0, "0", "", "School place")
    private var testEducation = Education(1, "ABC", "Test Education", "This is a test education", "This is a test education", testSchool, "", 0.0, 0.0, "level", "field")
    private val educationListFragment = EducationListFragment()


    @Test
    fun filter_Check_Returns_True_If_No_Search_Input_From_User() {

        assertTrue(educationListFragment.educationContainsString(testEducation, ""))

    }

    @Test
    fun filter_Check_Returns_True_If_Education_Contains_Search_User_Input() {

        assertTrue(educationListFragment.educationContainsString(testEducation, "Test"))
    }

    @Test
    fun filter_Check_Returns_True_If_No_Input_From_Spinners() {

       assertTrue(educationListFragment.educationInfoContainsChosenSpinnersInfo(testEducation, "", "", ""))
    }

    @Test
    fun filter_Check_Returns_True_If_Education_Contains_Input_From_All_Spinners() {

        assertTrue(educationListFragment.educationInfoContainsChosenSpinnersInfo(testEducation, "place", "level", "field"))
    }

    @Test
    fun filter_Check_Returns_True_If_Education_Contains_Input_From_Only_place_Spinner() {

        assertTrue(educationListFragment.educationInfoContainsChosenSpinnersInfo(testEducation, "place", "", ""))
    }

    @Test
    fun filter_Check_Returns_True_If_Education_Contains_Input_From_Only_Level_Spinner() {

        assertTrue(educationListFragment.educationInfoContainsChosenSpinnersInfo(testEducation, "", "level", ""))
    }

    @Test
    fun filter_Check_Returns_True_If_Education_Contains_Input_From_Only_StudyField_Spinner() {

        assertTrue(educationListFragment.educationInfoContainsChosenSpinnersInfo(testEducation, "", "", "field"))
    }


    @Test
    fun returns_The_Original_Education_List_If_No_User_Search_Input(){

        val testList = ArrayList<Education>()
        testList.add(testEducation)
        testList.add(testEducation)

        assertSame(educationListFragment.filterFromSearch(testList, ""), testList)

    }

    @Test
    fun returns_The_Original_Education_List_If_No_User_Search_Input_And_Nothing_chosen_From_Spinners(){

        Education.favouriteEducationlist.add(testEducation)
        Education.favouriteEducationlist.add(testEducation)

        assertSame(educationListFragment.filterFromSpinners("", "", "", ""), Education.educationlist)

    }


}
