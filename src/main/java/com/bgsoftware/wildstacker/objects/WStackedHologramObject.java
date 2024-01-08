package com.bgsoftware.wildstacker.objects;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;

import java.util.Collections;
import java.util.Random;

public abstract class WStackedHologramObject<T> extends WStackedObject<T> {

    protected Hologram hologram;

    protected WStackedHologramObject(T object, int stackAmount) {
        super(object, stackAmount);
    }

    public void removeHologram() {
        if (hologram != null) {
            hologram.delete();
            hologram = null;
        }
    }

    public void setHologramName(String name, boolean createIfNull) {
        if (hologram == null) {
            if (!createIfNull)
                return;
            hologram = DHAPI.createHologram("WS-"+new Random().nextInt(),getLocation().add(0.5, 1.5, 0.5),true, Collections.singletonList(name));
        }
        DHAPI.setHologramLines(hologram,Collections.singletonList(name));
    }

    public Hologram createHologram() {
        return DHAPI.createHologram("",getLocation().add(0.5, 1.5, 0.5),true);
    }

}
