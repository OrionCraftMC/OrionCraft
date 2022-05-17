package io.github.orioncraftmc.orion.api.bridge

interface ServerDataBridge {
	var name: String
	var ipAddress: String
	var playerCount: String
	var motd: String
}
