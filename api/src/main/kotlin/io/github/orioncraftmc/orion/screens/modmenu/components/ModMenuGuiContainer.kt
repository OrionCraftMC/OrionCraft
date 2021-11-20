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

package io.github.orioncraftmc.orion.screens.modmenu.components

import io.github.orioncraftmc.meditate.enums.YogaEdge
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.gui.components.flex
import io.github.orioncraftmc.orion.api.gui.components.impl.containers.ComponentContainer
import io.github.orioncraftmc.orion.api.gui.components.nodeSize
import io.github.orioncraftmc.orion.api.gui.model.Size
import io.github.orioncraftmc.orion.utils.ColorConstants

class ModMenuGuiContainer : ComponentContainer() {
	init {
		flex {
			setWidthPercent(70f)
			setHeightAuto()
			aspectRatio = 1.75f
		}

		addComponent(ModMenuCloseButtonComponent { MinecraftBridge.openScreen(null) }.flex {
			nodeSize = Size(10.0)
			setMarginAuto(YogaEdge.LEFT)
			setMargin(YogaEdge.TOP, 5f)
			setMargin(YogaEdge.RIGHT, 5f)
		})

		backgroundColor = ColorConstants.modLabelBackgroundColor
	}
}
