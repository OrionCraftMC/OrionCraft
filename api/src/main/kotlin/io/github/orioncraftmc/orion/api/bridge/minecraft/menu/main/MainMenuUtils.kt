package io.github.orioncraftmc.orion.api.bridge.minecraft.menu.main

interface MainMenuUtils {
	fun executeMainMenuAction(action: MainMenuAction)
	fun getTranslationForMainMenuAction(action: MainMenuAction): String
}
