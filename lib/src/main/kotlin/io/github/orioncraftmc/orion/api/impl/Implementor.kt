package io.github.orioncraftmc.orion.api.impl

import io.github.orioncraftmc.orion.api.render.Tezzellator
import io.github.orioncraftmc.orion.api.scheduler.Scheduler
import io.github.orioncraftmc.orion.api.meta.Platform
import io.github.orioncraftmc.orion.api.meta.Version

/**
 * Stores implementation details.
 */
class Implementor(
	val version: Version,
	val platform: Platform,
	val clientBrand: String,
	val scheduler: Scheduler,
	val tezzellator: Tezzellator
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
