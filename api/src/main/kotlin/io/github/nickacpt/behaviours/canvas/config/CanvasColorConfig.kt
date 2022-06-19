package io.github.nickacpt.behaviours.canvas.config

data class CanvasColorConfig<ColorType>(
    var elementBackground: CanvasColorStyle<ColorType>? = null,
    var elementBorder: CanvasColorStyle<ColorType>? = null,

    var selectionBackground: ColorType? = null,
    var selectionBorder: ColorType? = null,
)
