package twilightforest.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Fabric-native replacement for Beanification DI.
 * Each @Component class registers itself and gets wired manually.
 */
public class TFBeanRegistry {
	private static final Map<Class<?>, Object> BEANS = new LinkedHashMap<>();
	private static final List<Runnable> POST_INIT = new ArrayList<>();
	private static boolean initialized = false;

	public static synchronized <T> void register(Class<T> clazz, T instance) {
		BEANS.put(clazz, instance);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		return (T) BEANS.get(clazz);
	}

	public static void addPostInit(Runnable runnable) {
		if (initialized) {
			runnable.run();
		} else {
			POST_INIT.add(runnable);
		}
	}

	public static void runPostInit() {
		initialized = true;
		for (Runnable r : POST_INIT) {
			r.run();
		}
		POST_INIT.clear();
	}
}