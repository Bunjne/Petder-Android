package com.example.petder.SettingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.petder.Model.Breed
import com.example.petder.R
import com.example.petder.Remote.Get
import com.example.petder.filterInfo
import kotlinx.android.synthetic.main.activity_filter.*

class Filter : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerBreeding: Spinner
    private lateinit var spinnerAge: Spinner
    private lateinit var spinnerLocation: Spinner
    private var selectedGender: Pair<Int, String> = Pair(-1, "")
    private var selectedBreeding: Pair<Int, Int> = Pair(-1, 0)
    private var selectedAge: Pair<Int, Any> = Pair(-1, 0)
    private var selectedLocation: Pair<Int, String> = Pair(-1, "")
    private var allFilters = Get().getAllFilters()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        setSpinner()

        ivBack_filter.setOnClickListener { finish() }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            R.id.spinnerGender_filter -> selectedGender =
                Pair(position, spinnerGender.adapter.getItem(position).toString())
            R.id.spinnerBreeding_filter -> selectedBreeding =
                Pair(position, allFilters.breedings[position].breedingId)
            R.id.spinnerAge_filter -> selectedAge = Pair(position, spinnerAge.adapter.getItem(position))
            R.id.spinnerLocation_filter -> selectedLocation =
                Pair(position, spinnerLocation.adapter.getItem(position).toString())
        }
        filterInfo = arrayListOf(selectedGender, selectedBreeding, selectedAge, selectedLocation)
    }

    private fun setSpinner() {
        val genderArray = allFilters.genders
        val ageArray = allFilters.ages
        val locationArray = allFilters.addresses
        val breedingArray = ArrayList<String>()
        allFilters.breedings.map { breedingArray.add(it.breed) }

        genderArray.add(0, "All")
        breedingArray.add(0, "All")
        ageArray.add(0, 0)
        locationArray.add(0, "All")

        spinnerGender = this.spinnerGender_filter
        spinnerBreeding = this.spinnerBreeding_filter
        spinnerAge = this.spinnerAge_filter
        spinnerLocation = this.spinnerLocation_filter

        val genderAdapter = ArrayAdapter(this, R.layout.custom_spinner, genderArray)
        val breedAdapter = ArrayAdapter(this, R.layout.custom_spinner, breedingArray)
        val ageAdapter = ArrayAdapter(this, R.layout.custom_spinner, ageArray)
        val locationAdapter = ArrayAdapter(this, R.layout.custom_spinner, locationArray)

        genderAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        breedAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        ageAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        locationAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)

        spinnerGender.adapter = genderAdapter
        spinnerBreeding.adapter = breedAdapter
        spinnerAge.adapter = ageAdapter
        spinnerLocation.adapter = locationAdapter

        spinnerGender.setSelection(filterInfo[0].first)
        spinnerBreeding.setSelection(filterInfo[1].first)
        spinnerAge.setSelection(filterInfo[2].first)
        spinnerLocation.setSelection(filterInfo[3].first)

        spinnerGender.onItemSelectedListener = this
        spinnerBreeding.onItemSelectedListener = this
        spinnerAge.onItemSelectedListener = this
        spinnerLocation.onItemSelectedListener = this
    }
}
