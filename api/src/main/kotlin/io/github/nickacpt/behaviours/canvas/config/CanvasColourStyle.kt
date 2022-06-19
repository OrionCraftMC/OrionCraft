package io.github.nickacpt.behaviours.canvas.config

data class CanvasColourStyle<ColorType>(
	var normal: ColorType,
	var hover: ColorType,
	var mouseDown: ColorType = hover,
)
