package twilightforest.compat.jade;

import java.lang.reflect.InvocationTargetException;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import twilightforest.block.DryingRackBlock;

@WailaPlugin
public class JadeCommonPlugin implements IWailaPlugin {
	private static final String CLIENT_PLUGIN = "twilightforest.compat.jade.JadeCompat";

	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(DryingRackServerDataProvider.INSTANCE, DryingRackBlock.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		try {
			Class<?> pluginClass = Class.forName(CLIENT_PLUGIN);
			Object plugin = pluginClass.getDeclaredConstructor().newInstance();
			pluginClass.getMethod("registerClient", IWailaClientRegistration.class).invoke(plugin, registration);
		} catch (ClassNotFoundException ignored) {
			// The server classpath intentionally cannot load client-only Jade tooltip providers.
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
			throw new IllegalStateException("Unable to create client Jade plugin", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Client Jade plugin failed to register", e.getCause());
		}
	}
}
