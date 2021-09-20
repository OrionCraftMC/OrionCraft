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

package io.github.orioncraftmc.orion.api.mods.settings

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.orioncraftmc.orion.api.mods.settings.storage.GlobalSettings
import java.io.File

class SettingsProvider(runDirectory: File) {

	val mapper = jacksonObjectMapper()

	private var globalSettings: GlobalSettings = GlobalSettings()
	lateinit var currentProfile: RawSettingsProfile

	private val settingsProfilesFolder =
		File(runDirectory, ".orioncraft${File.separatorChar}profiles").also { it.mkdirs() }
	private val globalSettingsFile = File(runDirectory, ".orioncraft${File.separatorChar}settings.json")

	private fun computeDefaultProfile() = mutableMapOf<String, RawSettingsProfileData>()
	private fun getProfileFile(name: String): File = File(settingsProfilesFolder, "$name.json")

	private fun createEmptyProfileIfNeeded() {
		val name = globalSettings.currentProfile
		val profileFile = getProfileFile(name)

		if (!profileFile.exists()) {
			mapper.writeValue(profileFile, computeDefaultProfile())
		}
	}

	fun load() {
		loadGlobalSettings()
		createEmptyProfileIfNeeded()
		loadCurrentProfile()
	}

	private fun loadCurrentProfile() {
		currentProfile = mapper.readValue(getProfileFile(globalSettings.currentProfile))
	}

	fun save() {
		writeGlobalSettings()
		writeCurrentProfile()
	}

	private fun writeCurrentProfile() {
		mapper.writeValue(getProfileFile(globalSettings.currentProfile), currentProfile)
	}

	private fun loadGlobalSettings() {
		if (globalSettingsFile.exists()) {
			globalSettings = mapper.readValue(globalSettingsFile)
		} else {
			// Save default global settings
			writeGlobalSettings()
		}
	}

	private fun writeGlobalSettings() {
		mapper.writeValue(globalSettingsFile, globalSettings)
	}

}
