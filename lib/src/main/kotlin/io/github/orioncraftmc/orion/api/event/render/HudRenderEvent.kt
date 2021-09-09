package io.github.orioncraftmc.orion.api.event.render

import io.github.orioncraftmc.orion.api.event.Event

/**
 * Called every frame.
 */
class HudRenderEvent(val tickDelta: Float) : Event {
	override val cancellable: Boolean
		get() = false
}
