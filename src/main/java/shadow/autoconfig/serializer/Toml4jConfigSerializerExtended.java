package shadow.autoconfig.serializer;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import shadow.cloth.clothconfig.com.moandjiezana.toml.TomlWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class Toml4jConfigSerializerExtended<T extends ConfigData> implements ConfigSerializer<T> {
    private Config definition;
    private Class<T> configClass;
    private shadow.cloth.clothconfig.com.moandjiezana.toml.TomlWriter tomlWriter;

    public Toml4jConfigSerializerExtended(Config definition, Class<T> configClass, shadow.cloth.clothconfig.com.moandjiezana.toml.TomlWriter tomlWriter) {
        this.definition = definition;
        this.configClass = configClass;
        this.tomlWriter = tomlWriter;
    }

    public Toml4jConfigSerializerExtended(Config definition, Class<T> configClass) {
        this(definition, configClass, new TomlWriter());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(this.definition.name() + ".toml");
    }

    public void serialize(T config) throws SerializationException {
        Path configPath = this.getConfigPath();

        try {
            Files.createDirectories(configPath.getParent());
            this.tomlWriter.write(config, configPath.toFile());
        } catch (IOException var4) {
            throw new SerializationException(var4);
        }
    }

    public T deserialize() throws SerializationException {
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath, new LinkOption[0])) {
            try {
                return (T) (new Toml()).read(configPath.toFile()).to(this.configClass);
            } catch (IllegalStateException var3) {
                throw new SerializationException(var3);
            }
        } else {
            return this.createDefault();
        }
    }

    public T createDefault() {
        return (T)Utils.constructUnsafely(this.configClass);
    }
}
