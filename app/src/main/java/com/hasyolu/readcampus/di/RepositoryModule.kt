package com.hasyolu.readcampus.di

import com.hasyolu.readcampus.network.HtmlClient
import com.hasyolu.readcampus.persistence.BookDao
import com.hasyolu.readcampus.persistence.BookSignDao
import com.hasyolu.readcampus.persistence.ChapterDao
import com.hasyolu.readcampus.persistence.ReadRecordDao
import com.hasyolu.readcampus.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    @ActivityRetainedScoped
    fun provideNovelReadRepository(
        htmlClient: HtmlClient,
        bookDao: BookDao,
        bookSignDao: BookSignDao,
        chapterDao: ChapterDao,
        readRecordDao: ReadRecordDao
    ): NovelReadRepository {
        return NovelReadRepository(htmlClient, bookDao, bookSignDao, chapterDao, readRecordDao)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideShelfRepository(
        bookDao: BookDao,
        bookSignDao: BookSignDao,
        chapterDao: ChapterDao,
        readRecordDao: ReadRecordDao
    ): ShelfRepository {
        return ShelfRepository(bookDao, bookSignDao, chapterDao, readRecordDao)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideSquareRepository(
        htmlClient: HtmlClient,
        bookDao: BookDao,
    ): SquareRepository {
        return SquareRepository(htmlClient,bookDao)
    }
}
