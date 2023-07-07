package icu.lama.minecraft.chatbridge.debug.impl;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyWorld;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.ProxyWorldType;

public class ExampleProxyWorld implements IProxyWorld {
    @Override
    public Object unwrap() {
        return this;
    }

    @Override
    public ProxyWorldType getType() {
        return ProxyWorldType.OVERWORLD;
    }

    @Override
    public String getName() {
        return "ExampleWorld";
    }

    @Override
    public void save() {
        System.out.println("WorldSave >> Triggered");
    }
}
