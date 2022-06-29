package com.example.petder.SettingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petder.Adapter.RankRecyclerAdapter
import com.example.petder.Model.RankInfoResponse
import com.example.petder.R
import com.example.petder.Remote.Get
import kotlinx.android.synthetic.main.activity_rank.*

class Rank : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        recyclerView = findViewById(R.id.recyclerView_rank)

        var rankInfo = Get().getRank()
        setRecyclerView(rankInfo)

        ivBack_rank.setOnClickListener {
            finish()
        }
    }

    private fun setRecyclerView(listData: List<RankInfoResponse>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewAdapter = RankRecyclerAdapter(listData)
        recyclerView.adapter = viewAdapter
    }
}

