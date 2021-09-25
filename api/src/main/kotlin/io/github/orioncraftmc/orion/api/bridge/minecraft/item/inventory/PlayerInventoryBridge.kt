package io.github.orioncraftmc.orion.api.bridge.minecraft.item.inventory

import io.github.orioncraftmc.orion.api.bridge.minecraft.item.ItemStackBridge

/**
 * Immutable player inventory definition.
 */
interface PlayerInventoryBridge {
	val mainInventory: Array<ItemStackBridge>

	val armorInventory: Array<ItemStackBridge>

	val currentItem: Int

	val mouseHeldStack: ItemStackBridge?

	val hotbarSize: Int

	val currentHeldItem: ItemStackBridge?

	val firstEmptyStack: Int

	fun hasItem(id: Int): Boolean
}
