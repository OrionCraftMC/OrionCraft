package io.github.orioncraftmc.orion.api.event.render

import io.github.orioncraftmc.orion.api.event.CancellableEvent

/**
 * Called every frame.
 */
class HudRenderEvent(val tickDelta: Float) : CancellableEvent()
