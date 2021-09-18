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
import io.github.orioncraftmc.orion.api.bridge.matrix
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.gui.model.Point
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.api.gui.utils.ComponentUtils
import java.util.*

open class ComponentOrionScreen : OrionScreen(), Component {

	val components: List<Component> by lazy { Collections.unmodifiableList(componentsList) }
	private val componentsList = mutableListOf<Component>()
	private val zeroPosition = Point()

	fun addComponent(component: Component) {
		component.parent = this
		componentsList.add(component)
	}


	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		componentsList.forEach {
			matrix {
				ComponentUtils.offsetCurrentMatrixForComponent(it)
				it.renderComponent(mouseX, mouseY)
			}
		}
		super.drawScreen(mouseX, mouseY, renderPartialTicks)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {
		super.handleMouseClick(mouseX, mouseY, clickedButtonId)
	}

	override fun onResize() {
		val sr = MinecraftBridge.scaledResolution
		size.apply {
			width = sr.scaledWidthFloat.toDouble()
			height = sr.scaledHeightFloat.toDouble()
		}

		super.onResize()
	}

	override fun renderComponent(mouseX: Int, mouseY: Int) {
	}

	override var anchor: Anchor
		get() = Anchor.TOP_LEFT
		set(value) {}

	final override var padding: Padding = Padding(0.0)

	override var position: Point
		get() {
			return zeroPosition
		}
		set(value) {}

	override var size: Size = Size()

	// We are at the root of the parent tree
	override var parent: Component?
		get() = null
		set(value) {}
}
