package com.bgsoftware.wildstacker.hooks;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface EconomyProvider {

    double getMoneyInBank(Player player, @Nullable String variable);

    void withdrawMoney(Player player, @Nullable String variable, double amount);

}
