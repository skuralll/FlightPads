package com.itsazza.launchpads

import com.itsazza.launchpads.cache.ArmorCache
import com.itsazza.launchpads.command.LaunchPadsCommand
import com.itsazza.launchpads.events.OnPlayerTakeDamage
import com.itsazza.launchpads.events.OnQuit
import com.itsazza.launchpads.events.OnSignPlace
import com.itsazza.launchpads.events.OnStep
import com.itsazza.launchpads.pads.LaunchPadStorage
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LaunchPads : JavaPlugin() {
    companion object {
        lateinit var instance: LaunchPads
            private set
    }

    override fun onEnable() {
        this.saveDefaultConfig()
        instance = this
        getCommand("flightpads")?.setExecutor(LaunchPadsCommand)
        Bukkit.getServer().pluginManager.registerEvents(OnSignPlace(), this)
        Bukkit.getServer().pluginManager.registerEvents(OnStep(), this)
        Bukkit.getServer().pluginManager.registerEvents(OnPlayerTakeDamage(), this)
        Bukkit.getServer().pluginManager.registerEvents(OnQuit(), this)

        // Load the saved launch pads
        LaunchPadStorage.load()
        Metrics(this, 10774)
    }

    override fun onDisable() {
        // recover all player's armor
        ArmorCache.getAll().map {(uuid, item) ->
            val player = Bukkit.getPlayer(uuid) ?: return@map
            player.inventory.chestplate = item
            ArmorCache.pop(uuid)
        }
    }

    fun reload() {
        reloadConfig()
        LaunchPadStorage.load()
    }
}