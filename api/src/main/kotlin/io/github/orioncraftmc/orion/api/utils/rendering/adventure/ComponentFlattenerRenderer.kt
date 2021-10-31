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

package io.github.orioncraftmc.orion.api.utils.rendering.adventure

import io.github.orioncraftmc.orion.api.bridge.FontRendererBridge
import io.github.orioncraftmc.orion.api.bridge.drawString
import io.github.orioncraftmc.orion.api.bridge.toColor
import net.kyori.adventure.text.flattener.FlattenerListener
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

class ComponentFlattenerRenderer(private val hasShadow: Boolean = true, private val startingX: Int, startingY: Int) : FlattenerListener {
	companion object {
		private const val sectionSign = 167.toChar()

		private val decorationsLegacyMap = mapOf(
			TextDecoration.OBFUSCATED to "k",
			TextDecoration.BOLD to "l",
			TextDecoration.STRIKETHROUGH to "m",
			TextDecoration.UNDERLINED to "n",
			TextDecoration.ITALIC to "o"
		)

	}

	private var x = startingX
	private var y = startingY

	private var color: TextColor = NamedTextColor.WHITE
	private val decorations: MutableSet<TextDecoration> = EnumSet.noneOf(TextDecoration::class.java)

	private val decorationsAsString get() = decorations.joinToString("") { "$sectionSign${decorationsLegacyMap[it]!!}" }

	override fun pushStyle(style: Style) {
		style.color()?.also { color = it }
		decorations.removeAll(getDecorationsWithState(style, TextDecoration.State.FALSE))
		decorations.addAll(getDecorationsWithState(style, TextDecoration.State.TRUE))
	}

	private fun getDecorationsWithState(style: Style, state: TextDecoration.State) =
		style.decorations().filterValues { it == state }.keys

	private val lineHeight = FontRendererBridge.fontHeight
	override fun component(text: String) {
		val lines = text.lines()
		val linesCount = lines.count()

		lines.forEach {
			val finalText = decorationsAsString + it
			val width = FontRendererBridge.getStringWidth(finalText)
			if (linesCount > 1) {
				x = startingX
				y += lineHeight
			}

			FontRendererBridge.drawString(finalText, x, y, color.toColor(), hasShadow)
			x += width
		}
	}

	override fun popStyle(style: Style) {
		decorations.removeAll(getDecorationsWithState(style, TextDecoration.State.TRUE))
	}
}
