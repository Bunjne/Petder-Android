package com.example.petder.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.petder.LoginActivity
import com.example.petder.Model.CurrentPetResponse
import com.example.petder.R
import com.example.petder.Remote.Get
import com.example.petder.Remote.Post
import com.example.petder.SelectionActivity
import com.example.petder.SettingActivity.Filter
import com.example.petder.SettingActivity.ProfileEditing
import com.example.petder.SettingActivity.Rank
import com.example.petder.SettingActivity.Request
import com.example.petder.userID
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

var currentpetId = -1
class ProfileFragment : Fragment() {

    private val PICK_IMAGE = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    private var currentPet: CurrentPetResponse? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_profile, null)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        storageReference = FirebaseStorage.getInstance().reference

        btEdit_profile.setOnClickListener {
            var intent = Intent(activity, ProfileEditing::class.java)
            startActivity(intent)
        }

        btFilter__profile.setOnClickListener {
            var intent = Intent(activity, Filter::class.java)
            startActivity(intent)
        }

        btRequest.setOnClickListener {
            var intent = Intent(activity, Request::class.java)
            startActivity(intent)
        }

        btRank_profile.setOnClickListener {
            var intent = Intent(activity, Rank::class.java)
            startActivity(intent)
        }

        ivSwap_profile.setOnClickListener {
            openActivity(SelectionActivity())
        }
        btLogout_profile.setOnClickListener {
            var intent = Intent(activity, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        ivBtEdit_profile.setOnClickListener {
            openGallery()
        }
    }

    override fun onResume() {
        super.onResume()
        setProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            var imageUri = data!!.data
            ivCatProfile.setImageURI(imageUri)
            filePath = imageUri
            uploadImage()
        }
    }

    private fun openActivity(toActivity: Activity) {
        var intent = Intent(activity, toActivity::class.java)
        startActivity(intent)
    }

    private fun setProfile() {
        currentPet = Get().getCurrentPet(userID)
        currentpetId = currentPet!!.petId
        tvName_profile.text = currentPet!!.name
        tvBreed_profile.text = currentPet!!.breed
        tvLocation_profile.text = currentPet!!.address
        Glide.with(this).load(currentPet!!.imageUrl).into(ivCatProfile)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    private fun uploadImage() {
        val ref = storageReference?.child("Balloon/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(filePath!!)

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
                println(downloadUri)
                val uploadResponse = Post().changeProfileImage(currentPet!!.imageId, downloadUri!!.toString())
                println(uploadResponse)
//                if(uploadResponse == 200)
//                    Glide.with(this).load(downloadUri).into(ivCatProfile)
            } else {
                // Handle failures
                Log.e("URL", "Error occurs during uploading image")
            }
        }
    }

}

