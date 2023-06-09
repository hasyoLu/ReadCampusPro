// Generated by Dagger (https://dagger.dev).
package com.hasyolu.readcampus.di;

import com.hasyolu.ktReader.network.HtmlClient;
import com.hasyolu.ktReader.persistence.BookDao;
import com.hasyolu.ktReader.repository.SquareRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class RepositoryModule_ProvideSquareRepositoryFactory implements Factory<SquareRepository> {
  private final Provider<HtmlClient> htmlClientProvider;

  private final Provider<BookDao> bookDaoProvider;

  public RepositoryModule_ProvideSquareRepositoryFactory(Provider<HtmlClient> htmlClientProvider,
      Provider<BookDao> bookDaoProvider) {
    this.htmlClientProvider = htmlClientProvider;
    this.bookDaoProvider = bookDaoProvider;
  }

  @Override
  public SquareRepository get() {
    return provideSquareRepository(htmlClientProvider.get(), bookDaoProvider.get());
  }

  public static RepositoryModule_ProvideSquareRepositoryFactory create(
      Provider<HtmlClient> htmlClientProvider, Provider<BookDao> bookDaoProvider) {
    return new RepositoryModule_ProvideSquareRepositoryFactory(htmlClientProvider, bookDaoProvider);
  }

  public static SquareRepository provideSquareRepository(HtmlClient htmlClient, BookDao bookDao) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideSquareRepository(htmlClient, bookDao));
  }
}
