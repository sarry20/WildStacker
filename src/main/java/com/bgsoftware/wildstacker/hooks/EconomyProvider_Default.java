package com.bgsoftware.wildstacker.hooks;

import org.bukkit.entity.Player;

public final class EconomyProvider_Default implements EconomyProvider {

    @Override
    public double getMoneyInBank(Player player, String variable) {
        return 0D;
    }

    @Override
    public void withdrawMoney(Player player,String variable, double amount) {

    }

}
