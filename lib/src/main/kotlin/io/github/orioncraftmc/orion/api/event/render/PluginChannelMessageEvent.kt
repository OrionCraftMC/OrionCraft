package io.github.orioncraftmc.orion.api.event.render

import io.github.orioncraftmc.orion.api.event.CancellableEvent
import java.io.DataInput

/**
 * Fired when a custom packet is sent from the server.
 *
 * This event should be cancelled if the data is handled.
 */
class PluginChannelMessageEvent(val channel: String, val data: DataInput): CancellableEvent()
