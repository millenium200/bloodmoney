package net.thereturningvoid.bloodmoney;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.thereturningvoid.bloodmoney.listeners.PlayerDeathListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BloodMoney extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    public static final String VERSION = "v1.2.0";

    public static String CHAT_PREFIX = "UNDEFINED BLOODMONEY PREFIX! THIS IS A PROBLEM! REPORT TO SERVER STAFF AS SOON AS POSSIBLE!";
    public Permission permission;
    public Economy economy;
    public BloodMoney instance = null;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        CHAT_PREFIX = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("bloodmoney.customPrefix") + " " + ChatColor.RESET);
        if (!registerEconomy()) {
            log.info(ChatColor.stripColor(CHAT_PREFIX) + "Vault not installed on this server! Disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerPermissions();
        registerListeners();
        log.info("[BloodMoney] Enabled BloodMoney " + VERSION);
    }

    @Override
    public void onDisable() {
        instance = null;
        log.info("[BloodMoney] Disabled BloodMoney " + VERSION);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bmreloadconfig")) {
            if (permission.has(sender, "bloodmoney.admin")) {
                this.reloadConfig();
                sender.sendMessage(CHAT_PREFIX + "Config reloaded!");
                return true;
            } else {
                sender.sendMessage(CHAT_PREFIX + "You don't have permission to use this command!");
                return true;
            }
        }
        return false;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
    }

    private boolean registerPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        permission = permissionProvider.getProvider();
        return permission != null;
    }

    private boolean registerEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) return false;
        economy = economyProvider.getProvider();
        return economy != null;
    }

}
