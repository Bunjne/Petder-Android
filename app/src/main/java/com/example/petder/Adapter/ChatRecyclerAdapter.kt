package com.example.petder.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petder.DateTime.DateTime
import com.example.petder.Fragment.ChatFragment
import com.example.petder.Model.Session
import com.example.petder.R
import kotlin.collections.ArrayList


class ChatRecyclerAdapter(chatInfo: ArrayList<Session>, fragment: ChatFragment) :
    RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>() {

    private var dataList = chatInfo
    private var chatFragment = fragment
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.chat_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = dataList[position].name
        holder.tvMessage.text = dataList[position].message
        holder.tvTime.text = DateTime().convertUTCToGMT(dataList[position].sentDateTime)
        Glide.with(view.context).load(dataList[position].imageUrl).centerCrop().into(holder.profileCat)
        holder.layout.setOnClickListener {
            var sessionId = dataList[position].sessionId
            var receiverId = dataList[position].receiverPetId
            chatFragment.openActivity(sessionId, receiverId)

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Initiate view ID
        var profileCat: ImageView = itemView.findViewById(R.id.ivProfile_chat)
        var tvMessage: TextView = itemView.findViewById(R.id.tvMessage_chat)
        var tvName: TextView = itemView.findViewById(R.id.tvName_chat)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime_chat)
        var layout: ConstraintLayout = itemView.findViewById(R.id.constraint_chat)
    }
}