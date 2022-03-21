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

package io.github.orioncraftmc.orion.screens.menu.main.components

import io.github.orioncraftmc.components.flex
import io.github.orioncraftmc.components.nodeSize
import io.github.orioncraftmc.meditate.enums.YogaEdge
import io.github.orioncraftmc.orion.api.bridge.MainMenuUtils
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.main.MainMenuAction
import io.github.orioncraftmc.orion.api.gui.components.impl.ButtonComponent
import io.github.orioncraftmc.orion.api.gui.hud.editor.ModsEditorScreen
import io.github.orioncraftmc.orion.screens.menu.main.buttonGapSize
import io.github.orioncraftmc.orion.screens.menu.main.menuButtonSize

class MainMenuButtonComponent(val action: MainMenuAction) :
	ButtonComponent(getTranslationForAction(action)) {

	init {
		flex {
			nodeSize = menuButtonSize
			setMargin(YogaEdge.BOTTOM, buttonGapSize)
		}
		onClick = { executeMainMenuAction() }
	}

	private fun executeMainMenuAction() {
		if (action == MainMenuAction.ORIONCRAFT_SETTINGS) {
			MinecraftBridge.openScreen(ModsEditorScreen(true))
			return
		}
		MainMenuUtils.executeMainMenuAction(action)
	}
}

private fun getTranslationForAction(action: MainMenuAction): String {
	if (action == MainMenuAction.ORIONCRAFT_SETTINGS) {
		return "OrionCraft Settings"
	}
	return MainMenuUtils.getTranslationForMainMenuAction(action)
}
