package io.github.orioncraftmc.orion.api.render

interface Tezzellator {
	val buffer: BufferBuilder

	fun draw()
}

object FallbackTezzellator: Tezzellator {
	override val buffer: BufferBuilder
		get() = TODO("Cannot be implemented")

	override fun draw() {
	}
}
