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
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size
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
		val (size, padding) = computeEffectiveProperties(component)

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

	private val emptyPadding = Padding(0.0)
	fun computeEffectiveProperties(component: Component): Pair<Size, Padding> {
		if (component.parent != null && component.flexLayoutNode != null) {
			return component.effectiveSize to emptyPadding
		}

		return component.size to component.padding
	}

	fun computeEffectivePosition(component: Component): Point {
		val flexLayoutNode = component.flexLayoutNode
		val parent = component.parent ?: throw UnsupportedOperationException("Component is always known to have Parent")
		if (flexLayoutNode != null && parent is ComponentContainer) {
 			performRootComponentLayout(parent)
			return Point(flexLayoutNode.layoutX.toDouble(), flexLayoutNode.layoutY.toDouble())
		}

		val (parentSize, parentPadding) = computeEffectiveProperties(parent)

		return AnchorUtils.computePosition(
			component.position,
			component.size,
			component.anchor,
			parentSize,
			component.padding,
			parentPadding,
			component.scale
		)
	}

	fun computeEffectiveSize(component: Component): Size {
		val flexLayoutNode = component.flexLayoutNode
		val parent = component.parent
		if (flexLayoutNode != null && parent is ComponentContainer) {
			performRootComponentLayout(parent)
			return Size(flexLayoutNode.layoutWidth.toDouble(), flexLayoutNode.layoutHeight.toDouble())
		}

		return (component.size + component.padding) * component.scale
	}

	fun performRootComponentLayout(container: ComponentContainer, force: Boolean = false) {
		// Perform layout of the upper components
		(container.parent as? ComponentContainer)?.let { performRootComponentLayout(it); }

		val flexLayoutNode = container.flexLayoutNode ?: return

		if (force || flexLayoutNode.isDirty) {
			flexLayoutNode.calculateLayout(container.size.width.toFloat(), container.size.height.toFloat())
		}
	}

}
