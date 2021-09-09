package io.github.orioncraftmc.orion.api.logging

object FallbackLoggerFactory: LoggerFactory {
	override fun create(name: String): Logger {
		return FallbackLogger
	}
}

object FallbackLogger: Logger {
	override fun name(): String {
		return "fallback"
	}

	override fun debug(str: String, vararg args: Any) {
		println("[debug] " + str.format(args))
	}

	override fun info(str: String, vararg args: Any) {
		println("[info] " + str.format(args))
	}

	override fun warn(str: String, vararg args: Any) {
		println("[warn] " + str.format(args))
	}

	override fun error(str: String, vararg args: Any) {
		println("[error] " + str.format(args))
	}

	override fun fatal(str: String, vararg args: Any) {
		println("[fatal] " + str.format(args))
	}
}
