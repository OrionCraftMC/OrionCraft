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

package io.github.orioncraftmc.orion.api.gui.components

import com.github.ajalt.colormath.Color
import io.github.orioncraftmc.meditate.YogaNode
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.utils.gui.ComponentUtils
import io.github.orioncraftmc.orion.utils.gui.ComponentUtils.computeEffectiveSize

/**
 * A UI element that can be rendered on the screen.
 */
interface Component {
	/**
	 * Render the component on the screen.
	 *
	 * @param mouseX The x-coordinate of the mouse.
	 * @param mouseY The y-coordinate of the mouse.
	 */
	fun renderComponent(mouseX: Int, mouseY: Int)

	/**
	 * Invoked when the mouse button has been clicked (pressed and released) on a component.
	 *
	 * @param mouseX The x-coordinate of the mouse.
	 * @param mouseY The y-coordinate of the mouse.
	 */
	fun handleMouseClick(mouseX: Int, mouseY: Int) {}

	/**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param mouseX The x-coordinate of the mouse.
	 * @param mouseY The y-coordinate of the mouse.
	 */
	fun handleMouseRelease(mouseX: Int, mouseY: Int) {}

	var anchor: Anchor

	var padding: Padding

	var position: Point

	var size: Size

	var scale: Double

	var snapToDevicePixels: Boolean

	val effectiveSize: Size
		get() = computeEffectiveSize(this)

	val effectivePosition: Point
		get() {
			if (parent == null) return position

			return ComponentUtils.computeEffectivePosition(this)
		}

	val effectiveLeft: Double
		get() = effectivePosition.x

	val effectiveTop: Double
		get() = effectivePosition.y

	val effectiveRight: Double
		get() = effectiveLeft + effectiveSize.width

	val effectiveBottom: Double
		get() = effectiveTop + effectiveSize.height

	var parent: Component?

	var backgroundColor: Color?

	var flexLayoutNode: YogaNode?
}
