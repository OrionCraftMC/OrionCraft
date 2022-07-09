package io.github.orioncraftmc.orion.utils.pinger

import io.github.orioncraftmc.orion.utils.pinger.model.ServerPingResult


interface ServerPinger {
	fun ping(address: String, port: Int): ServerPingResult?
}
