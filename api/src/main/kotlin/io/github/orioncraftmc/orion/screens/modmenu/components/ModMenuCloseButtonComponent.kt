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

package io.github.orioncraftmc.orion.screens.modmenu.components

import io.github.orioncraftmc.orion.api.bridge.TessellatorBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.DrawMode
import io.github.orioncraftmc.orion.api.gui.components.impl.ButtonComponent
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.gui.model.Point

class ModMenuCloseButtonComponent(onClick: () -> Unit) : ButtonComponent("", onClick = onClick) {
	var innerPadding = Padding(5.0)
	override fun renderComponent(mouseX: Int, mouseY: Int) {
		super.renderComponent(mouseX, mouseY)

		val size = effectiveSize
		val topLeft = Point(innerPadding.left, innerPadding.top)
		val bottomRight = Point(size.width - innerPadding.right, size.height - innerPadding.bottom)
		val tesselator = TessellatorBridge

		tesselator.start(DrawMode.LINES)
		tesselator.addVertex(topLeft.x, topLeft.y, 0.0)
		tesselator.addVertex(bottomRight.x, bottomRight.y, 0.0)
		tesselator.draw()
	}
}