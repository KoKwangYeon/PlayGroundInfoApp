package com.example.finalproject

import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.popup_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : Fragment() {
    lateinit var layoutManager:LinearLayoutManager
    lateinit var adapter: ReviewAdapter2
    lateinit var googleMap: GoogleMap
    lateinit var rdb: DatabaseReference
    lateinit var ciname2 :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initmap()
    }

    private fun initmap() {
        var ciname:String =""
        lateinit var checknickname:String
        lateinit var checkpassword:String

        val mapFragment = childFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
        showInputbtn.setOnClickListener {
            if(inputReview.visibility == GONE){
                inputReview.visibility = VISIBLE
                showInputbtn.setTextColor(WHITE)
                showInputbtn.setBackgroundColor(BLACK)
            }else{
                inputReview.visibility = GONE
                showInputbtn.setTextColor(BLACK)
                showInputbtn.setBackgroundColor(Color.parseColor("#86CAC4"))
            }
        }
        mapFragment.getMapAsync{
            googleMap =  it
            if(arguments!=null){
                info.visibility = VISIBLE
                ciname = arguments!!.getString("ciname").toString()
                val latitude= arguments!!.getDouble("latitude")
                val longitude= arguments!!.getDouble("longitude")
                val name1 = arguments!!.getString("name1")
                val name21 = arguments!!.getString("name21")
                val ciaddr = arguments!!.getString("ciaddr")
                var name2 = arguments!!.getString("name2")
                var name4 = arguments!!.getString("name4")
                val loc =LatLng(latitude,longitude)
                val myLoc = LatLng(37.452815,126.718538)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))

                val options = MarkerOptions()
                options.position(loc)
                options.title(ciname)
                googleMap.addMarker(options)
                options.position(myLoc)
                options.title("내 위치")
                googleMap.addMarker(options)

                ciName.text = ciname
                ciname2 = ciname
                ciNaddr.text = ciaddr
                Name1.text = name1
                Name21.text = name21
                Name2.text=name2
                Name4.text=name4

                recyclerView2.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                recyclerView2.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                rdb = FirebaseDatabase.getInstance().getReference("review/"+ciname)
                val query = FirebaseDatabase.getInstance().reference.child("review/"+ciname).limitToLast(50)
                val option = FirebaseRecyclerOptions.Builder<ReviewData>()
                    .setQuery(query,ReviewData::class.java)
                    .build()
                adapter = ReviewAdapter2(option)
                recyclerView2.adapter = adapter

                var mDatabase = FirebaseDatabase.getInstance().getReference("review")
                var rDatabase = mDatabase.child(ciname)

                reviewbtn.setOnClickListener{

                    if(!in_nickname.text.toString().equals("") && !in_review.text.toString().equals("")
                        && !in_starpoint.text.toString().equals("")  && !in_password.text.toString().equals(""))  {
                        val review=ReviewData(in_nickname.text.toString(),
                            in_review.text.toString(),
                            in_starpoint.text.toString(), in_password.text.toString())


                        if (adapter != null)
                            adapter.stopListening()
                        val query =
                            FirebaseDatabase.getInstance().reference.child("review/" + ciname)
                                .limitToLast(50)
                        val option = FirebaseRecyclerOptions.Builder<ReviewData>()
                            .setQuery(query, ReviewData::class.java)
                            .build()
                        adapter = ReviewAdapter2(option)
                        recyclerView2.adapter = adapter
                        adapter.startListening()
                        rdb.child(in_nickname.text.toString()).setValue(review)
                        Toast.makeText(getActivity(),"입력 완료!",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(getActivity(),"빈칸이 존재합니다!",Toast.LENGTH_SHORT).show()

                    }
                    in_nickname.setText("")
                    in_review.setText("")
                    in_starpoint.setText("")
                    in_password.setText("")

                }

                adapter.itemClickListener = object : ReviewAdapter2.OnItemClickListener {
                    override fun OnItemClick(holder : ReviewAdapter2.ViewHolder,view: View, position: Int) {
                        val dlg = PopupDialog(context)
                        dlg.start(holder.nickname.text.toString(),holder.password.text.toString())
                        dlg.setOnOKClickedListener{ nick,pass,checkNum ->
                            checkNick.text = nick
                            checkPass.text=pass
                            checkcheck.text = checkNum.toString()

                            if(checkNum ==1){
                                rdb.child(checkNick.text.toString()).removeValue()
                            }else{
                                Toast.makeText(getActivity(),"옳지않은 정보입니다!",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                adapter.startListening()
            }else{
                info.visibility = INVISIBLE

            }

        }
    }
}
