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

package io.github.orioncraftmc.orion.utils

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.activity.Activity
import de.jcm.discordgamesdk.activity.ActivityType
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.OrionCraftConstants
import io.github.orioncraftmc.orion.api.OrionCraftConstants.orionCraftDiscordClientId
import io.github.orioncraftmc.orion.api.OrionCraftConstants.orionCraftNostalgiaDiscordClientId
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.logger
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.time.Instant
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.concurrent.thread

object OrionDiscordIntegration {
	var discordSdkCore: Core? = null

	fun updateStateActivity(state: String) {
		if (discordSdkCore == null) return
		updateActivity(Activity().apply {
			this.state = state
			this.type = ActivityType.PLAYING
			this.details = "Playing ${OrionCraftConstants.clientRpcVersionString}"
			this.timestamps().start = Instant.now()
			this.assets().largeImage = "rpc_logo"
		})
	}

	fun updateActivity(activity: Activity) {
		activity.use {
			val manager = discordSdkCore?.activityManager() ?: return
			manager.updateActivity(activity)
		}
	}

	fun initIntegration(): Boolean {
		val discordLibrary = runCatching { downloadDiscordLibrary() }.getOrNull()
		if (discordLibrary == null) {
			logger.error("Unable to download Discord SDK's natives. No Discord integration will be done.")
			return false
		}

		// Initialize the Core
		Core.init(discordLibrary)

		CreateParams().use { params ->
			params.clientID =
				if (!OrionCraft.clientVersion.isNostalgiaVersion) orionCraftDiscordClientId else orionCraftNostalgiaDiscordClientId // Orion Client Id
			params.flags = CreateParams.getDefaultFlags()
			discordSdkCore = Core(params)
		}

		thread {
			while (true) {
				discordSdkCore?.runCallbacks()
				Thread.sleep(100)
			}
		}

		return true
	}

	private fun downloadDiscordLibrary(): File? {
		val cachedLibraryFile = File(MinecraftBridge.gameAppDirectory, "discord_game_sdk-natives.zip")

		// Find out which name Discord's library has (.dll for Windows, .so for Linux)
		val name = "discord_game_sdk"
		val suffix: String
		val osName = System.getProperty("os.name"). lowercase()
		var arch = System.getProperty("os.arch").lowercase()
		suffix = if (osName.contains("windows")) {
			".dll"
		} else if (osName.contains("linux")) {
			".so"
		} else if (osName.contains("mac os")) {
			".dylib"
		} else {
			throw RuntimeException("cannot determine OS type: $osName")
		}

		// Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		// At this point we need the "x86_64" version, as this one is used in the ZIP.
		if (arch == "amd64") arch = "x86_64"

		// Path of Discord's library inside the ZIP
		val zipPath = "lib/$arch/$name$suffix"

		// Open the URL as a ZipInputStream
		val downloadUrl = URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip")
		if (!cachedLibraryFile.exists()) {
			cachedLibraryFile.writeBytes(downloadUrl.readBytes())
		}
		val zin = ZipInputStream(cachedLibraryFile.inputStream())

		// Search for the right file inside the ZIP
		var entry: ZipEntry?
		while (zin.nextEntry.also { entry = it } != null) {
			if (entry?.name?.equals(zipPath) == true) {
				// Create a new temporary directory
				// We need to do this, because we may not change the filename on Windows
				val tempDir = File(System.getProperty("java.io.tmpdir"), "java-" + name + System.nanoTime())
				if (!tempDir.mkdir()) throw IOException("Cannot create temporary directory")
				tempDir.deleteOnExit()

				// Create a temporary file inside our directory (with a "normal" name)
				val temp = File(tempDir, name + suffix)
				temp.deleteOnExit()

				// Copy the file in the ZIP to our temporary file
				Files.copy(zin, temp.toPath())

				// We are done, so close the input stream
				zin.close()

				// Return our temporary file
				return temp
			}
			// next entry
			zin.closeEntry()
		}
		zin.close()
		// We couldn't find the library inside the ZIP
		return null
	}

}
