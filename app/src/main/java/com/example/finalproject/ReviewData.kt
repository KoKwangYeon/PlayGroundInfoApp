package com.example.finalproject

data class ReviewData(var nickname:String, var review:String,var starPoint:String,var password:String) {
    constructor():this("nonN","nonReview","nonStar","nonP")
}