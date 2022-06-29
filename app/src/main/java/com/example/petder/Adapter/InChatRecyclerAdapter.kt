package com.example.petder.Adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.example.petder.DateTime.DateTime
import com.example.petder.Fragment.currentpetId
import com.example.petder.Model.AllMessages
import com.example.petder.R
import com.example.petder.Remote.Post


class InChatRecyclerAdapter(mMessageList: ArrayList<AllMessages>) :
    RecyclerView.Adapter<InChatRecyclerAdapter.ViewHolder>() {

    private var dataList = mMessageList
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.in_chat_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var sender = dataList[position].senderName
        var isUnsent = dataList[position].isUnsent
        var isSystemMessage = dataList[position].isSystemMessage
        var senderId = dataList[position].senderId
        var time = DateTime().convertUTCToGMT(dataList[position].sendDateTime)
        var profileUrl = dataList[position].senderImageUrl
        var messageId = dataList[position].messageId
        var message = dataList[position].message
        // hold message to unSend
        holder.tvMessage.setOnLongClickListener {
            holder.layout.removeAllViews()
            var unSendMessage = createUnsentMessage("$sender unsent a message.")
            holder.layout.gravity = Gravity.CENTER
            holder.layout.addView(unSendMessage)
            Post().unsentMessage(messageId)
            isUnsent = true
            true
        }

        if (isUnsent) {     // check unsent message
            holder.layout.removeAllViews()
            var unSendMessage = createUnsentMessage("$sender unsent a message.")
            holder.layout.gravity = Gravity.CENTER
            holder.layout.addView(unSendMessage)
        } else if (isSystemMessage) {
            holder.layout.removeAllViews()
            var unSendMessage = createUnsentMessage(message)
            holder.layout.gravity = Gravity.CENTER
            holder.layout.addView(unSendMessage)
        } else {
            if (senderId != currentpetId) { //isSender
                holder.tvTime.text = time
                Glide.with(view.context).load(profileUrl).centerCrop().into(holder.profileCat)
                val param = holder.layout.layoutParams as LayoutParams
                param.rightMargin = 100
                holder.layout.layoutParams = param
            } else {
                holder.profileCat.visibility = View.GONE
                holder.tvTime.visibility = View.GONE
                holder.tvMessage.background = view.resources.getDrawable(R.drawable.sky_blue_border_24dp)

                // create time textView and put add into layout
                // set margin and params of layout
                var tvTime = TextView(view.context)
                tvTime.text = "12:00"
                tvTime.textSize = 12.0f
                tvTime.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                tvTime.setTextColor(view.resources.getColor(R.color.grey))
                holder.layout.gravity = Gravity.RIGHT
                if (holder.layout.size != 4)
                    holder.layout.addView(tvTime, 0)
                val param = holder.layout.layoutParams as LayoutParams
                param.leftMargin = 100
                holder.layout.layoutParams = param

                tvTime.text = time
            }
            holder.tvMessage.text = message
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Initiate view ID
        var profileCat: ImageView = itemView.findViewById(R.id.ivProfile_inChat)
        var tvMessage: TextView = itemView.findViewById(R.id.tvMessage_inChat)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime_inChat)
        var layout = itemView.findViewById<LinearLayout>(R.id.layout_inChat)
    }

    private fun createUnsentMessage(message: String): TextView {
        var unSentMessage = TextView(view.context)
        unSentMessage.text = "$message"
        unSentMessage.textSize = 12.0f
        unSentMessage.background = view.resources.getDrawable(R.drawable.sky_blue_border_24dp)
        unSentMessage.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        unSentMessage.setTextColor(view.resources.getColor(R.color.grey))
        return unSentMessage
    }
}