package com.hasyolu.readcampus.persistence

import androidx.room.*
import com.hasyolu.readcampus.model.ReadRecordBean

@Dao
interface ReadRecordDao:
    BaseBeanDao<ReadRecordBean> {
    @Query("SELECT * FROM my_read_records WHERE `bookMd5`== :bookMd5")
    suspend fun getByMd5(bookMd5: String): ReadRecordBean?

    @Query("SELECT * FROM my_read_records")
    suspend fun getList(): List<ReadRecordBean>
}
