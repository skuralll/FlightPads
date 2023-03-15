package com.itsazza.launchpads.events

import com.itsazza.launchpads.cache.ArmorCache
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class OnQuit : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        player.inventory.chestplate = ArmorCache.pop(player.uniqueId) // recover from cache
    }
}