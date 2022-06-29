package com.example.petder


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.example.petder.Adapter.InChatRecyclerAdapter
import com.example.petder.Fragment.currentpetId
import com.example.petder.Model.AllMessages
import com.example.petder.Remote.Get
import com.example.petder.Remote.Post
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var allMessages = ArrayList<AllMessages>()
    private var sessionId = -1
    private var receiverPetId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recyclerView = findViewById(R.id.recycler_inChat)

        tvToolbar_inChat.text = "Meme"
        toolbar_inChat.setNavigationIcon(resources.getDrawable(R.drawable.white_back))
        toolbar_inChat.setNavigationOnClickListener {
            finish()
        }

        sessionId = intent.getStringExtra("sessionId")!!.toInt()
        intent.getStringExtra("receiverId")!!.toInt()
        var response = Get().getAllMessages(sessionId, currentpetId, receiverPetId)
        allMessages = ArrayList(response)
        if (allMessages.isNotEmpty())
            setRecyclerView(allMessages)

        ivSend_inChat.setOnClickListener {
            sendMessage()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setRecyclerView(listData: ArrayList<AllMessages>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        viewAdapter = InChatRecyclerAdapter(listData)
        viewAdapter.setHasStableIds(true)
        recyclerView.adapter = viewAdapter
    }

    private fun sendMessage() {
        var message = etMessage_inChat.text.toString()
        if (message != "") {
            var sendStatus = Post().sendMessage(sessionId, message, currentpetId)
            if (sendStatus != null) {
                println(sendStatus)
                allMessages.add(0,AllMessages("", ""
                    , sendStatus.sender_pet_id, sendStatus.sent_datetime, message, false
                    , false,sendStatus.message_id))
                val smoothScroller: SmoothScroller = object : LinearSmoothScroller(this) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                smoothScroller.targetPosition = 0
                recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                viewAdapter.notifyItemInserted(0)
                etMessage_inChat.text = null
            }
        }
    }
}
