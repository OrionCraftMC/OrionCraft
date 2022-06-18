/*
 * MIT License
 *
 * Copyright (c) 2021 OrionCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.orioncraftmc.orion.utils

import com.github.ajalt.colormath.model.RGBInt

object ColorConstants {

	val debugColor = RGBInt(255, 255, 255)

	val modLabelBackgroundColor = RGBInt(0, 0, 0, 111)

	val rectangleBorder = RGBInt(0, 0, 0, 255)

	val buttonBackground = RGBInt(0, 0, 0, 90)
	val buttonPressedBackground = RGBInt(255, 255, 255, 90)

	val modComponentBackground = RGBInt(255, 255, 255, 40)
	val modComponentBackgroundSelected = RGBInt(255, 255, 255, 100)
	val modComponentSelectionBorder = RGBInt(200, 200, 200)

}
