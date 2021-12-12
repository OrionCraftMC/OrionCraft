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

package io.github.orioncraftmc.orion.utils.rendering

import com.github.ajalt.colormath.Color
import io.github.orioncraftmc.orion.api.bridge.*
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.DrawMode
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.GlCapability

object RectRenderingUtils {

	fun drawRectangle(
		x1: Double,
		y1: Double,
		x2: Double,
		y2: Double,
		color: Color,
		isHollow: Boolean = false,
		lineWidth: Double = 1.0
	) {

		val finalX1 = minOf(x1, x2)
		val finalX2 = maxOf(x1, x2)
		val finalY1 = minOf(y1, y2)
		val finalY2 = maxOf(y1, y2)

		val tessellator = TessellatorBridge

		val halfLineWidth = lineWidth / 2.0

		basicShapesRendering {
			if (isHollow) {
				OpenGlBridge.setLineWidth(lineWidth.toFloat())
				tessellator.start(DrawMode.LINE_LOOP)
				tessellator.setColor(color)
			} else {
				tessellator.start(DrawMode.QUADS)
				tessellator.setColor(color)
			}

			tessellator.addVertex(finalX1 - halfLineWidth, finalY2 + halfLineWidth, 0.0)
			tessellator.addVertex(finalX2 + halfLineWidth, finalY2 + halfLineWidth, 0.0)
			tessellator.addVertex(finalX2 + halfLineWidth, finalY1 - halfLineWidth, 0.0)
			tessellator.addVertex(finalX1 - halfLineWidth, finalY1 - halfLineWidth, 0.0)

			tessellator.draw()
		}
		OpenGlBridge.resetColor()
	}

	fun drawRectangleWithUv(
		x1: Double,
		y1: Double,
		x2: Double,
		y2: Double,
		u1: Double,
		v1: Double,
		u2: Double,
		v2: Double
	) {

		val finalX1 = minOf(x1, x2)
		val finalX2 = maxOf(x1, x2)
		val finalY1 = minOf(y1, y2)
		val finalY2 = maxOf(y1, y2)

		val finalU1 = minOf(u1, u2)
		val finalU2 = maxOf(u1, u2)
		val finalV1 = minOf(v1, v2)
		val finalV2 = maxOf(v1, v2)

		val tessellator = TessellatorBridge

		matrix {
			OpenGlBridge.enableCapability(GlCapability.TEXTURE_2D)
			blend {
				tessellator.start(DrawMode.QUADS)
				tessellator.setTesselatorColor(255, 255, 255, 255)
				tessellator.addVertexWithUV(finalX1, finalY2, 0.0, finalU1, finalV2)
				tessellator.addVertexWithUV(finalX2, finalY2, 0.0, finalU2, finalV2)
				tessellator.addVertexWithUV(finalX2, finalY1, 0.0, finalU2, finalV1)
				tessellator.addVertexWithUV(finalX1, finalY1, 0.0, finalU1, finalV1)
				tessellator.draw()
			}
		}
		OpenGlBridge.resetColor()
	}

}
