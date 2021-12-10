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

package io.github.orioncraftmc.orion.api.gui.components.impl.ninepatch

import io.github.orioncraftmc.orion.api.meta.ninepatch.NinePatchElement
import io.github.orioncraftmc.orion.utils.rendering.RectRenderingUtils
import juuxel.libninepatch.TextureRenderer

object NinePatchTextureRenderer : TextureRenderer<NinePatchElement> {
	override fun draw(
		texture: NinePatchElement,
		x: Int,
		y: Int,
		width: Int,
		height: Int,
		u1: Float,
		v1: Float,
		u2: Float,
		v2: Float
	) {
		RectRenderingUtils.drawRectangleWithUv(
			x.toDouble(),
			y.toDouble(),
			(x + width).toDouble(),
			(y + height).toDouble(),
			u1.toDouble(),
			v1.toDouble(),
			u2.toDouble(),
			v2.toDouble(),
		)

		return
	}

}
