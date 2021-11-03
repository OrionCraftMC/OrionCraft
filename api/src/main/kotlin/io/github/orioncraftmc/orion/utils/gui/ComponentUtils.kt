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

package io.github.orioncraftmc.orion.utils.gui

import com.github.ajalt.colormath.Color
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.utils.rendering.RectRenderingUtils
import kotlin.math.roundToInt

object ComponentUtils {

	fun getComponentOriginPosition(component: Component) =
		Point(-component.padding.left, -component.padding.top)

	fun renderComponent(
		component: Component,
		mouseX: Int,
		mouseY: Int,
		doLayout: Boolean = true
	) {
		if (doLayout) performComponentLayout(component)
		renderBackgroundColor(component, component.backgroundColor)
		val (finalMouseX, finalMouseY) = computeMousePosition(component, mouseX, mouseY)
		OpenGlBridge.enableBlend()
		component.renderComponent(finalMouseX, finalMouseY)
	}

	fun translateComponent(component: Component) {
		val newPos = component.effectivePosition
		OpenGlBridge.translate(
			valueToDevicePixelsIfNeeded(component, newPos.x),
			valueToDevicePixelsIfNeeded(component, newPos.y),
			0.0
		)
	}

	private fun valueToDevicePixelsIfNeeded(
		component: Component,
		value: Double
	): Double {
		if (component.snapToDevicePixels) {
			return value.roundToInt().toDouble()
		}
		return value
	}

	fun performComponentLayout(component: Component) {
		translateComponent(component)
		scaleComponent(component)
	}

	fun renderBackgroundColor(component: Component, color: Color?) {
		val backgroundColor = color ?: return
		val padding = component.padding

		val size = component.size
		RectRenderingUtils.drawRectangle(
			-padding.left,
			-padding.top,
			size.width + padding.right,
			size.height + padding.bottom,
			backgroundColor,
			false
		)
	}

	fun scaleComponent(component: Component) {
		OpenGlBridge.scale(component.scale, component.scale, 1.0)
	}


	fun isMouseWithinComponent(
		mouseX: Int,
		mouseY: Int,
		component: Component,
		isAbsoluteCoordinates: Boolean = false,
		bufferSize: Int = 0
	): Boolean {
		val componentSize = component.effectiveSize
		var startingPoint = getComponentOriginPosition(component)
		if (isAbsoluteCoordinates) startingPoint = component.effectivePosition + getComponentOriginPosition(component)

		return (mouseX >= (startingPoint.x - bufferSize) && mouseX <= (startingPoint.x + (componentSize.width * component.scale) + bufferSize)
				&& mouseY >= (startingPoint.y - bufferSize) && mouseY <= (startingPoint.y + (componentSize.height * component.scale) + bufferSize))
	}

	fun computeMousePosition(
		component: Component,
		mouseX: Int,
		mouseY: Int
	): Pair<Int, Int> {
		val componentPos = component.effectivePosition

		val finalMouseX = mouseX - componentPos.x.toInt()
		val finalMouseY = mouseY - componentPos.y.toInt()
		return Pair(finalMouseX, finalMouseY)
	}

}