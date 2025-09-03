package dev.aari.ultratrim;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.EnumSet;
import java.util.Set;

public class TrimCommand implements CommandExecutor {

    private static final Set<Material> ARMOR_ITEMS = EnumSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (!player.hasPermission("ultratrim.use")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length != 3) {
            player.sendMessage("§cUsage: /trim <ore> <trim> <all/wearing>");
            return true;
        }

        TrimMaterial trimMaterial = parseTrimMaterial(args[0]);
        if (trimMaterial == null) {
            player.sendMessage("§cInvalid trim material: " + args[0]);
            return true;
        }

        TrimPattern trimPattern = parseTrimPattern(args[1]);
        if (trimPattern == null) {
            player.sendMessage("§cInvalid trim pattern: " + args[1]);
            return true;
        }

        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
        String mode = args[2].toLowerCase();

        switch (mode) {
            case "all" -> {
                int trimmed = trimAllArmor(player, armorTrim);
                player.sendMessage("§aTrimmed " + trimmed + " armor pieces!");
            }
            case "wearing" -> {
                int trimmed = trimWearingArmor(player, armorTrim);
                player.sendMessage("§aTrimmed " + trimmed + " armor pieces you're wearing!");
            }
            default -> {
                player.sendMessage("§cInvalid mode! Use 'all' or 'wearing'");
                return true;
            }
        }

        return true;
    }

    private int trimAllArmor(Player player, ArmorTrim trim) {
        int count = 0;
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && ARMOR_ITEMS.contains(item.getType())) {
                if (applyTrim(item, trim)) {
                    count++;
                }
            }
        }

        return count;
    }

    private int trimWearingArmor(Player player, ArmorTrim trim) {
        int count = 0;
        ItemStack[] armor = player.getInventory().getArmorContents();

        for (ItemStack item : armor) {
            if (item != null && ARMOR_ITEMS.contains(item.getType())) {
                if (applyTrim(item, trim)) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean applyTrim(ItemStack item, ArmorTrim trim) {
        if (!(item.getItemMeta() instanceof ArmorMeta meta)) {
            return false;
        }

        meta.setTrim(trim);
        item.setItemMeta(meta);
        return true;
    }

    private TrimMaterial parseTrimMaterial(String input) {
        return switch (input.toLowerCase()) {
            case "quartz" -> TrimMaterial.QUARTZ;
            case "iron" -> TrimMaterial.IRON;
            case "netherite" -> TrimMaterial.NETHERITE;
            case "redstone" -> TrimMaterial.REDSTONE;
            case "copper" -> TrimMaterial.COPPER;
            case "gold", "golden" -> TrimMaterial.GOLD;
            case "emerald" -> TrimMaterial.EMERALD;
            case "diamond" -> TrimMaterial.DIAMOND;
            case "lapis" -> TrimMaterial.LAPIS;
            case "amethyst" -> TrimMaterial.AMETHYST;
            default -> null;
        };
    }

    private TrimPattern parseTrimPattern(String input) {
        return switch (input.toLowerCase()) {
            case "sentry" -> TrimPattern.SENTRY;
            case "dune" -> TrimPattern.DUNE;
            case "coast" -> TrimPattern.COAST;
            case "wild" -> TrimPattern.WILD;
            case "ward" -> TrimPattern.WARD;
            case "eye" -> TrimPattern.EYE;
            case "vex" -> TrimPattern.VEX;
            case "tide" -> TrimPattern.TIDE;
            case "snout" -> TrimPattern.SNOUT;
            case "rib" -> TrimPattern.RIB;
            case "spire" -> TrimPattern.SPIRE;
            case "wayfinder" -> TrimPattern.WAYFINDER;
            case "shaper" -> TrimPattern.SHAPER;
            case "silence" -> TrimPattern.SILENCE;
            case "raiser" -> TrimPattern.RAISER;
            case "host" -> TrimPattern.HOST;
            case "flow" -> TrimPattern.FLOW;
            case "bolt" -> TrimPattern.BOLT;
            default -> null;
        };
    }
}
