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

package io.github.orioncraftmc.orion.api.settings

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.orioncraftmc.orion.api.OrionCraftConstants
import io.github.orioncraftmc.orion.api.settings.storage.SettingsFile
import io.github.orioncraftmc.orion.api.settings.storage.SettingsProfile
import java.io.File

class SettingsProvider(runDirectory: File) {

	val mapper = jacksonObjectMapper()
	private val settingsFile = File(runDirectory, ".orioncraft${File.separatorChar}settings.json")

	var currentSettingsFile: SettingsFile =
		SettingsFile(
			OrionCraftConstants.DEFAULT_PROFILE_NAME,
			mutableMapOf(OrionCraftConstants.DEFAULT_PROFILE_NAME to computeDefaultProfile())
		)
		set(value) {
			field = value
			saveSettings()
		}

	private var currentSettingsProfileName: String
		get() = currentSettingsFile.currentProfileName
		set(value) {
			currentSettingsFile.currentProfileName = value
			saveSettings()
		}

	var currentSettingsProfile: SettingsProfile
		get() = currentSettingsFile.profiles[currentSettingsProfileName] ?: computeDefaultProfile()
		set(value) {
			currentSettingsFile.currentProfileName = value.name
		}


	private fun computeDefaultProfile() = SettingsProfile(OrionCraftConstants.DEFAULT_PROFILE_NAME, mutableMapOf())

	fun load() {
		if (!settingsFile.exists()) {
			saveSettings()
		} else {
			currentSettingsFile = mapper.readValue(settingsFile)
		}
	}

	fun saveSettings() {
		settingsFile.parentFile.mkdirs()
		mapper.writeValue(settingsFile, currentSettingsFile)
	}

}
