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

package io.github.orioncraftmc.orion.api.gui

import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.minecraft.ScaledResolutionBridge
import io.github.orioncraftmc.orion.api.gui.components.impl.RectangleComponent

open class ParentComponentHelper {
	val parentComponent = RectangleComponent()
	var lastGameWidth = 0
	var lastGameHeight = 0
	var lastGuiScale = -1
	lateinit var lastScaledResolution: ScaledResolutionBridge

	protected fun updateParentComponent() {
		if (MinecraftBridge.gameWidth != lastGameWidth || MinecraftBridge.gameHeight != lastGameHeight || MinecraftBridge.gameSettings.guiScale != lastGuiScale) {
			parentComponent.size.apply {
				lastScaledResolution = MinecraftBridge.scaledResolution
				width = lastScaledResolution.scaledWidthFloat.toDouble()
				height = lastScaledResolution.scaledHeightFloat.toDouble()
			}
		}

		lastGameWidth = MinecraftBridge.gameWidth
		lastGameHeight = MinecraftBridge.gameHeight
		lastGuiScale = MinecraftBridge.gameSettings.guiScale
	}
}
