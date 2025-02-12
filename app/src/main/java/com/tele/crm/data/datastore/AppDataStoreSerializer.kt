package com.tele.crm.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object AppDataStoreSerializer : Serializer<AppDataStoreDto> {

    override val defaultValue: AppDataStoreDto
        get() = AppDataStoreDto()

    override suspend fun readFrom(input: InputStream): AppDataStoreDto {
        return try {
            Json.decodeFromString(deserializer = AppDataStoreDto.serializer(),
                string = input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppDataStoreDto, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(serializer = AppDataStoreDto.serializer(), value = t).encodeToByteArray())
        }
    }
}
