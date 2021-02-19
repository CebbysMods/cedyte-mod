package com.cebbys.cedyte.registry;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.registry.Registry;

public class CedyteRegistries {
	
	public static final Registry<Object> TREE_HANDLERS;
	
	static {
		TREE_HANDLERS = RegistryBuilder.createRegistry("tree_handlers", Lifecycle.stable());
	}
}
