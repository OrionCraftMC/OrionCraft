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

package io.github.orioncraftmc.orion.ui.ultralight

import com.labymedia.ultralight.UltralightPlatform
import com.labymedia.ultralight.UltralightRenderer
import com.labymedia.ultralight.UltralightView
import com.labymedia.ultralight.config.FontHinting
import com.labymedia.ultralight.config.UltralightConfig
import com.labymedia.ultralight.config.UltralightViewConfig
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.TextureParameter
import io.github.orioncraftmc.orion.api.bridge.rendering.TextureParameterValue


class OrionUltralightManager {

	private val platform: UltralightPlatform = UltralightPlatform.instance()
	private val renderer: UltralightRenderer
	private val view: UltralightView

	private var glTexture = -1
	private val lastJavascriptGarbageCollections: Long = 0

	init {

		platform.setConfig(
			UltralightConfig().forceRepaint(false).fontHinting(FontHinting.SMOOTH)
		)

		platform.usePlatformFontLoader()
		platform.setFileSystem(OrionUltralightFileSystem)
		platform.setLogger(OrionUltralightLogger)
		platform.setClipboard(OrionUltralightClipboard)

		renderer = UltralightRenderer.create()
		this.renderer.logMemoryUsage()

		view = renderer.createView(
			300, 200, UltralightViewConfig().isAccelerated(false).initialDeviceScale(1.0).isTransparent(true)
		)
	}

	/**
	 * Resizes the web view.
	 *
	 * @param width  The new view width
	 * @param height The new view height
	 */
	fun resize(width: Int, height: Int) {
		view.resize(width.toLong(), height.toLong())
	}

	fun render() {
		if (glTexture == -1) {
			createGLTexture()
		}
	}

	private fun createGLTexture() {
		OpenGlBridge.enableTexture2D()

		glTexture = OpenGlBridge.generateNewTextureId()
		OpenGlBridge.bind2dTextureWithId(glTexture)

		OpenGlBridge.setTexture2dParameter(TextureParameter.GL_TEXTURE_MIN_FILTER, TextureParameterValue.GL_NEAREST)
		OpenGlBridge.setTexture2dParameter(TextureParameter.GL_TEXTURE_MAG_FILTER, TextureParameterValue.GL_NEAREST)
		OpenGlBridge.setTexture2dParameter(TextureParameter.GL_TEXTURE_WRAP_T, TextureParameterValue.GL_CLAMP_TO_EDGE)
		OpenGlBridge.setTexture2dParameter(TextureParameter.GL_TEXTURE_WRAP_S, TextureParameterValue.GL_CLAMP_TO_EDGE)

		OpenGlBridge.bind2dTextureWithId(0)
		OpenGlBridge.disableTexture2D()
	}
}
