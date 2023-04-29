// Generated by Dagger (https://dagger.dev).
package com.hasyolu.readcampus.di;

import com.hasyolu.ktReader.persistence.BookDao;
import com.hasyolu.ktReader.persistence.BookSignDao;
import com.hasyolu.ktReader.persistence.ChapterDao;
import com.hasyolu.ktReader.persistence.ReadRecordDao;
import com.hasyolu.ktReader.repository.ShelfRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RepositoryModule_ProvideShelfRepositoryFactory implements Factory<ShelfRepository> {
  private final Provider<BookDao> bookDaoProvider;

  private final Provider<BookSignDao> bookSignDaoProvider;

  private final Provider<ChapterDao> chapterDaoProvider;

  private final Provider<ReadRecordDao> readRecordDaoProvider;

  public RepositoryModule_ProvideShelfRepositoryFactory(Provider<BookDao> bookDaoProvider,
      Provider<BookSignDao> bookSignDaoProvider, Provider<ChapterDao> chapterDaoProvider,
      Provider<ReadRecordDao> readRecordDaoProvider) {
    this.bookDaoProvider = bookDaoProvider;
    this.bookSignDaoProvider = bookSignDaoProvider;
    this.chapterDaoProvider = chapterDaoProvider;
    this.readRecordDaoProvider = readRecordDaoProvider;
  }

  @Override
  public ShelfRepository get() {
    return provideShelfRepository(bookDaoProvider.get(), bookSignDaoProvider.get(), chapterDaoProvider.get(), readRecordDaoProvider.get());
  }

  public static RepositoryModule_ProvideShelfRepositoryFactory create(
      Provider<BookDao> bookDaoProvider, Provider<BookSignDao> bookSignDaoProvider,
      Provider<ChapterDao> chapterDaoProvider, Provider<ReadRecordDao> readRecordDaoProvider) {
    return new RepositoryModule_ProvideShelfRepositoryFactory(bookDaoProvider, bookSignDaoProvider, chapterDaoProvider, readRecordDaoProvider);
  }

  public static ShelfRepository provideShelfRepository(BookDao bookDao, BookSignDao bookSignDao,
      ChapterDao chapterDao, ReadRecordDao readRecordDao) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideShelfRepository(bookDao, bookSignDao, chapterDao, readRecordDao));
  }
}