package io.github.orioncraftmc.orion.tmic.handlers

import io.github.orioncraftmc.orion.api.event.impl.action.GameActionChangeEvent
import io.github.orioncraftmc.orion.api.logger
import io.github.orioncraftmc.tellmeicheatnow.model.TMICNSupportedServer

object ActionChangeHandler {

	fun handleGameActionChange(e: GameActionChangeEvent) {
		val data = e.data ?: return
		val supportedServer =
			TMICNSupportedServer.values().firstOrNull { it.addressPattern.containsMatchIn(data.serverIp) }
				?: return

		logger.debug("Detected user login into a TMIC supported server ($supportedServer)")
	}
}
