package io.github.orioncraftmc.orion.api.impl

import io.github.orioncraftmc.orion.api.logging.FallbackLogger
import io.github.orioncraftmc.orion.api.logging.FallbackLoggerFactory
import io.github.orioncraftmc.orion.api.logging.LoggerFactory
import io.github.orioncraftmc.orion.api.render.Tezzellator
import io.github.orioncraftmc.orion.api.scheduler.Scheduler
import io.github.orioncraftmc.orion.api.meta.Platform
import io.github.orioncraftmc.orion.api.meta.Version
import io.github.orioncraftmc.orion.api.render.FallbackTezzellator
import io.github.orioncraftmc.orion.api.scheduler.FallbackScheduler

/**
 * Stores implementation details.
 */
class Implementor(
	val version: Version,
	val platform: Platform,
	val clientBrand: String,
	val scheduler: Scheduler,
	val tezzellator: Tezzellator,
	val loggerFactory: LoggerFactory
) {
	companion object {
		/**
		 * Represents an empty implementor.
		 */
		private val fallback = Implementor(
			Version.INVALID,
			Platform.INVALID,
			"fallback",
			FallbackScheduler,
			FallbackTezzellator,
			FallbackLoggerFactory
		)
		private var implementor: Implementor? = null

		fun get(): Implementor = implementor ?: fallback

		fun set(implementor: Implementor) {
			if (this.implementor != null) {
				throw IllegalStateException("Cannot reinitialize implementor")
			}

			this.implementor = implementor
		}
	}
}
