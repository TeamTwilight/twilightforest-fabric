package twilightforest.compat;

import net.fabricmc.loader.api.FabricLoader;
import twilightforest.TwilightForestMod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//I was having an issue where the game refused to load with the enum method and I couldnt figure it out, so I moved to a new method of registering compat
//It works the same as it used to, but all the content for each mod should be in its own class.
public abstract class TFCompat {

    public static final String TRINKETS_ID = "trinkets";
    public static final String IE_ID = "immersiveengineering";
    public static final String TCON_ID = "tconstruct";
    public static final String UNDERGARDEN_ID = "undergarden";

    public static HashMap<String, Class<? extends TFCompat>> classes = new HashMap<>();
    public static Set<TFCompat> modules = new HashSet<>();

    static {
        classes.put(TRINKETS_ID, TrinketsCompat.class);
        //classes.put(IE_ID, IECompat.class);
        //classes.put(TCON_ID, TConCompat.class);
        //classes.put(UNDERGARDEN_ID, UndergardenCompat.class);
    }

    protected TFCompat(String modName) {
        this.modName = modName;
    }

    public static void initCompat() {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.init();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in init!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }


    public static void postInitCompat() {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.postInit();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in postInit!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void sendIMCs() {
        for (TFCompat compat : modules) {
            if (compat.isActivated) {
                try {
                    compat.handleIMCs();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in sending IMCs!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    protected abstract void init();

    protected abstract void postInit();

    protected abstract void handleIMCs();

    public final String modName;

    private boolean isActivated = false;
}
