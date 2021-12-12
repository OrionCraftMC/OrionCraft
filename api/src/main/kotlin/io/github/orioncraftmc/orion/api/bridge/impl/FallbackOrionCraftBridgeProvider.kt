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

package io.github.orioncraftmc.orion.api.bridge.impl

import io.github.orioncraftmc.orion.api.bridge.OrionCraftBridgeProvider
import io.github.orioncraftmc.orion.api.bridge.minecraft.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.input.KeybindingUtils
import io.github.orioncraftmc.orion.api.bridge.main.MainMenuUtils
import io.github.orioncraftmc.orion.api.bridge.minecraft.resources.ResourceLocationUtils
import io.github.orioncraftmc.orion.api.bridge.rendering.OpenGlBridge
import io.github.orioncraftmc.orion.api.bridge.rendering.TessellatorBridge
import io.github.orioncraftmc.orion.api.logging.FallbackLogger
import io.github.orioncraftmc.orion.api.logging.Logger

internal object FallbackOrionCraftBridgeProvider : OrionCraftBridgeProvider {
	override val minecraftBridge: MinecraftBridge
		get() = bridgeNotImplemented()

	override val openGlBridge: OpenGlBridge
		get() = bridgeNotImplemented()

	override val tessellator: TessellatorBridge
		get() = bridgeNotImplemented()

	override val resourceLocationUtils: ResourceLocationUtils
		get() = bridgeNotImplemented()

	override val mainMenuUtils: MainMenuUtils
		get() = bridgeNotImplemented()

	override val keybindingUtils: KeybindingUtils
		get() = bridgeNotImplemented()

	override val logger: Logger
		get() = FallbackLogger

	private fun bridgeNotImplemented(): Nothing {
		throw NotImplementedError("Fallback Bridge Provider is being used. Someone forgot to set bridges properly!")
	}
}
