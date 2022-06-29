package com.example.petder.SettingActivity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.example.petder.*
import com.example.petder.DateTime.DateTime
import com.example.petder.Model.ImageURLEdit
import com.example.petder.Model.PetInfoEdit
import com.example.petder.Remote.Get
import com.example.petder.Remote.Post
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile_editing.*
import java.util.*
import kotlin.collections.ArrayList


class ProfileEditing : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var filePath: ArrayList<Uri> = ArrayList()
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 71
    private var currentPositionImage = -1
    private var selectedImagePosition: ArrayList<Int> = ArrayList()
    private var imageViewList: ArrayList<ImageView> = ArrayList()
    private var petInfoEdit = Get().getPetInfoEdit(userID)
    private var allBreed = Get().getAllBreed()
    private lateinit var spinnerBreeding: Spinner
    private var selectedBreedId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_editing)

        storageReference = FirebaseStorage.getInstance().reference

        imageViewList = arrayListOf(
            ivEdit_editing, ivEdit_2_editing,
            ivEdit_3_editing, ivEdit_4_editing, ivEdit_5_editing, ivEdit_6_editing
        )

        for (i in 0 until imageViewList.size) {
            imageViewList[i].setOnClickListener {
                currentPositionImage = i
                launchGallery()
            }
        }
        setInfo()
        setSpinner()

        ivBack_editing.setOnClickListener {
            finish()
        }

        etAge_editing.setOnClickListener { showDatePickerDialog() }

        btDone_editing.setOnClickListener {
            editInfo()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Nothing is selected", Toast.LENGTH_LONG).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            R.id.spinnerBreeding_editing -> selectedBreedId = allBreed[position].breedId
        }
    }

    private fun setSpinner() {
        val breedingArray = ArrayList<String>()
        allBreed.map { breedingArray.add(it.name) }
        spinnerBreeding = this.spinnerBreeding_editing
        val breedAdapter = ArrayAdapter(this, R.layout.custom_spinner, breedingArray)
        breedAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinnerBreeding.adapter = breedAdapter

        for(i in breedingArray.indices) {
            if(petInfoEdit.breedId == allBreed[i].breedId) {
                spinnerBreeding.setSelection(i)
                break
            }
        }
        spinnerBreeding.onItemSelectedListener = this

    }

    private fun setInfo() {
        var birthDate = DateTime().formatDateTimeFromString("yyyy-MM-dd", petInfoEdit.birthDateTime)

        for (i in petInfoEdit.petImages.indices) {
            Glide.with(this).load(petInfoEdit.petImages[i].imageUrl)
                .into(imageViewList[i])
            imageViewList[i].setPadding(0)
        }

        etName_editing.setText(petInfoEdit.name)
        etAge_editing.setText(birthDate)
        etDescription_editing.setText(petInfoEdit.address)
        etGender_editing.setText(petInfoEdit.gender)
    }

    private fun editInfo() {
        var name = etName_editing.text.toString()
        var birthDate = etAge_editing.text.toString()
        var gender = etGender_editing.text.toString()
        var description = etDescription_editing.text.toString()
        var userList = arrayListOf(name, selectedBreedId, birthDate, description, gender)
        if (!userList.contains("")) {
            println(selectedBreedId)
            var petInfo = PetInfoEdit(
                selectedBreedId,
                name,
                gender,
                birthDate,
                description,
                true,
                petInfoEdit.address,
                petInfoEdit.petImages
            )
            uploadImage(petInfo)
        } else {
            Toast.makeText(this, "Invalid Information", Toast.LENGTH_LONG).show()
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
                etAge_editing.setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
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
            selectedImagePosition.add(currentPositionImage)
            imageViewList[currentPositionImage].setImageURI(data.data)
            imageViewList[currentPositionImage].setPadding(0)
            filePath.add(data.data!!) // to be used in uploadImage

        }
    }

    private fun uploadImage(petInfoEdit: PetInfoEdit) {
        var isProfile = false
        if (filePath.isNullOrEmpty()) {
            editPetInfo(petInfoEdit)
        } else {
            for (i in filePath.indices) {
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

                        if (selectedImagePosition[i] <= petInfoEdit.petImages.size - 1) {
                            petInfoEdit.petImages[i].imageUrl = downloadUri.toString()
                        } else {
                            petInfoEdit.petImages.add(ImageURLEdit(0, downloadUri.toString(), isProfile))
                        }

                        if (i == filePath.lastIndex) {
                            editPetInfo(petInfoEdit)
                        }
                    } else {
                        // Handle failures
                        Log.e("URL", "Error occurs during uploading image")
                    }
                }
            }
        }
    }

    private fun editPetInfo(obj: PetInfoEdit) {
        var editResponse = Post().editPetInfo(petInfoEdit.userId, petInfoEdit.petId, obj)
        if (editResponse == 200) {
            finish() // remove current activity from stack
        }
    }
}
