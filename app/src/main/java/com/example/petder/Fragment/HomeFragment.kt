package com.example.petder.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petder.*
import com.example.petder.Adapter.HomeArrayAdapter
import com.example.petder.Model.*
import com.example.petder.Remote.Get
import com.example.petder.Remote.Post
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private var petDataList = ArrayList<OtherPetInfoResponse>()
    private lateinit var arrayAdapter: HomeArrayAdapter
    private lateinit var flingContainer: SwipeFlingAdapterView
    private var isLike = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, null)
        flingContainer = view.findViewById(R.id.frame_home)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var response = Get().getOtherPetInfo(userID, currentpetId, filterInfo)
        Log.e("check1", filterInfo.toString())
        petDataList = ArrayList(response)
        if (petDataList.isNotEmpty()) {

            arrayAdapter = HomeArrayAdapter(activity!!.baseContext, R.layout.slide_item, petDataList)

            flingContainer.adapter = arrayAdapter

            flingContainer.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
                override fun removeFirstObjectInAdapter() {
                }

                override fun onLeftCardExit(p0: Any?) {
                    petDataList.removeAt(0)
                    arrayAdapter.notifyDataSetChanged()
                    Toast.makeText(activity, "Passes", Toast.LENGTH_LONG).show()
                }

                override fun onRightCardExit(p0: Any?) { // add match_pet and also create greeting message saying 'hello'
                    if (!isLike) {
                        Post().sendRequest(currentpetId, petDataList[0].petId)
                        Toast.makeText(activity, "Matched", Toast.LENGTH_LONG).show()
                    }
                    isLike = false
                    petDataList.removeAt(0)
                    arrayAdapter.notifyDataSetChanged()
                }

                override fun onAdapterAboutToEmpty(p0: Int) {
                }

                override fun onScroll(p0: Float) {
                    var view = flingContainer.selectedView
                    if (p0 < 0) {
                        view.findViewById<View>(R.id.item_swipe_left_indicator).alpha = -p0
                    } else if (p0 > 0) {
                        view.findViewById<View>(R.id.item_swipe_right_indicator).alpha = p0
                    } else {
                        view.findViewById<View>(R.id.item_swipe_left_indicator).alpha = 0F
                        view.findViewById<View>(R.id.item_swipe_right_indicator).alpha = 0F
                    }
                }

            })

            btLeft_home.setOnClickListener {
                if (petDataList.isNotEmpty()) {
                    Post().blockPet(currentpetId, petDataList[0].petId)
                    flingContainer.topCardListener.selectLeft()
                    Toast.makeText(activity, "Blocked!", Toast.LENGTH_LONG).show()
                }
            }
            btRight_home.setOnClickListener {
                if (petDataList.isNotEmpty()) {
                    var status = Post().like(currentpetId, petDataList[0].petId)
                    if (status == 200) {
                        isLike = true
                        flingContainer.topCardListener.selectRight()
                        Toast.makeText(activity, "Liked!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(activity, "There are no other pets.", Toast.LENGTH_LONG).show()
        }
    }
}