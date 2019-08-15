package com.example.mymovies.data

import android.arch.persistence.room.Entity

@Entity(tableName = "videos")
class Video{
    var key:String = ""
    var name:String = ""
    constructor(key:String, name:String){
        this.key = key
        this.name = name
    }
}