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

package io.github.orioncraftmc.orion.api.gui.components.screens

import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Point

open class ComponentOrionScreen : ComponentContainer() {
	private val zeroPoint = Point()

	// We are at the root of the parent tree
	override var parent: Component?
		get() = null
		set(value) {}

	override var anchor: Anchor
		get() = Anchor.TOP_LEFT
		set(value) {}

	override var position: Point
		get() = zeroPoint
		set(value) {}

	override var scale: Double
		get() = 1.0
		set(value) {}

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		super.drawScreen(mouseX, mouseY, renderPartialTicks)
		renderComponent(mouseX, mouseY)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {
		super.handleMouseClick(mouseX, mouseY, clickedButtonId)
	}

	override fun onResize() {
		componentsList.clear()
		val sr = MinecraftBridge.scaledResolution
		size.apply {
			width = sr.scaledWidthFloat.toDouble()
			height = sr.scaledHeightFloat.toDouble()
		}

		super.onResize()
	}

}
