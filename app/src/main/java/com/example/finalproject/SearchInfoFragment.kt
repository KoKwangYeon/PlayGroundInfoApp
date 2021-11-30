package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_search_info.*

/**
 * A simple [Fragment] subclass.
 */
class SearchInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        cancelbtn.setOnClickListener {
            getActivity()?.finish()
        }

        searchbtn.setOnClickListener {
            val region = region.text.toString()

            if(region == ""){
                Toast.makeText(getActivity(),"지역을 입력해주세요!",Toast.LENGTH_SHORT).show()
            }else {
                val maininfofragment = MainInfoFragment()
                var args = Bundle()
                args.putString("regionName", region)
                maininfofragment.arguments = args
                fragmentManager?.beginTransaction()?.replace(R.id.frame, maininfofragment)
                    ?.commit()
            }
        }
    }

}
