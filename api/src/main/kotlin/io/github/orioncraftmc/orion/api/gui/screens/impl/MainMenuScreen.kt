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

import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.gui.components.impl.LabelComponent
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.api.gui.components.screens.ComponentOrionScreen
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.gui.model.Padding
import io.github.orioncraftmc.orion.api.utils.BrandingUtils

abstract class MainMenuScreen : ComponentOrionScreen() {

	private val logoContainer = ComponentContainer().apply {
		addComponent(BrandingUtils.getBrandingComponent(3.5).apply {
			anchor = Anchor.BOTTOM_MIDDLE
			padding = Padding(0.0, 0.0, 0.0, 10.0)
		})
	}

	init {
		padding = Padding(5.0)
		addLabels()
		addOrionCraftLogo()
	}

	private fun addOrionCraftLogo() {
		addComponent(logoContainer)
	}

	private fun updateOrionLogoContainerSize() {
		logoContainer.apply {
			size.apply {
				width = this@MainMenuScreen.size.width
				height = this@MainMenuScreen.size.height / 4 + 48
			}
		}
	}

	override fun onResize() {
		super.onResize()
		updateOrionLogoContainerSize()
	}

	private fun addLabels() {
		addComponent(LabelComponent("OrionCraft ${OrionCraft.clientVersion}").apply {
			anchor = Anchor.BOTTOM_LEFT
		})

		addComponent(LabelComponent("Copyright Mojang Studios. Do not distribute!").apply {
			anchor = Anchor.BOTTOM_RIGHT
		})
	}


	// Implementation is provided by version
	abstract fun renderSkybox(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	// Implementation is provided by version
	abstract fun superHandleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int)

	// Implementation is provided by version
	abstract fun superDrawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		renderSkybox(mouseX, mouseY, renderPartialTicks)

		super.drawScreen(mouseX, mouseY, renderPartialTicks)

		//call to super.drawScreen to render vanilla buttons
		superDrawScreen(mouseX, mouseY, renderPartialTicks)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {
		//call to super.mouseClicked to handle clicks on vanilla buttons
		superHandleMouseClick(mouseX, mouseY, clickedButtonId)
	}
}
