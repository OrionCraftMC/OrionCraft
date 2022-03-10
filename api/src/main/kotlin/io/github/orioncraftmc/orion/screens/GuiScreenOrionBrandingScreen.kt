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

package io.github.orioncraftmc.orion.screens

import io.github.orioncraftmc.components.model.Anchor
import io.github.orioncraftmc.components.model.Padding
import io.github.orioncraftmc.components.utils.ComponentUtils
import io.github.orioncraftmc.orion.api.OrionCraftConstants
import io.github.orioncraftmc.orion.api.bridge.gui.GuiScreenBridge
import io.github.orioncraftmc.orion.api.bridge.matrix
import io.github.orioncraftmc.orion.api.gui.ParentComponentHelper
import io.github.orioncraftmc.orion.api.gui.components.impl.LabelComponent
import io.github.orioncraftmc.orion.api.gui.hud.editor.ModsEditorScreen
import io.github.orioncraftmc.orion.utils.BrandingUtils

class GuiScreenOrionBrandingScreen(val gui: GuiScreenBridge) : ParentComponentHelper() {

	private val brandingComponent = BrandingUtils.getBrandingComponent(1.5, true).apply {
		parent = parentComponent
		padding = Padding(2.0)
		anchor = Anchor.BOTTOM_RIGHT
	}

	private val clientName = (LabelComponent(OrionCraftConstants.mainMenuClientName).apply {
		parent = parentComponent
		padding = Padding(2.0)
		anchor = Anchor.BOTTOM_LEFT
	})


	fun drawScreen() {
		updateParentComponent()
		if (gui is ModsEditorScreen) {
			return
		}
		matrix {
			ComponentUtils.renderComponent(brandingComponent, 0, 0)
		}
		matrix {
			ComponentUtils.renderComponent(clientName, 0, 0)
		}
	}
}
