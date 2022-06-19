package io.github.nickacpt.behaviours.canvas.config

data class CanvasConfig<ColorType>(
	val colors: CanvasColorConfig<ColorType> = CanvasColorConfig(),
	var borderWidth: Float = 1f,
	var safeZoneSize: Float = 4f,
	var safeZoneBorderWidth: Float = 1f,
)

