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

package io.github.orioncraftmc.orion.mods.staffhack.gui

import com.github.ajalt.colormath.model.RGBInt
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.components.impl.LabelComponent
import io.github.orioncraftmc.orion.api.gui.components.impl.RectangleComponent
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.flow.FlowLayoutContainer
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.utils.ColorConstants

class HackNameComponent(name: String, textAnchor: Anchor) : FlowLayoutContainer() {
	init {
		snapToDevicePixels = true
		anchor = textAnchor
		backgroundColor = ColorConstants.modLabelBackgroundColor

		val rectangle = RectangleComponent().apply {
			size = Size(2.0, MinecraftBridge.fontRenderer.fontHeight.toDouble())
			backgroundColor = RGBInt(0, 0x90, 0xff)
			anchor = Anchor.MIDDLE_LEFT
			padding = Padding(5.0, 0.0)
			snapToDevicePixels = true
		}
		val name = LabelComponent(name).apply {
			padding = Padding(5.0)
			anchor = Anchor.MIDDLE
			snapToDevicePixels = true
		}
		var first: Component = rectangle
		var second: Component = name

		if (anchor.getSideAtTop() == Anchor.TOP_LEFT) {
			//swap first with second
			first = name
			second = rectangle
		}

		addComponent(first)
		addComponent(second)

	}
}
