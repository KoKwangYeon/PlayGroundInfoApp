package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ReviewAdapter2(options: FirebaseRecyclerOptions<ReviewData>) :
    FirebaseRecyclerAdapter<ReviewData, ReviewAdapter2.ViewHolder>(options) {

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nickname: TextView
        var review: TextView
        var starpoint: TextView
        var password:TextView
        init{
            nickname  = itemView.findViewById(R.id.nickname)
            review = itemView.findViewById(R.id.review)
            starpoint= itemView.findViewById(R.id.starpoint)
            password=itemView.findViewById(R.id.password)

            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,adapterPosition)
            }
        }
    }
    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder,view:View,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter2.ViewHolder {
        var v = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_row, parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ReviewData) {
        holder.nickname.text =model.nickname
        holder.review.text = model.review
        holder.starpoint.text = model.starPoint
        holder.password.text = model.password
    }
}