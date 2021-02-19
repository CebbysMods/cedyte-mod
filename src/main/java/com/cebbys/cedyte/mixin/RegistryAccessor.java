package com.cebbys.cedyte.mixin;

import java.util.Map;

import com.google.common.base.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;

@Mixin(Registry.class)
public interface RegistryAccessor {

    @Accessor("DEFAULT_ENTRIES")
    @Mutable
    public static void setDefaultEntries( Map< Identifier, Supplier<?> > entries ) {
        throw new AssertionError();
    }
    
    @Accessor("DEFAULT_ENTRIES")
    public static Map< Identifier, Supplier<?> > getDefaultEntries() {
        throw new AssertionError();
    }
    
    @Accessor("ROOT")
    public static MutableRegistry<?> getRoot() {
        throw new AssertionError();
    }
}