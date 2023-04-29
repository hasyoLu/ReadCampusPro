package com.hasyolu.readcampus.persistence

import androidx.room.*
import com.hasyolu.readcampus.model.ChapterBean

@Dao
interface ChapterDao:
    BaseBeanDao<ChapterBean> {
    @Query("SELECT * FROM cached_chapters WHERE url=:url")
    suspend fun get(url: String): ChapterBean?

    @Query("SELECT content FROM cached_chapters WHERE url=:url")
    suspend fun getContentByUrl(url: String): String?

    @Query("SELECT * FROM cached_chapters WHERE bookUrl=:bookUrl AND `index`>=:start ORDER  BY `index` ASC LIMIT :limit")
    suspend fun getListByBookUrl(bookUrl: String, start: Int = 0, limit: Int = 100): MutableList<ChapterBean>

    @Query("SELECT count(*) FROM cached_chapters WHERE bookUrl=:bookUrl")
    suspend fun getListCountByBookUrl(bookUrl: String): Int

    @Query("DELETE FROM cached_chapters WHERE bookUrl=:bookUrl")
    suspend fun deleteByBookUrl(bookUrl: String)
}
