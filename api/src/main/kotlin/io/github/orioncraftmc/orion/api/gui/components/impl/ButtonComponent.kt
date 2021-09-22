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

package io.github.orioncraftmc.orion.api.gui.components.impl

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.RGBInt
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.utils.gui.ComponentUtils
import io.github.orioncraftmc.orion.api.utils.rendering.RectRenderingUtils

class ButtonComponent(var text: String, var color: Color = RGBInt(255, 255, 255), onClick: () -> Unit = {}) : ComponentContainer() {

	init {
		addComponent(LabelComponent(text, color).apply {
			anchor = Anchor.MIDDLE
		})
	}

	var unpressedBackground = RGBInt(0, 0, 0, 90)
	var pressedBackground = RGBInt(255, 255, 255, 90)
	var borderColor = RGBInt(255, 255, 255, 127)

	override fun renderComponent(mouseX: Int, mouseY: Int) {
		ComponentUtils.renderBackgroundColor(this, getBackgroundColor(mouseX, mouseY))
		renderButtonBorder()
		super.renderComponent(mouseX, mouseY)
	}

	private fun renderButtonBorder() {
		val ourPadding = padding
		val ourSize = size
		OpenGlBridge.setLineWidth(2.0f)
		RectRenderingUtils.drawRectangle(
			-ourPadding.left,
			-ourPadding.top,
			ourSize.width + ourPadding.right,
			ourSize.height + ourPadding.bottom,
			borderColor,
			true
		)

	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
		super.handleMouseClick(mouseX, mouseY)
	}

	private fun getBackgroundColor(mouseX: Int, mouseY: Int) =
		if (ComponentUtils.isMouseWithinComponent(mouseX, mouseY, this)) {
			pressedBackground
		} else {
			unpressedBackground
		}
}
