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

package io.github.orioncraftmc.orion.api.gui.hud.mod

import io.github.orioncraftmc.orion.api.event.EventBus
import io.github.orioncraftmc.orion.api.event.impl.HudModComponentRefreshEvent
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.model.Anchor
import io.github.orioncraftmc.orion.api.mod.ModCategory
import io.github.orioncraftmc.orion.api.mod.OrionMod
import java.util.*

abstract class HudOrionMod<H : Enum<H>>(id: String, name: String) : OrionMod(id, name, ModCategory.HUD) {

	val hudModSetting = setting().hud<H>()
	var hudSettings: MutableMap<H, HudModSettingsModel> by hudModSetting

	abstract val allHudElements: EnumSet<*>

	/**
	 * Returns the component that is rendered on the gui
	 *
	 * This method is given an anchor for the mod to decide how to lay out the information shown
	 * Example: Users can decide to anchor components on the right side of their screen
	 * @param anchor The anchor requested by the user
	 */
	abstract fun getHudComponent(anchor: Anchor, hudElement: Enum<H>): Component?

	/**
	 * Returns the hud component that is used as a fallback when editing the hud gui
	 *
	 * This method is given an anchor for the mod to decide how to lay out the information shown
	 * Example: Users can decide to anchor components on the right side of their screen
	 * @param anchor The anchor requested by the user
	 */
	abstract fun getDummyHudComponent(anchor: Anchor, hudElement: Enum<H>): Component

	/**
	 * Requests OrionCraft to delete the old hud component shown on the hud
	 */
	fun refreshHudComponent(hudElement: Enum<H>) {
		EventBus.callEvent(HudModComponentRefreshEvent(this, hudElement))
	}
}
