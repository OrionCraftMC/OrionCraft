package io.github.orioncraftmc.orion.tmic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.orioncraftmc.orion.api.OrionCraftModsEntrypoint
import io.github.orioncraftmc.orion.api.logger
import io.github.orioncraftmc.orion.api.onEvent
import io.github.orioncraftmc.orion.tmic.handlers.ActionChangeHandler

object TmicEntrypoint : OrionCraftModsEntrypoint {

	override fun initializeMods() {
		if (!initTmicn()) {
			logger.info("Skipping loading of tell-me-i-cheat-now-lib. Required constants are not present")
		}
		
		onEvent(ActionChangeHandler::handleGameActionChange)
	}

	private val objectMapper by lazy { jacksonObjectMapper() }
	private fun initTmicn(): Boolean {
		val constantsFileResource = "/assets/orion/data/landia-constants.json"

		val constantsJson =
			javaClass.getResourceAsStream(constantsFileResource)
				?.readAllBytes()
				?.decodeToString()
				?: return false

		// Register the Landia constants if present
		// TODO

		return true
	}
}
