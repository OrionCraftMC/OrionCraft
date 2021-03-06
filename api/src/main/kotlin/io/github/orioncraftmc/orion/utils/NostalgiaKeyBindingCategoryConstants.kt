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

package io.github.orioncraftmc.orion.utils

internal object NostalgiaKeyBindingCategoryConstants {
	private val categories = mapOf(
		"Gameplay" to listOf(
			"key.attack",
			"key.drop",
			"key.pickItem",
			"key.use"
		),
		"Movement" to listOf(
			"key.jump",
			"key.sneak",
			"key.left",
			"key.right",
			"key.back",
			"key.forward"
		),
		"Multiplayer" to listOf(
			"key.playerlist",
			"key.chat",
			"key.command"
		)
	)

	@JvmStatic
	fun getKeyCategory(name: String): String {
		return categories.entries.firstOrNull { it.value.contains(name) }?.key ?: "Minecraft"
	}
}
