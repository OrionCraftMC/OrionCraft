package io.github.orioncraftmc.orion.api.event.impl.action

data class GameActionConnectionData constructor(
	val serverIp: String, val serverPort: Int
) {

	companion object {
		@JvmStatic
		fun fromServerIp(ip: String): GameActionConnectionData {
			val data = ip.split(':', limit = 2)
			val serverPort =
				(data.getOrNull(1) ?: "25565").let { kotlin.runCatching { Integer.parseInt(it) }.getOrNull() ?: 25565 }
			return GameActionConnectionData(data[0], serverPort)
		}
	}

}
