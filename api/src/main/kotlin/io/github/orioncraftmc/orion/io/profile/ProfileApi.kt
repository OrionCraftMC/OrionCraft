/*
 * MIT License
 *
 * Copyright (c) 2021 OrionCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.orioncraftmc.orion.io.profile

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import io.github.orioncraftmc.orion.io.profile.models.Profile
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("UnstableApiUsage")
object ProfileApi {
	private val objectMapper by lazy { configureMapper(jacksonObjectMapper()) }

	private fun configureMapper(mapper: ObjectMapper): ObjectMapper {
		return mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
			.enable(MapperFeature.PROPAGATE_TRANSIENT_MARKER)
	}

	private val profileCache: LoadingCache<UUID, Profile?> =
		CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(CacheLoader.from { _ -> null })
	private val profileNameCache: LoadingCache<String, Profile?> =
		CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(CacheLoader.from { _ -> null })

	fun getProfileById(id: UUID): Profile? {
		return profileCache.getIfPresent(id) ?: getProfileService().findById(id)?.execute()?.body()
			?.takeIf { !it.isError }?.also {
				profileCache.put(id, it)
				profileNameCache.put(it.name!!, it)
			}
	}

	fun getProfileByName(name: String): Profile? {
		return profileNameCache.getIfPresent(name) ?: getProfileService().findByName(name)?.execute()?.body()
			?.takeIf { !it.isError }?.also {
				profileNameCache.put(it.name!!, it)
				profileCache.put(it.uuid!!, it)
			}
	}

	private fun getProfileService(): ProfileService {
		return Retrofit.Builder().baseUrl("https://api.ashcon.app/mojang/v2/").addConverterFactory(
			JacksonConverterFactory.create(
				objectMapper
			)
		).build().create(ProfileService::class.java)

	}
}
