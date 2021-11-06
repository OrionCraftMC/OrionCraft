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

package io.github.orioncraftmc.orion.backport.gui.controls.entries

import io.github.orioncraftmc.orion.api.bridge.FontRendererBridge
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.TessellatorBridge
import io.github.orioncraftmc.orion.backport.gui.controls.ControlsListEntry

//TODO: i18n
class ControlsListCategoryEntry(val text: String) : ControlsListEntry {
	private val textWidth = FontRendererBridge.getStringWidth(text)

	override fun drawEntry(
		var1: Int,
		var2: Int,
		var3: Int,
		var4: Int,
		var5: Int,
		var6: TessellatorBridge,
		var7: Int,
		var8: Int,
		var9: Boolean
	) {
		FontRendererBridge.drawString(
			text,
			MinecraftBridge.currentOpenedScreen!!.width / 2 - textWidth / 2,
			var3 + var5 - FontRendererBridge.fontHeight - 1,
			(-1).toUInt()
		)

	}

	override fun mousePressed(
		index: Int,
		mouseX: Int,
		mouseY: Int,
		mouseEvent: Int,
		relativeX: Int,
		relativeY: Int
	): Boolean {
		TODO("Not yet implemented")
	}

	override fun mouseReleased(index: Int, mouseX: Int, mouseY: Int, mouseEvent: Int, relativeX: Int, relativeY: Int) {
		TODO("Not yet implemented")
	}
}
