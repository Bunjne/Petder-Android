package com.example.petder.SettingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petder.Adapter.RequestRecyclerAdapter
import com.example.petder.Fragment.currentpetId
import com.example.petder.Model.PetInfo1
import com.example.petder.Model.RequestResponse
import com.example.petder.R
import com.example.petder.Remote.Get
import com.example.petder.userID
import kotlinx.android.synthetic.main.activity_request.*

class Request : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var requestList: ArrayList<RequestResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        ivBack_request.setOnClickListener {
            finish()
        }
        recyclerView = findViewById(R.id.recyclerView_request)

        var response = Get().getRequest(currentpetId)
        requestList = ArrayList(response)

        if (requestList.isNotEmpty())
            setRecyclerView(requestList)
    }

    private fun setRecyclerView(listData: List<RequestResponse>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewAdapter = RequestRecyclerAdapter(listData, this)
        recyclerView.adapter = viewAdapter
    }

    fun notifyDataSet(position: Int) {
        requestList.removeAt(position)
        viewAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Accept successfully", Toast.LENGTH_LONG).show()
    }
}
