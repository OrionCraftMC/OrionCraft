package io.github.orioncraftmc.orion.api.logging

/**
 * Fancy logging go brr.
 */
interface Logger {
	fun name(): String

	fun debug(str: String, vararg args: Any)

	fun info(str: String, vararg args: Any)

	fun warn(str: String, vararg args: Any)

	fun error(str: String, vararg args: Any)

	fun fatal(str: String, vararg args: Any)
}
