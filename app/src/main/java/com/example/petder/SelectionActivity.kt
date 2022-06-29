package com.example.petder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petder.Adapter.SelectionRecyclerAdapter
import com.example.petder.Model.AllPetResponse
import com.example.petder.Remote.Delete
import com.example.petder.Remote.Get
import com.example.petder.Remote.Post
import com.example.petder.SettingActivity.ProfileEditing
import kotlinx.android.synthetic.main.activity_selection.*
import kotlinx.android.synthetic.main.fragment_profile.*


class SelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var allPetResponse: ArrayList<AllPetResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        recyclerView = findViewById(R.id.recyclerView_selection)
        tvToolbar_selection.text = "Selection Cat"
        toolbar_selection.navigationIcon = resources.getDrawable(R.drawable.white_back)

        var response = Get().getAllPetExceptCurrent(userID)
        allPetResponse = ArrayList(response)

        if (allPetResponse.isNotEmpty())
            setRecyclerView(allPetResponse)

        toolbar_selection.setNavigationOnClickListener {
            backToPreviousPage()
        }

        ibtDelete_selection.setOnClickListener {
            var deleteStatus = Delete().deletePetById(allPetResponse.last().petId)
            if (deleteStatus == 200) {
                allPetResponse.removeAt(allPetResponse.lastIndex)
                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Delete successfully", Toast.LENGTH_LONG).show()
            }
        }

        ibtAdd_selection.setOnClickListener {
            if (allPetResponse.size < 6) {
                var intent = Intent(this, CreateActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Each account can have only 6 pets", Toast.LENGTH_LONG).show()
            }
        }

    }

    internal fun backToPreviousPage() {
        finish()
    }

    private fun setRecyclerView(allPetResponse: List<AllPetResponse>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewAdapter = SelectionRecyclerAdapter(allPetResponse, this)
        recyclerView.adapter = viewAdapter
    }
}
