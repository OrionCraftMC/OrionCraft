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

package io.github.orioncraftmc.orion.api.rendering

import com.github.ajalt.colormath.Color
import io.github.orioncraftmc.orion.api.bridge.TessellatorBridge
import io.github.orioncraftmc.orion.api.bridge.setColor
import io.github.orioncraftmc.orion.api.bridge.basicShapesRendering

object RectRenderingUtils {

	fun drawHollowRectangle(x1: Double, y1: Double, x2: Double, y2: Double, color: Color) {
		basicShapesRendering {
			val width = x2 - x1
			val height = y2 - y1
			TessellatorBridge.startDrawingLineLoop()
			TessellatorBridge.setColor(color)

			//Top left
			TessellatorBridge.addVertex(x1, y1, 0.0)
			//Top right
			TessellatorBridge.addVertex(x1 + width, y1, 0.0)
			//Bottom right
			TessellatorBridge.addVertex(x1 + width, y1 + height, 0.0)
			//Bottom left
			TessellatorBridge.addVertex(x1, y1 + height, 0.0)

			TessellatorBridge.draw()
		}
	}

}
