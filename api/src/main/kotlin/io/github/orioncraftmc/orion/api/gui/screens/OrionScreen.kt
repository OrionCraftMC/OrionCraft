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

@file:Suppress("UNUSED_PARAMETER")

package io.github.orioncraftmc.orion.api.gui.screens

import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.gui.GuiScreenBridge

interface OrionScreen : GuiScreenBridge {

	override var zLevel: Float
		get() = 0.0f
		set(value) {}

	fun internalOnResize() {
		onResize()
	}

	fun drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float) {}

	fun handleMouseClick(mouseX: Int, mouseY: Int, clickedButtonId: Int) {}

	/**
	 * Invoked when the mouse button has been clicked (pressed and released).
	 */
	fun handleMouseClick(mouseX: Int, mouseY: Int) {}

	/**
	 * Invoked when a mouse button has been released.
	 */
	fun handleMouseRelease(mouseX: Int, mouseY: Int) {}

	/**
	 * Invoked when a mouse button has been moved.
	 */
	fun handleMouseMove(mouseX: Int, mouseY: Int, button: Int) {}

	fun onResize() {}

	fun onClose() {}

	fun drawDefaultBackground() {
		MinecraftBridge.drawDefaultBackground()
	}
}
