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

package io.github.orioncraftmc.orion.api.gui.model

import io.github.orioncraftmc.orion.utils.gui.AnchorUtils
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AnchorUtilsTest {

	private val zero = Point(0.0, 0.0)
	private val size = Size(1.0, 1.0)
	private val parentSize = Size(3.0, 2.0)

	@Test
	fun `Top Anchor returns expected results`() {
		assertEquals(zero, AnchorUtils.computePosition(zero, size, Anchor.TOP_LEFT))
		assertEquals(
			Point(-0.5, 0.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.TOP_MIDDLE
			)
		)
		assertEquals(
			Point(-1.0, 0.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.TOP_RIGHT
			)
		)
	}

	@Test
	fun `Middle Anchor returns expected results`() {
		assertEquals(
			Point(0.0, -0.5), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.MIDDLE_LEFT
			)
		)
		assertEquals(Point(-0.5, -0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE))
		assertEquals(
			Point(-1.0, -0.5), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.MIDDLE_RIGHT
			)
		)
	}

	@Test
	fun `Bottom Anchor returns expected results`() {
		assertEquals(
			Point(0.0, -1.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.BOTTOM_LEFT
			)
		)
		assertEquals(
			Point(-0.5, -1.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.BOTTOM_MIDDLE
			)
		)
		assertEquals(
			Point(-1.0, -1.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.BOTTOM_RIGHT
			)
		)
	}

	@Test
	fun `Top Anchor can be used properly with containers`() {
		assertEquals(zero, AnchorUtils.computePosition(zero, size, Anchor.TOP_LEFT, parentSize))
		assertEquals(
			Point(1.0, 0.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.TOP_MIDDLE,
				parentSize
			)
		)
		assertEquals(
			Point(2.0, 0.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.TOP_RIGHT,
				parentSize
			)
		)
	}

	@Test
	fun `Middle Anchor can be used properly with containers`() {
		assertEquals(
			Point(0.0, 0.5), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.MIDDLE_LEFT,
				parentSize
			)
		)
		assertEquals(
			Point(1.0, 0.5), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.MIDDLE,
				parentSize
			)
		)
		assertEquals(
			Point(2.0, 0.5), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.MIDDLE_RIGHT,
				parentSize
			)
		)
	}

	@Test
	fun `Bottom Anchor can be used properly with containers`() {
		assertEquals(
			Point(0.0, 1.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.BOTTOM_LEFT,
				parentSize
			)
		)
		assertEquals(
			Point(1.0, 1.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.BOTTOM_MIDDLE,
				parentSize
			)
		)
		assertEquals(
			Point(2.0, 1.0), AnchorUtils.computePosition(
				zero,
				size,
				Anchor.BOTTOM_RIGHT,
				parentSize
			)
		)
	}
}
