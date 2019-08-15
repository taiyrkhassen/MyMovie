package com.example.mymovies.data

import android.arch.persistence.room.Entity


class Review{
    var author:String = ""
    var content:String = ""
    constructor(author:String, content:String){
        this.author = author
        this.content = content
    }
}