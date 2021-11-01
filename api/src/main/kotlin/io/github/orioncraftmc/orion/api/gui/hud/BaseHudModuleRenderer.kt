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

package io.github.orioncraftmc.orion.api.gui.hud

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.event.impl.HudModComponentRefreshEvent
import io.github.orioncraftmc.orion.api.gui.ParentComponentHelper
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudModSettingsModel
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod
import io.github.orioncraftmc.orion.api.gui.hud.mod.single.SingleHudOrionMod
import io.github.orioncraftmc.orion.api.onEvent

abstract class BaseHudModuleRenderer(val includeDummyComponents: Boolean = false) : ParentComponentHelper() {

	init {
		onEvent<HudModComponentRefreshEvent<*>> {
			// Delete current component as the next frame the component will be refreshed.
			removeHudModComponent(it.mod, it.element)
		}
	}

	val modElementComponents: Table<HudOrionMod<*>, Enum<*>, Component> =
		HashBasedTable.create()

	fun destroyAllComponents() {
		// Destroy all components in screen and refresh them all next frame
		modElementComponents.clear()
	}

	fun renderHudElements() {
		// First, update our parent information to be synced with the game window size
		updateParentComponent()

		// Go through all hud mods, find which components are supposed to be added and removed
		OrionCraft.modManager.mods.values.filterIsInstance<HudOrionMod<*>>().forEach { hudMod ->
			hudMod.allHudElements.forEach hudElements@{ hudElement ->
				// Component is not supposed to be visible anymore
				if (hasElementVisible(hudMod, hudElement) && !hasElementEnabled(hudMod, hudElement)) {
					removeHudModComponent(hudMod, hudElement)
					return@forEach
				}

				// Component is supposed to be visible, but is not on screen
				if (hasElementEnabled(hudMod, hudElement)) {
					prepareHudModComponent(hudMod, hudElement)
				}
			}
		}

		// Render all components to the screen
		modElementComponents.cellSet().forEach {
			renderComponent(it.rowKey, it.columnKey, it.value)
		}
	}

	abstract fun renderComponent(mod: HudOrionMod<*>, hudElement: Enum<*>, component: Component)

	private fun hasElementEnabled(
		hudMod: HudOrionMod<*>,
		hudElement: Enum<*>
	): Boolean {
		// If the mod only has one hud element, only check if the mod is enabled.
		// Otherwise, check if the specific element is enabled as well.
		return hudMod.isEnabled && (hudMod is SingleHudOrionMod || getHudElementSettings(hudMod, hudElement).enabled)
	}

	private fun prepareHudModComponent(hudMod: HudOrionMod<*>, hudElement: Enum<*>) {
		if (hasElementVisible(hudMod, hudElement)) return
		val hudSettings = getHudElementSettings(hudMod, hudElement)
		var component = hudMod.getHudComponent(hudSettings.anchor, @Suppress("TYPE_MISMATCH") hudElement)

		if (component == null && includeDummyComponents) {
			component = hudMod.getDummyHudComponent(hudSettings.anchor, @Suppress("TYPE_MISMATCH") hudElement)
		}

		if (component != null) {
			// Apply component settings
			applyComponentSettings(component, hudSettings)
			modElementComponents.put(hudMod, hudElement, component)
		}
	}

	fun applyComponentSettings(
		component: Component,
		hudSettings: HudModSettingsModel
	) {
		component.apply {
			parent = parentComponent
			anchor = hudSettings.anchor
			position = hudSettings.position
			scale = hudSettings.scale
		}
	}

	fun getHudElementSettings(
		hudMod: HudOrionMod<*>,
		hudElement: Enum<*>
	) = hudMod.hudSettings[hudElement] ?: HudModSettingsModel().also { hudMod.hudSettings[@Suppress("TYPE_MISMATCH") hudElement] = it }

	protected fun removeHudModComponent(mod: HudOrionMod<*>, hudElement: Enum<*>) {
		modElementComponents.remove(mod, hudElement)
	}

	private fun hasElementVisible(hudMod: HudOrionMod<*>, hudElement: Enum<*>) =
		modElementComponents.contains(hudMod, hudElement)
}
