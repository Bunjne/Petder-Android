package com.example.petder.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.example.petder.Model.OtherPetInfoResponse
import com.example.petder.Model.PetInfo1
import kotlinx.android.synthetic.main.slide_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class HomeArrayAdapter(
    context: Context, @LayoutRes private val layoutResource: Int,
    private val dataList: ArrayList<OtherPetInfoResponse>
) :
    ArrayAdapter<OtherPetInfoResponse>(context, layoutResource, dataList) {
    var baseContext = context
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(layoutResource, parent, false)
        var data = dataList[position]
        var random = Random()
        var randomNum = random.nextInt(data.imageUrls.size)
        view.tvName_home.text = data.name
        view.tvBreed_home.text = data.breed
        view.tvLocation_home.text = data.address
        Glide.with(baseContext).load(data.imageUrls[randomNum]).centerCrop()
            .into(view.ivProfile_home)
        return view
    }
}