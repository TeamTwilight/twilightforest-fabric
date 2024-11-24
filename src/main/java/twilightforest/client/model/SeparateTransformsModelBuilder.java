/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package twilightforest.client.model;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.CustomLoaderBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.TwilightForestMod;

import java.util.LinkedHashMap;
import java.util.Map;

public class SeparateTransformsModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T>
{
	private static final ResourceLocation NAME = new ResourceLocation(TwilightForestMod.ID, "separate_transforms");

	public static <T extends ModelBuilder<T>> SeparateTransformsModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper)
	{
		return new SeparateTransformsModelBuilder<>(parent, existingFileHelper);
	}

	private T base;
	private final Map<String, T> childModels = new LinkedHashMap<>();

	protected SeparateTransformsModelBuilder(T parent, ExistingFileHelper existingFileHelper)
	{
		super(NAME, parent, existingFileHelper);
	}

	public SeparateTransformsModelBuilder<T> base(T modelBuilder)
	{
		Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
		base = modelBuilder;
		return this;
	}

	public SeparateTransformsModelBuilder<T> perspective(ItemDisplayContext perspective, T modelBuilder)
	{
		Preconditions.checkNotNull(perspective, "layer must not be null");
		Preconditions.checkNotNull(modelBuilder, "modelBuilder must not be null");
		childModels.put(perspective.getSerializedName(), modelBuilder);
		return this;
	}

	@Override
	public JsonObject toJson(JsonObject json)
	{
		json = super.toJson(json);

		if (base != null)
		{
			json.add("base", base.toJson());
		}

		JsonObject parts = new JsonObject();
		for(Map.Entry<String, T> entry : childModels.entrySet())
		{
			parts.add(entry.getKey(), entry.getValue().toJson());
		}
		json.add("perspectives", parts);

		return json;
	}
}