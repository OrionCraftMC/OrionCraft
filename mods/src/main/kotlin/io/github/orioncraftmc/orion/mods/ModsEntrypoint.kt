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

package io.github.orioncraftmc.orion.mods

import io.github.orioncraftmc.orion.api.OrionCraftModsEntrypoint
import io.github.orioncraftmc.orion.api.gui.hud.mod.single.simple.SimpleTextHudOrionMod
import io.github.orioncraftmc.orion.mods.fullbright.FullBrightMod
import io.github.orioncraftmc.orion.mods.hud.FpsHudMod

object ModsEntrypoint : OrionCraftModsEntrypoint {
	override fun initializeMods() {
		registerMod(FullBrightMod)
		registerMod(FpsHudMod)
		for (i in 1..3) registerMod(exampleMod(i))
	}

	private fun exampleMod(i: Int) = object : SimpleTextHudOrionMod("example$i", "Hud Mod $i") {
		override val value: String
			get() = "Mod #$i"
	}

}
