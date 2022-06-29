package com.example.petder.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petder.Model.*
import com.example.petder.R
import com.example.petder.Remote.Post
import com.example.petder.SettingActivity.Request


class RequestRecyclerAdapter(listData: List<RequestResponse>, activity: Request) :
    RecyclerView.Adapter<RequestRecyclerAdapter.ViewHolder>() {

    private var listData = listData
    private lateinit var view: View
    private var activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.request_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = listData[position]

        holder.tvName.text = data.name
        holder.tvBreed.text = data.breed
        Glide.with(view.context).load(data.imageUrl).centerCrop().into(holder.profileCat)

        holder.btAddFriend.setOnClickListener {
            Post().acceptRequest(data.requestListId)
            activity.notifyDataSet(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Initiate view ID
        var profileCat = itemView.findViewById<ImageView>(R.id.ivProfile_request)
        var tvName = itemView.findViewById<TextView>(R.id.tvName_request)
        var tvBreed = itemView.findViewById<TextView>(R.id.tvBreed_request)
        var btAddFriend = itemView.findViewById<ImageView>(R.id.ivAdd_request)
    }
}