package com.itsazza.launchpads.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.TimeUnit

object ArmorCache {
    private val launches: Cache<UUID, ItemStack> = CacheBuilder.newBuilder()
        .maximumSize(500)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build()

    fun put(uuid: UUID, item: ItemStack) {
        launches.put(uuid, item)
    }

    fun pop(uuid: UUID): ItemStack {
        val item = launches.getIfPresent(uuid) ?: return ItemStack(Material.AIR)
        launches.invalidate(uuid)
        return item
    }

    fun getAll(): Map<UUID, ItemStack> {
        return launches.asMap()
    }
}