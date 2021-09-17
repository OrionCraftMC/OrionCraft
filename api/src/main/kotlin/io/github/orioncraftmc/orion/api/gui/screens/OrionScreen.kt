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

package io.github.orioncraftmc.orion.api.gui.screens

import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.components.ParentComponent
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size

abstract class OrionScreen : ParentComponent {
	open fun onResize() {}

	fun internalOnResize() {
		val sr = MinecraftBridge.scaledResolution
		size.apply {
			width = sr.scaledWidthFloat.toDouble()
			height = sr.scaledHeightFloat.toDouble()
		}
		onResize()
	}

	open fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {}

	open fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {}

	override var position: Point = Point(0.0, 0.0)

	override var size: Size = Size(0.0, 0.0)

	override var anchor: Anchor = Anchor.TOP_LEFT

	fun drawDefaultBackground() {
		MinecraftBridge.drawDefaultBackground()
	}
}
