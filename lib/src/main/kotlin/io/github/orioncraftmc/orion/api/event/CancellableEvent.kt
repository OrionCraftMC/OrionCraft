package io.github.orioncraftmc.orion.api.event

/**
 * Specifies an event that can be cancelled.
 */
abstract class CancellableEvent: Event {
	var cancelled = false
		private set

	final override val cancellable: Boolean
		get() = true

	fun cancel() = run { cancelled = true }
}
