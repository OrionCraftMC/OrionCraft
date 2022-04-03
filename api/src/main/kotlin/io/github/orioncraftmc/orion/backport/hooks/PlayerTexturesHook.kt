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

package io.github.orioncraftmc.orion.backport.hooks

import io.github.orioncraftmc.orion.api.bridge.ResourceLocationUtils
import io.github.orioncraftmc.orion.api.bridge.minecraft.resources.ResourceLocationBridge
import io.github.orioncraftmc.orion.api.logger
import io.github.orioncraftmc.orion.backport.skins.CapeImageHelper
import io.github.orioncraftmc.orion.io.capes.CapesApi
import io.github.orioncraftmc.orion.io.profile.ProfileApi
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.function.Consumer
import javax.imageio.ImageIO

object PlayerTexturesHook {

	fun getPlayerDefaultSkinResourceLocation(): ResourceLocationBridge {
		return ResourceLocationUtils.createNewOrionResourceLocation("textures/entity/steve.png")
	}

	fun fetchPlayerTexture(location: String, cancel: Runnable, imageHandler: Consumer<BufferedImage>, slimSkinHandler: Consumer<Boolean>?, oldModelHandler: Consumer<Boolean>?) {
		if (!location.startsWith("orion_")) {
			return
		}
		val downloadData: List<String>
		try {
 			downloadData = location.split('_', limit = 4)
			if (downloadData.size != 4) {
				return
			}
		} catch (e: Exception) {
			e.printStackTrace()
			return
		}
		cancel.run()

		val part = downloadData[1]
		val name = downloadData[3]
		var result: ByteArray? = null

		logger.debug("Downloading part $part for name $name")
		val isCloak = part == "cloak"
		when (part) {
			"skin" -> {
				result = getPlayerSkin(name)
				slimSkinHandler?.accept(isPlayerUsingSlimSkin(name))
			}
			"cloak" -> result = getPlayerCloak(name)
		}

		if (result != null) {
			try {
				ByteArrayInputStream(result).use { byteStream ->
					var image = ImageIO.read(byteStream)
					if (isCloak) image = CapeImageHelper.parseCapeImage(image)
					if (!isCloak) {
						val ratio = image.width / image.height.toDouble()
						oldModelHandler?.accept(ratio != 1.0)
					}
					imageHandler.accept(image)
				}
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}

	}

	private fun getPlayerSkin(name: String): ByteArray? {
		return ProfileApi.getProfileByName(name)?.textures?.skin?.data?.let {
			Base64.getDecoder().decode(it)
		}
	}

	private fun isPlayerUsingSlimSkin(name: String): Boolean {
		return ProfileApi.getProfileByName(name)?.textures?.slim == true
	}

	private fun getPlayerCloak(name: String): ByteArray? {
		return CapesApi.getCapeForPlayer(name)?.imageUrl?.let { URL(it).readBytes() }
	}

}
