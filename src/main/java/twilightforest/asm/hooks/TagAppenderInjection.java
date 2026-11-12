package twilightforest.asm.hooks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unchecked")
public interface TagAppenderInjection<T> {
	private TagAppender<T> self() {
		return (TagAppender<T>) this;
	}

	default TagAppender<T> twilightforest$add(Block... blocks) {
		for (Block block : blocks) {
			self().add((ResourceKey<T>) BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
		}
		return self();
	}

	default TagAppender<T> twilightforest$add(Item... items) {
		for (Item item : items) {
			self().add((ResourceKey<T>) BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
		}
		return self();
	}

	default TagAppender<T> twilightforest$add(EntityType<?>... entityTypes) {
		for (EntityType<?> entityType : entityTypes) {
			self().add((ResourceKey<T>) BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).orElseThrow());
		}
		return self();
	}

	default TagAppender<T> twilightforest$remove(Item... items) {
		for (Item item : items) {
			self().remove((ResourceKey<T>) BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
		}
		return self();
	}
}
