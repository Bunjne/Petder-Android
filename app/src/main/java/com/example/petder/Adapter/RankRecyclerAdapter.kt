package com.example.petder.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petder.Model.RankInfo
import com.example.petder.Model.RankInfoResponse
import com.example.petder.R


class RankRecyclerAdapter(listData: List<RankInfoResponse>) :
    RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder>() {

    //    private var listData = listData
    private var listData = listData
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.rank_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listData.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRank.text = "${position + 1}"
        holder.tvNumLike.text = listData[position].numberOfLikes.toString()
        holder.tvName.text = listData[position].name
        if (listData[position].imageUrl != "")
            Glide.with(view.context).load(listData[position].imageUrl).centerCrop()
                .into(holder.profileCat)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Initiate view ID
        var profileCat = itemView.findViewById<ImageView>(R.id.ivProfile_rank)
        var tvRank = itemView.findViewById<TextView>(R.id.tvNumber_rank)
        var tvName = itemView.findViewById<TextView>(R.id.tvName_rank)
        var tvNumLike = itemView.findViewById<TextView>(R.id.tvLike_rank)
    }
}