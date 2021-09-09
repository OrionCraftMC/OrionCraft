package io.github.orioncraftmc.orion.api.logging

import io.github.orioncraftmc.orion.api.OrionApi

// TODO
/**
 * Factory for create [Logger]s.
 */
interface LoggerFactory {
	fun create(name: String): Logger

	companion object {
		fun create(name: String): Logger {
			return get().create(name)
		}

		fun get(): LoggerFactory {
			return OrionApi.loggerFactory()
		}
	}
}
