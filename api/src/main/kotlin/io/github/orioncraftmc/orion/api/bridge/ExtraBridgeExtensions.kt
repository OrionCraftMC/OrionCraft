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

package io.github.orioncraftmc.orion.api.bridge

import com.github.ajalt.colormath.Color
import io.github.orioncraftmc.orion.api.bridge.rendering.FontRendererBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.TessellatorBridge

fun FontRendererBridge.drawString(value: String, x: Int, y: Int, color: Color, hasShadow: Boolean = false) {
	drawString(value, x, y, color.toSRGB().toRGBInt().argb, hasShadow)
}

 fun TessellatorBridge.setColor(color: Color) {
	val rgb = color.toSRGB()
	setColor(rgb.redInt, rgb.greenInt, rgb.blueInt, rgb.alphaInt)
}

 fun OpenGlBridge.setColor(color: Color) {
	val rgb = color.toSRGB()
	setColor(rgb.redInt, rgb.greenInt, rgb.blueInt, rgb.alphaInt)
}

inline fun matrix(code: () -> Unit) {
	OpenGlBridge.pushMatrix()
	code()
	OpenGlBridge.popMatrix()
}

inline fun basicShapesRendering(code: () -> Unit) {
	matrix {
		OpenGlBridge.enableBlend()
		OpenGlBridge.disableTexture2D()
		OpenGlBridge.enableBlendAlphaMinusSrcAlpha()
		code()
		OpenGlBridge.enableTexture2D()
		OpenGlBridge.disableBlend()
	}
}
