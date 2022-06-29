package com.example.petder.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petder.*
import com.example.petder.Fragment.currentpetId
import com.example.petder.Model.AllPetResponse
import com.example.petder.Model.PetInfo1
import com.example.petder.Remote.Post
import java.util.*


class SelectionRecyclerAdapter(petInfo: List<AllPetResponse>, activity: SelectionActivity) :
    RecyclerView.Adapter<SelectionRecyclerAdapter.ViewHolder>() {

    private var dataList = petInfo
    private var activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.selection_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = dataList[position].name
        holder.tvBreed.text = dataList[position].breed
        Glide.with(activity).load(dataList[position].imageUrl).centerCrop().into(holder.profileCat)

        holder.layout.setOnClickListener {
            // change current pet
            var changeCurrentStatus = Post().changeCurrentPet(currentpetId, dataList[position].petId)
            if (changeCurrentStatus == 200)
                activity.backToPreviousPage()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Initiate view ID
        var profileCat: ImageView = itemView.findViewById(R.id.ivProfile_selection)
        var tvName: TextView = itemView.findViewById(R.id.tvName_selection)
        var tvBreed: TextView = itemView.findViewById(R.id.tvBreed_selection)
        var layout: ConstraintLayout = itemView.findViewById(R.id.layout_selection)
    }
}