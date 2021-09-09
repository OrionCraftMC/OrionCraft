package io.github.orioncraftmc.orion.api.scheduler

import io.github.orioncraftmc.orion.api.OrionApi

/**
 * Allows running tasks asynchronously
 */
// TODO
interface Scheduler {
	companion object {
		fun get() = OrionApi.scheduler()
	}

	fun queueOnMainThread(task: Runnable)
}

object FallbackScheduler: Scheduler {
	override fun queueOnMainThread(task: Runnable) {
		task.run()
	}
}
