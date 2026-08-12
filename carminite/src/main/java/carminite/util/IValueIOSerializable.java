package carminite.util;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface IValueIOSerializable {
	void serialize(ValueOutput output);

	void deserialize(ValueInput input);
}