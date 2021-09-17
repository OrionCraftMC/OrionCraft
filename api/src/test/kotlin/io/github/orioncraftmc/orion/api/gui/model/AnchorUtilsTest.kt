package io.github.orioncraftmc.orion.api.gui.model

import kotlin.test.Test
import kotlin.test.assertEquals

internal class AnchorUtilsTest {

	private val zero = Point(0.0, 0.0)
	private val size = Size(1.0, 1.0)
	private val parentSize = Size(3.0, 2.0)

    @Test
    fun `Top Anchor returns expected results`() {
		assertEquals(zero, AnchorUtils.computePosition(zero, size, Anchor.TOP_LEFT))
		assertEquals(Point(-0.5, 0.0), AnchorUtils.computePosition(zero, size, Anchor.TOP_MIDDLE))
		assertEquals(Point(-1.0, 0.0), AnchorUtils.computePosition(zero, size, Anchor.TOP_RIGHT))
    }

    @Test
    fun `Middle Anchor returns expected results`() {
		assertEquals(Point(0.0, -0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE_LEFT))
		assertEquals(Point(-0.5, -0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE))
		assertEquals(Point(-1.0, -0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE_RIGHT))
    }

    @Test
    fun `Bottom Anchor returns expected results`() {
		assertEquals(Point(0.0, -1.0), AnchorUtils.computePosition(zero, size, Anchor.BOTTOM_LEFT))
		assertEquals(Point(-0.5, -1.0), AnchorUtils.computePosition(zero, size, Anchor.BOTTOM_MIDDLE))
		assertEquals(Point(-1.0, -1.0), AnchorUtils.computePosition(zero, size, Anchor.BOTTOM_RIGHT))
    }

	@Test
	fun `Top Anchor can be used properly with containers`() {
		assertEquals(zero, AnchorUtils.computePosition(zero, size, Anchor.TOP_LEFT, parentSize))
		assertEquals(Point(1.0, 0.0), AnchorUtils.computePosition(zero, size, Anchor.TOP_MIDDLE, parentSize))
		assertEquals(Point(2.0, 0.0), AnchorUtils.computePosition(zero, size, Anchor.TOP_RIGHT, parentSize))
	}

	@Test
	fun `Middle Anchor can be used properly with containers`() {
		assertEquals(Point(0.0, 0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE_LEFT, parentSize))
		assertEquals(Point(1.0, 0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE, parentSize))
		assertEquals(Point(2.0, 0.5), AnchorUtils.computePosition(zero, size, Anchor.MIDDLE_RIGHT, parentSize))
	}

	@Test
	fun `Bottom Anchor can be used properly with containers`() {
		assertEquals(Point(0.0, 1.0), AnchorUtils.computePosition(zero, size, Anchor.BOTTOM_LEFT, parentSize))
		assertEquals(Point(1.0, 1.0), AnchorUtils.computePosition(zero, size, Anchor.BOTTOM_MIDDLE, parentSize))
		assertEquals(Point(2.0, 1.0), AnchorUtils.computePosition(zero, size, Anchor.BOTTOM_RIGHT, parentSize))
	}
}
