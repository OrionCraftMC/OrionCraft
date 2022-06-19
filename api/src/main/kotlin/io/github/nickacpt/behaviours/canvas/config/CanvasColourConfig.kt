package io.github.nickacpt.behaviours.canvas.config

data class CanvasColourConfig<ColorType>(
	var background: CanvasColourStyle<ColorType>? = null,
	var border: CanvasColourStyle<ColorType>? = null,
	var resizeHandle: CanvasColourStyle<ColorType>? = null,
)
