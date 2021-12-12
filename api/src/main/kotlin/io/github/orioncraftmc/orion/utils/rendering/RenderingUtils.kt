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
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.TessellatorBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.DrawMode
import io.github.orioncraftmc.orion.api.bridge.rendering.enums.GlCapability
import io.github.orioncraftmc.orion.api.bridge.setColor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object RenderingUtils {
	const val DOUBLE_PI = Math.PI * 2
	const val FULL_ANGLE = 360.0
	val HALF_CIRCLE_RADIANS = Math.toRadians(180.0)

	fun drawFilledArc(
		x: Double,
		y: Double,
		radius: Int,
		startAngle: Int,
		endAngle: Int,
		color: Color,
		lineWidth: Float = 1.0F
	) {
		drawArc(x, y, radius, startAngle, endAngle, color, lineWidth, true)
	}

	fun drawCircle(cx: Double, cy: Double, radius: Int, color: Color, width: Float) {
		val numSegments = 90

		val theta = DOUBLE_PI / numSegments
		val tangentialFactor = tan(theta)
		val radialFactor = cos(theta)
		var x = radius.toDouble()
		var y = 0.0

		val tessellator = TessellatorBridge
		tessellator.start(DrawMode.LINE_LOOP)
		tessellator.setColor(color)
		OpenGlBridge.setLineWidth(width)

		for (i in 0..numSegments) {
			tessellator.addVertex(x + cx, y + cy, 0.0)

			//calculate the tangential vector
			//remember, the radial vector is (x, y)
			//to get the tangential vector we flip those coordinates and negate one of them
			val tx = -y
			val ty = x

			//add the tangential vector

			x += tx * tangentialFactor
			y += ty * tangentialFactor

			//correct using the radial factor
			x *= radialFactor
			y *= radialFactor
		}

		tessellator.draw()
	}

	fun drawArc(
		x: Double,
		y: Double,
		radius: Int,
		startAngle: Int,
		endAngle: Int,
		color: Color,
		lineWidth: Float = 1.0F,
		filled: Boolean = false
	) {
		val arcResolution = 180
		OpenGlBridge.pushMatrix()
		OpenGlBridge.enableCapability(GlCapability.BLEND)
		OpenGlBridge.disableCapability(GlCapability.TEXTURE_2D)
		OpenGlBridge.enableBlendAlphaMinusSrcAlpha()
		val tessellator = TessellatorBridge

		if (filled) {
			// If we want a filled arc, we can use Triangle Fan to draw it
			tessellator.start(DrawMode.TRIANGLE_FAN)
		} else {
			// Otherwise, draw a line loop
			tessellator.start(DrawMode.LINE_LOOP)
		}

		tessellator.setColor(color)

		if (filled) {
			//Only add the center vertex if using filled
			tessellator.addVertex(x, y, 0.0)
		}
		for (step in (startAngle / FULL_ANGLE * arcResolution).toInt()..(endAngle / FULL_ANGLE * arcResolution).toInt()) {
			val angle = DOUBLE_PI * step / arcResolution + HALF_CIRCLE_RADIANS
			tessellator.addVertex(x + sin(angle) * radius, y + cos(angle) * radius, 0.0)
		}

		OpenGlBridge.setLineWidth(lineWidth)
		tessellator.draw()

		OpenGlBridge.enableCapability(GlCapability.TEXTURE_2D)
		OpenGlBridge.disableCapability(GlCapability.BLEND)
		OpenGlBridge.popMatrix()
	}
}
