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

import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.OrionCraftConstants
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.components.containers.flow.FlowLayoutContainer
import io.github.orioncraftmc.components.containers.flow.FlowLayoutDirection
import io.github.orioncraftmc.orion.api.gui.components.impl.LabelComponent
import io.github.orioncraftmc.components.model.Anchor

object BrandingUtils {
	fun getBrandingComponent(mainTextScale: Double, isGuiBackgroundTarget: Boolean = false): Component {
		return FlowLayoutContainer(FlowLayoutDirection.VERTICAL).apply {
			snapToDevicePixels = true
			val mainLabelComponent = LabelComponent(OrionCraftConstants.mainClientName).apply {
				scale = mainTextScale
				if (isGuiBackgroundTarget) anchor = Anchor.TOP_RIGHT
			}
			addComponent(mainLabelComponent)

			var secondaryLabelText: String? = null
			if (OrionCraft.clientVersion.isNostalgiaVersion) {
				secondaryLabelText = "Nostalgia"
			} else if (isGuiBackgroundTarget && OrionCraftConstants.isCustomBranded) {
				secondaryLabelText = "(powered by OrionCraftMc)" //TODO: Translate to client language
			}

			if (secondaryLabelText != null) {
				addComponent(LabelComponent(secondaryLabelText).apply {
					anchor = Anchor.TOP_RIGHT
					snapToDevicePixels = true
				})
			}
		}
	}
}
