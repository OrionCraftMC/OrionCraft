package io.github.orioncraftmc.orion.api.logging

object FallbackLogger : Logger {
	override fun name(): String {
		return "fallback"
	}

	override fun debug(str: String, vararg args: Any) {
		println("[DEBUG] " + str.format(args))
	}

	override fun info(str: String, vararg args: Any) {
		println("[INFO] " + str.format(args))
	}

	override fun warn(str: String, vararg args: Any) {
		println("[WARN] " + str.format(args))
	}

	override fun error(str: String, vararg args: Any) {
		println("[ERROR] " + str.format(args))
	}

	override fun fatal(str: String, vararg args: Any) {
		println("[FATAL] " + str.format(args))
	}
}
