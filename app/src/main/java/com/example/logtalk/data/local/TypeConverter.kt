package com.example.logtalk.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): String? {
        return byteArray?.joinToString(",") { it.toString() }
    }

    @TypeConverter
    fun toByteArray(string: String?): ByteArray? {
        return string?.split(",")?.map { it.toByte() }?.toByteArray()
    }
}