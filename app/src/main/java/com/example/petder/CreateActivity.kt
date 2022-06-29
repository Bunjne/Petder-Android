package com.example.petder

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.setPadding
import com.example.petder.Model.ImageURL
import com.example.petder.Model.PetInfo
import com.example.petder.Remote.Get
import com.example.petder.Remote.Post
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_profile_editing.*
import java.util.*
import kotlin.collections.ArrayList

class CreateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var filePath: ArrayList<Uri> = ArrayList()
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 71
    private var clickedImage: ArrayList<ImageView> = ArrayList()
    private var imageURLFromStorage: ArrayList<ImageURL> = ArrayList()
    private var currentPositionImage = -1
    private var imageViewList: ArrayList<ImageView> = ArrayList()
    private var allBreed = Get().getAllBreed()
    private lateinit var spinnerBreeding: Spinner
    private var selectedBreedId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        storageReference = FirebaseStorage.getInstance().reference

        imageViewList = arrayListOf(
            ivEdit_create, ivEdit_2_create,
            ivEdit_3_create, ivEdit_4_create, ivEdit_5_create, ivEdit_6_create
        )

        ivBack_create.setOnClickListener {
            finish()
        }

        etAge_create.setOnClickListener { showDatePickerDialog() }

        for (i in 0 until imageViewList.size) {
            imageViewList[i].setOnClickListener {
                currentPositionImage = i
                launchGallery()
            }
        }

        setSpinner()
        editInfo()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            R.id.spinnerBreeding_create -> selectedBreedId = allBreed[position].breedId
        }
    }

    private fun setSpinner() {
        val breedingArray = ArrayList<String>()
        allBreed.map { breedingArray.add(it.name) }
        spinnerBreeding = this.spinnerBreeding_create
        val breedAdapter = ArrayAdapter(this, R.layout.custom_spinner, breedingArray)
        breedAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinnerBreeding.adapter = breedAdapter

        spinnerBreeding.onItemSelectedListener = this

    }

    private fun editInfo() {
        btDone_create.setOnClickListener {

            var name = etName_create.text.toString()
            var age = etAge_create.text.toString()
            var gender = etGender_create.text.toString()
            var description = etDescription_create.text.toString()
            var userList = arrayListOf(name, selectedBreedId, age, description, gender)
            if (!userList.contains("")) {
                var petInfo = PetInfo(name, selectedBreedId, age, description, gender, imageURLFromStorage)
                uploadImage(petInfo)
            } else {
                Toast.makeText(this, "Invalid Information", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        var calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)

        var picker = DatePickerDialog(
            this,
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                etAge_create.setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
            },
            year,
            month,
            day
        )
        picker.show()
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            imageViewList[currentPositionImage].setImageURI(data.data)
            imageViewList[currentPositionImage].setPadding(0)
            clickedImage.add(imageViewList[currentPositionImage])
            filePath.add(data.data!!) // to be used in uploadImage
        }
    }

    private fun uploadImage(petInfo: PetInfo) {
        var isProfile = false
        for (i in clickedImage.indices) {

            val ref = storageReference?.child("Balloon/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath[i])

            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    if (i == 0) isProfile = true

                    imageURLFromStorage.add(ImageURL(downloadUri.toString(), isProfile))
                    if (i == clickedImage.lastIndex) {
                        petInfo.PetImages = imageURLFromStorage
                        postNewCat(petInfo)
                    }
//                        Log.e("URL", downloadUri.toString())
                } else {
                    // Handle failures
                    Log.e("URL", "Error occurs during uploading image")
                }
            }
        }
    }

    private fun postNewCat(petInfo: PetInfo) {
        var json = Gson().toJson(petInfo)
        var createResponse = Post().createNewCat(userID, json)

        if (createResponse == 200) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // remove current activity from stack
        }
    }
}
