package com.hasyolu.readcampus.persistence

import androidx.room.ProvidedTypeConverter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ProvidedTypeConverter
class TypeResponseConverter @Inject constructor(
  private val moshi: Moshi
) {

//  @TypeConverter
//  fun fromString(value: String): List<PokemonInfo.TypeResponse>? {
//    val listType = Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
//    val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
//    return adapter.fromJson(value)
//  }
//
//  @TypeConverter
//  fun fromInfoType(type: List<PokemonInfo.TypeResponse>?): String {
//    val listType = Types.newParameterizedType(List::class.java, PokemonInfo.TypeResponse::class.java)
//    val adapter: JsonAdapter<List<PokemonInfo.TypeResponse>> = moshi.adapter(listType)
//    return adapter.toJson(type)
//  }
}
