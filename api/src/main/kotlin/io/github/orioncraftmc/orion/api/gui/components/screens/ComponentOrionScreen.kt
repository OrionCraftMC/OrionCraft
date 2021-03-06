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

@file:Suppress("UNUSED_PARAMETER")

package io.github.orioncraftmc.orion.api.gui.components.screens

import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.components.containers.ComponentContainer
import io.github.orioncraftmc.components.flex
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Point
import io.github.orioncraftmc.components.utils.ComponentUtils
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.screens.OrionScreen
import kotlin.math.roundToInt

open class ComponentOrionScreen(isFlex: Boolean = false) : ComponentContainer(), OrionScreen {
	init {
		if (isFlex) flex {}
	}

	override var parentScreen: OrionScreen? = null

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
		super<OrionScreen>.handleMouseClick(mouseX, mouseY, clickedButtonId)
		super<ComponentContainer>.handleMouseClick(mouseX, mouseY)
		handleMouseClick(mouseX, mouseY)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int) {
	}

	override fun handleMouseRelease(mouseX: Int, mouseY: Int) {
		super<OrionScreen>.handleMouseRelease(mouseX, mouseY)
		super<ComponentContainer>.handleMouseRelease(mouseX, mouseY)
	}

	override fun onResize() {
		val sr = MinecraftBridge.scaledResolution
		size.apply {
			width = sr.scaledWidthFloat.toDouble()
			height = sr.scaledHeightFloat.toDouble()
		}

		ComponentUtils.performRootComponentLayout(this, true)

		super.onResize()
	}

	override val width: Int
		get() = size.width.roundToInt()

	override val height: Int
		get() = size.height.roundToInt()

}
