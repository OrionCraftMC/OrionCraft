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

package io.github.orioncraftmc.orion.utils.rendering

import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.matrix
import io.github.orioncraftmc.orion.api.gui.components.impl.ninepatch.NinePatchTextureRenderer
import io.github.orioncraftmc.orion.api.meta.ninepatch.NinePatchElement
import io.github.orioncraftmc.orion.utils.NinePatchConstants
import kotlin.math.roundToInt

object NinePatchRendererUtils {
	fun renderNinePatch(element: NinePatchElement, x: Double, y: Double, width: Double, height: Double, scale: Double) {
		val patchScale = scale
		val finalScale = scale * 2
		val resource = NinePatchConstants.getResourceLocationForElementWithScale(element)
		MinecraftBridge.renderEngine.bindTexture(resource)
		OpenGlBridge.translate(x, y, 0.0)
		matrix {
			OpenGlBridge.translate(-element.paddingSize.toDouble(), -element.paddingSize.toDouble(), 0.0)
			OpenGlBridge.scale(1 / finalScale, 1 / finalScale, 1.0)
			element.getNinePatch(patchScale).draw(
				NinePatchTextureRenderer,
				((width + (element.paddingSize * 2)) * finalScale).roundToInt(),
				((height + (element.paddingSize * 2)) * finalScale).roundToInt()
			)
		}
		OpenGlBridge.translate(-x, -y, 0.0)
	}
}
