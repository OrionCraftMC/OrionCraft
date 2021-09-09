package io.github.orioncraftmc.orion.api.impl

import io.github.orioncraftmc.orion.api.scheduler.Scheduler
import io.github.orioncraftmc.orion.api.version.Platform
import io.github.orioncraftmc.orion.api.version.Version

/**
 * Stores implementation details.
 */
class Implementor(
	val version: Version,
	val platform: Platform,
	val scheduler: Scheduler,
) {
	companion object {
		private var implementor: Implementor? = null

		fun get() = implementor!!

		fun set(implementor: Implementor) {
			if (this.implementor != null) {
				throw IllegalStateException("Cannot reinitialize implementor")
			}

			this.implementor = implementor
		}
	}
}
