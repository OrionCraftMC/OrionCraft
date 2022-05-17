package io.github.orioncraftmc.orion.api.event.impl

import io.github.orioncraftmc.orion.api.bridge.ServerDataBridge
import io.github.orioncraftmc.orion.api.event.Event

data class ServerPingFinishedEvent(val data: ServerDataBridge): Event
