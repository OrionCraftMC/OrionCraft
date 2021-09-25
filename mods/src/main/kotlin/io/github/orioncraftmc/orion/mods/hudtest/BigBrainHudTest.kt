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

package io.github.orioncraftmc.orion.mods.hudtest

import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.minecraft.item.inventory.PlayerInventoryBridge
import io.github.orioncraftmc.orion.api.event.impl.HudRenderEvent
import io.github.orioncraftmc.orion.api.mod.ModCategory
import io.github.orioncraftmc.orion.api.mod.OrionMod
import io.github.orioncraftmc.orion.api.on

object BigBrainHudTest: OrionMod("hudtest", "Hud Test", ModCategory.NEW) {
	init {
		on<HudRenderEvent> {
			MinecraftBridge.fontRenderer.drawString("Orion Hud Render Test", 0, 0, 0xFF0000)

			OpenGlBridge.setColor(255, 255, 255, 255)
			val inv = MinecraftBridge.player.playerInventory
			OpenGlBridge.enableRescaleNormal()
			OpenGlBridge.enableBlend()
			OpenGlBridge.enableBlendAlphaMinusSrcAlpha()
			OpenGlBridge.enableStandardItemLighting()

			for (j in 3 downTo 0) {
				val k = (MinecraftBridge.scaledResolution.scaledWidth / 2 - 90 - j * 20 + 2) + 50
				val l = 5
				renderArmorProper(3 - j, k, l, it.tickDelta, inv)
			}
		}
	}

	private fun renderArmorProper(i: Int, j: Int, k: Int, tickDelta: Float, inv: PlayerInventoryBridge) {
		val stack = inv.armorInventory[i]
		if (stack != null) {
			val g = stack.animationsToGo - tickDelta
			if (g > 0.0F) {
				OpenGlBridge.pushMatrix()
				val h = 1.0F + g / 5.0F
				OpenGlBridge.translate((j + 8).toDouble(), (-(k + 12)).toDouble(), 0.0)
				OpenGlBridge.scale(1.5 / h, (h + 1.5) / 2.0F, 1.5)
				OpenGlBridge.translate(((-(j + 8)).toDouble()), ((-(k + 12)).toDouble()), 0.0)
			}
			MinecraftBridge.defaultRenderItem.renderItemAndEffectIntoGui(stack, j + 60, k)
			if (g > 0.0F) {
				OpenGlBridge.popMatrix()
			}
			MinecraftBridge.defaultRenderItem.renderItemOverlayIntoGui(stack, j + 60, k)
		}
	}
}
