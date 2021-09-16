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

package io.github.orioncraftmc.orion.api.gui.screens.impl

import com.github.ajalt.colormath.model.RGBInt
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.extras.zodiac.Zodiac
import io.github.orioncraftmc.orion.api.gui.extras.zodiac.settings.HorizontalDirection
import io.github.orioncraftmc.orion.api.gui.extras.zodiac.settings.VerticalDirection
import io.github.orioncraftmc.orion.api.gui.extras.zodiac.settings.ZodiacSettings
import io.github.orioncraftmc.orion.api.gui.screens.OrionScreen

abstract class MainMenuScreen : OrionScreen {

	private val zodiac = Zodiac(ZodiacSettings(
		HorizontalDirection.RANDOM,
		VerticalDirection.RANDOM,

		0.5,
		0.5,

		density = 500,

		bounceX = true,
		bounceY = true,

		dotRadiusMin = 1.0,
		dotRadiusMax = 2.5,

		dotColor = RGBInt(255, 255, 255, 75),
		linkColor = RGBInt(255, 255, 255, 65),

		linkDistance = 15,
		linkWidth = 2
	))

	override fun onInitOrResize(width: Int, height: Int) {
		zodiac.refresh(MinecraftBridge.gameWidth, MinecraftBridge.gameHeight)
	}

	// Implementation is provided by version
	abstract fun renderSkybox(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		renderSkybox(mouseX, mouseY, renderPartialTicks)
		val scaledResolution = MinecraftBridge.scaledResolution
		zodiac.draw(scaledResolution.scaledWidth, scaledResolution.scaledHeight)
	}
}
