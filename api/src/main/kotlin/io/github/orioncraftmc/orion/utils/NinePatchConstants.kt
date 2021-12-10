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

import io.github.orioncraftmc.orion.api.bridge.ResourceLocationUtils
import io.github.orioncraftmc.orion.api.bridge.minecraft.resources.ResourceLocationBridge
import io.github.orioncraftmc.orion.api.meta.ninepatch.NinePatchElement
import java.text.DecimalFormat

object NinePatchConstants {
	val ninePatchScales = arrayOf(1.0, 2.0, 2.5)

	val button = NinePatchElement("textures/gui/button/button_%scale%x.png", 15, 15, 54, 54)

	val buttonHover = NinePatchElement("textures/gui/button/button_hover_%scale%x.png", 15, 15, 54, 54)

	private val decimalFormat = DecimalFormat("0.#")
	fun getResourceLocationForElementWithScale(element: NinePatchElement, scale: Double = 1.0): ResourceLocationBridge {
		return ResourceLocationUtils.createNewOrionResourceLocation(
			element.resourceName.replace(
				"%scale%",
				decimalFormat.format(scale)
			)
		)
	}
}
