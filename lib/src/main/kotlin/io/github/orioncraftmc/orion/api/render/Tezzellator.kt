package io.github.orioncraftmc.orion.api.render

interface Tezzellator {
	val buffer: BufferBuilder

	fun draw()
}
