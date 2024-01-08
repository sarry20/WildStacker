package com.bgsoftware.wildstacker.hooks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import svar.ajneb97.api.ServerVariablesAPI;
import svar.ajneb97.model.ServerVariablesVariable;
import svar.ajneb97.model.structure.Variable;

public class ServerVariables implements EconomyProvider {

    private static ServerVariablesAPI eco;

    public ServerVariables(){
        eco = new ServerVariablesAPI((svar.ajneb97.ServerVariables) Bukkit.getServer().getPluginManager().getPlugin("ServerVariables"));
    }

    public static boolean isCompatible() {
        return Bukkit.getServer().getPluginManager().getPlugin("ServerVariables") != null;
    }

    @Override
    public double getMoneyInBank(Player player, String variable) {
        return Double.parseDouble(eco.getPlayerByUUID(player.getUniqueId().toString()).getVariable(variable).getCurrentValue());
    }

    @Override
    public void withdrawMoney(Player player, String variable, double amount) {
        ServerVariablesVariable var = eco.getPlayerByUUID(player.getUniqueId().toString()).getVariable(variable);
        var.setCurrentValue(String.valueOf(Double.parseDouble(var.getCurrentValue())-amount));
    }
}