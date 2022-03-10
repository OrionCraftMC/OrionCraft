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

package io.github.orioncraftmc.orion.api

import io.github.orioncraftmc.components.OrionComponents
import io.github.orioncraftmc.orion.api.bridge.KeybindingUtils
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.OrionCraftBridgeProvider
import io.github.orioncraftmc.orion.api.bridge.impl.FallbackOrionCraftBridgeProvider
import io.github.orioncraftmc.orion.api.event.impl.LocaleLoadEvent
import io.github.orioncraftmc.orion.api.gui.OrionComponentsBridgeImpl
import io.github.orioncraftmc.orion.api.gui.hud.InGameHudRenderer
import io.github.orioncraftmc.orion.api.keybinding.KeybindingManager
import io.github.orioncraftmc.orion.api.keybinding.OrionKeybinding
import io.github.orioncraftmc.orion.api.keybinding.OrionKeybindingCategory
import io.github.orioncraftmc.orion.api.meta.ClientVersion
import io.github.orioncraftmc.orion.api.mod.ModManager
import io.github.orioncraftmc.orion.api.mod.settings.SettingsProvider
import io.github.orioncraftmc.orion.backport.hooks.NostalgiaKeybindingsHook
import io.github.orioncraftmc.orion.utils.LegacyKeyboardKey
import io.github.orioncraftmc.orion.utils.OrionDiscordIntegration
import kotlin.system.measureTimeMillis

object OrionCraft {

	var clientVersion: ClientVersion = ClientVersion.INVALID
		private set

	var bridges: OrionCraftBridgeProvider = FallbackOrionCraftBridgeProvider
		private set

	lateinit var modManager: ModManager

	lateinit var settingsProvider: SettingsProvider
	lateinit var inGameHudRenderer: InGameHudRenderer

	fun startGameEntrypoint(version: ClientVersion) {
		logger.info("Initializing OrionCraft on Minecraft $version")
		clientVersion = version

		initializeLocaleListener()
	}

	fun setOrionCraftBridgesEntrypoint(bridgeProvider: OrionCraftBridgeProvider) {
		bridges = bridgeProvider
		logger.info("Initialized Orion Bridges on Minecraft $clientVersion successfully")

		doInit()
	}

	private fun doInit() {
		OrionComponents.bridge = OrionComponentsBridgeImpl
		OrionDiscordIntegration.initIntegration()
		OrionDiscordIntegration.updateStateActivity("Initializing..")

		initializeSettings()
		initializeKeybindings()
		initializeMods()
		initializeHudRendererManager()
	}

	private fun initializeLocaleListener() {
		onEvent<LocaleLoadEvent> {
			val assetFileResource = "/assets/orion/lang/${it.locale}.lang"

			val orionTranslationStream = javaClass.getResourceAsStream(assetFileResource)
			if (orionTranslationStream != null) {
				it.properties.load(orionTranslationStream)
				logger.info("Loaded Orion translation file for ${it.locale}")
			}
		}
	}

	private fun initializeKeybindings() {
		KeybindingManager.initialize()

		if (clientVersion.versionData?.hasVanillaPerspectiveKeybinding != true) {
			// Version requires us to register custom keybinding
			NostalgiaKeybindingsHook.togglePerspectiveKeybinding = KeybindingUtils.registerKeybinding(
				OrionKeybinding(
					"nostalgia.perspective_toggle",
					LegacyKeyboardKey.KEY_F5,
					OrionKeybindingCategory.MINECRAFT
				)
			)
		}

		logger.info("Initialized Orion Keybindings successfully")
	}

	private fun initializeSettings() {
		logger.info("Initializing OrionCraft settings")
		settingsProvider = SettingsProvider(MinecraftBridge.gameAppDirectory)

		val time = measureTimeMillis { settingsProvider.load() }
		logger.info("Initialized and loaded OrionCraft settings in $time ms")
	}

	private val modEntrypoints = arrayOf("io.github.orioncraftmc.orion.mods.ModsEntrypoint")

	private fun initializeHudRendererManager() {
		logger.info("Initializing OrionCraft Hud Renderer Manager")
		inGameHudRenderer = InGameHudRenderer()
		logger.info("Initialized OrionCraft Hud Renderer Manager")
	}

	private fun initializeMods() {
		OrionDiscordIntegration.updateStateActivity("Loading mods..")
		logger.info("Initializing OrionCraft Mod Manager")
		modManager = ModManager()
		logger.info("Initialized OrionCraft Mod Manager")

		logger.info("Initializing OrionCraft mods")
		for (modEntrypoint in modEntrypoints) {
			val entrypointClazz = Class.forName(modEntrypoint).kotlin
			val entrypointInstance = entrypointClazz.objectInstance
			if (entrypointInstance is OrionCraftModsEntrypoint) {
				val entrypointName = entrypointClazz.simpleName
				logger.debug("Found mod entrypoint $entrypointName")
				val time = measureTimeMillis {
					entrypointInstance.initializeMods()
				}
				logger.debug("Initialized entrypoint $entrypointName in $time ms")
			}
		}
	}
}
