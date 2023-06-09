// Generated by Dagger (https://dagger.dev).
package com.hasyolu.readcampus.di;

import com.hasyolu.ktReader.persistence.AppDataBase;
import com.hasyolu.ktReader.persistence.ChapterDao;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class PersistenceModule_ProvideChapterDaoFactory implements Factory<ChapterDao> {
  private final Provider<AppDataBase> appDataBaseProvider;

  public PersistenceModule_ProvideChapterDaoFactory(Provider<AppDataBase> appDataBaseProvider) {
    this.appDataBaseProvider = appDataBaseProvider;
  }

  @Override
  public ChapterDao get() {
    return provideChapterDao(appDataBaseProvider.get());
  }

  public static PersistenceModule_ProvideChapterDaoFactory create(
      Provider<AppDataBase> appDataBaseProvider) {
    return new PersistenceModule_ProvideChapterDaoFactory(appDataBaseProvider);
  }

  public static ChapterDao provideChapterDao(AppDataBase appDataBase) {
    return Preconditions.checkNotNullFromProvides(PersistenceModule.INSTANCE.provideChapterDao(appDataBase));
  }
}
