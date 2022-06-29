package com.example.petder.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petder.Adapter.ChatRecyclerAdapter
import com.example.petder.ChatActivity
import com.example.petder.Model.Session
import com.example.petder.R
import com.example.petder.Remote.Get

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var allSessions: ArrayList<Session> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_chat, null)
        recyclerView = view.findViewById(R.id.recyclerView_chat)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var response = Get().getAllSessions(currentpetId)
        allSessions = ArrayList(response)
        if (allSessions.isNotEmpty())
            setRecyclerView(allSessions)
    }

    override fun onResume() {
//        viewAdapter.notifyDataSetChanged()
        super.onResume()
    }

    private fun setRecyclerView(infoData: ArrayList<Session>) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setItemViewCacheSize(10)
        viewAdapter = ChatRecyclerAdapter(infoData, this)
        viewAdapter.hasStableIds()
        viewAdapter.notifyDataSetChanged()
        recyclerView.adapter = viewAdapter
    }

    internal fun openActivity(sessionId:Int,receiverId:Int) {
        var intent = Intent(activity, ChatActivity()::class.java)
        intent.putExtra("sessionId",sessionId.toString()) // must be String instead of Int because getStringExtra
        intent.putExtra("receiverId",receiverId.toString())
        startActivity(intent)
    }
}
