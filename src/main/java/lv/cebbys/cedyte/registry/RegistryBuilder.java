package lv.cebbys.cedyte.registry;

import com.mojang.serialization.Lifecycle;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class RegistryBuilder {

    public static < T > Registry<T> createRegistry( String registryId, Lifecycle cycle ) {
        RegistryKey< Registry< T > > key = createRegistryKey( registryId );
        return new SimpleRegistry< T >( key, cycle );
    }

    public static <T> RegistryKey<Registry<T>> createRegistryKey( String registryId ) {
        return RegistryKey.ofRegistry( new Identifier( registryId ) );
    }
}
