package io.github.orioncraftmc.orion.utils.pinger.model

import io.github.orioncraftmc.orion.utils.pinger.model.players.ServerPingResultPlayerData
import net.kyori.adventure.text.Component

data class ServerPingResult(
	val motd: Component,
	val version: ServerPingVersionData,
	val players: ServerPingResultPlayerData,
	val icon: String?,
	val time: Int
)
