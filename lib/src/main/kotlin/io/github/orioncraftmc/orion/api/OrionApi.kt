package io.github.orioncraftmc.orion.api

import io.github.orioncraftmc.orion.api.impl.Implementor

/**
 * Convenience methods to access different services provided by Orion.
 */
object OrionApi {
	fun version() = Implementor.get().version

	fun clientBrand() = Implementor.get().clientBrand

	fun platform() = Implementor.get().platform

	fun scheduler() = Implementor.get().scheduler

	fun tezzellator() = Implementor.get().tezzellator
}
