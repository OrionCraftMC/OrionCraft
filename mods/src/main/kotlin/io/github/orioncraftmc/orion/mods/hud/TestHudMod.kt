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

package io.github.orioncraftmc.orion.mods.hud

import com.github.ajalt.colormath.model.RGB
import io.github.orioncraftmc.components.Component
import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.components.impl.LabelComponent
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod

object TestHudMod : HudOrionMod<TestHudMod.Elements>("hudtest", "Hud Test") {

	enum class Elements {
		TEST_ONE,
		TEST_TWO
	}

	override fun getHudComponent(anchor: Anchor, hudElement: Elements): Component {
		return when (hudElement) {
			Elements.TEST_ONE ->
				object : LabelComponent("Test Hud Mod - Hello world!") {
					override var text: String
						get() = "${MinecraftBridge.gameWidth}x${MinecraftBridge.gameHeight}"
						set(value) {}
				}.apply {
					color = RGB("#FF5555")
				}
			Elements.TEST_TWO ->
				LabelComponent("Test Hud Mod - Hello world!").apply {
					color = RGB("#FFAA00")
				}
		}
	}

	override fun getDummyHudComponent(anchor: Anchor, hudElement: Elements): Component {
		return getHudComponent(anchor, hudElement)
	}
}
