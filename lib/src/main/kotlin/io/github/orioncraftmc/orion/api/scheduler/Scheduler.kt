package io.github.orioncraftmc.orion.api.scheduler

import io.github.orioncraftmc.orion.api.impl.Implementor

/**
 * Allows running tasks asynchronously
 */
interface Scheduler {
	companion object {
		fun get() = Implementor.get().scheduler
	}

	fun runOnMainThread(task: Runnable)
}
