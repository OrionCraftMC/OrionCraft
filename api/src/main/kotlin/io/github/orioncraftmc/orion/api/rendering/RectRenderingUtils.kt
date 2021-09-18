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
import io.github.orioncraftmc.orion.api.bridge.basicShapesRendering
import io.github.orioncraftmc.orion.api.bridge.setColor

object RectRenderingUtils {

	fun drawRectangle(x1: Double, y1: Double, x2: Double, y2: Double, color: Color, isHollow: Boolean = false) {
		val tessellator = TessellatorBridge
		basicShapesRendering {
			if (isHollow) {
				tessellator.startDrawingLineLoop()
				tessellator.setColor(color)
			} else {
				tessellator.startDrawingQuads()
				tessellator.setColor(color)
			}

			tessellator.addVertex(x1, y2, 0.0)
			tessellator.addVertex(x2, y2, 0.0)
			tessellator.addVertex(x2, y1, 0.0)
			tessellator.addVertex(x1, y1, 0.0)

			tessellator.draw()
		}
	}

}
