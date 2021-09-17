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

import io.github.orioncraftmc.orion.api.gui.screens.OrionScreen

abstract class MainMenuScreen : OrionScreen {

	// Implementation is provided by version
	abstract fun renderSkybox(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	// Implementation is provided by version
	abstract fun superHandleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int)

	// Implementation is provided by version
	abstract fun superDrawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float)

	override fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {
		renderSkybox(mouseX, mouseY, renderPartialTicks)

		//call to super.drawScreen to render vanilla buttons
		superDrawScreen(mouseX, mouseY, renderPartialTicks)
	}

	override fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {

		//call to super.mouseClicked to handle clicks on vanilla buttons
		superHandleMouseClick(mouseX, mouseY, clickedButtonId)
	}
}
