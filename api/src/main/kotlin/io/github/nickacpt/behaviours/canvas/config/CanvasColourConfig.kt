package io.github.nickacpt.behaviours.canvas.config

data class CanvasColourConfig<ColorType>(
	var elementBackground: CanvasColourStyle<ColorType>? = null,
	var elementBorder: CanvasColourStyle<ColorType>? = null,

	var selectionBackground: ColorType? = null,
	var selectionBorder: ColorType? = null,

	var resizeHandle: CanvasColourStyle<ColorType>? = null,
)
