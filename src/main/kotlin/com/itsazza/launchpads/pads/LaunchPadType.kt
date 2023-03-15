package com.itsazza.launchpads.pads

import com.itsazza.launchpads.LaunchPads
import com.itsazza.launchpads.cache.LaunchCache
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.ceil

enum class LaunchPadType(private val dataFormat: String, private val dataSize: Int = dataFormat.split(",").size) {
    REGULAR("<x>,<y>,<z>"),
    LOOK("<power>,<offset>");

    private val instance = LaunchPads.instance

    fun launch(player: Player, data: List<String>): Boolean {
        // check data size
        if (data.size != dataSize) {
            player.sendMessage("§cInvalid data in line 3. Expected $dataFormat")
            return false
        }

        // generate vector
        val vector: Vector = when (this) {
            REGULAR -> Vector(data[0].toDouble(), data[1].toDouble(), data[2].toDouble())
            LOOK -> player.location.direction.multiply(data[0].toDouble()).add(Vector(0.0, data[1].toDouble(), 0.0))
        }

        // launch player
        setVelocity(player, vector)
        putOnElytra(player)
        return true
    }

    private fun setVelocity(player: Player, velocity: Vector) {
        object : BukkitRunnable() {
            override fun run() {
                player.velocity = velocity
            }
        }.runTaskLater(instance, 1L)

        if (instance.config.getBoolean("falldamage.prevent") && player.gameMode != GameMode.CREATIVE) {
            LaunchCache.put(
                player.uniqueId,
                ceil(instance.config.getDouble("falldamage.multiplier") * velocity.y * 1000).toLong()
            )
        }
    }

    // Equip players with elytra
    private fun putOnElytra(player: Player){
        // check if player already has elytra
        if (player.inventory.chestplate?.type == Material.ELYTRA) return

        // create customed elytra
        val elytra = ItemStack(Material.ELYTRA)
        val meta = elytra.itemMeta
        meta?.addEnchant(Enchantment.VANISHING_CURSE, 1, true)
        meta?.addEnchant(Enchantment.BINDING_CURSE, 1, true)
        meta?.isUnbreakable = true
        elytra.itemMeta = meta

        player.inventory.chestplate = elytra
    }
}