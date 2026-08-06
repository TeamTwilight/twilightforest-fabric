package twilightforest.data.custom;

/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.VisibleForTesting;


// Copied from NeoForge API
public abstract class ParticleDescriptionProvider implements DataProvider {
	private final PackOutput.PathProvider particlesPath;
	@VisibleForTesting
	protected final ExistingFileHelper fileHelper;
	@VisibleForTesting
	protected final Map<ResourceLocation, List<String>> descriptions;

	protected ParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
		this.particlesPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "particles");
		this.fileHelper = fileHelper;
		this.descriptions = new HashMap<>();
	}

	protected abstract void addDescriptions();

	protected void sprite(ParticleType<?> type, ResourceLocation texture) {
		this.spriteSet(type, texture);
	}

	protected void spriteSet(ParticleType<?> type, ResourceLocation baseName, int numOfTextures, boolean reverse) {
		Preconditions.checkArgument(numOfTextures > 0, "The number of textures to generate must be positive");
		this.spriteSet(type, () -> new Iterator<>() {
			private int counter = 0;

			@Override
			public boolean hasNext() {
				return this.counter < numOfTextures;
			}

			@Override
			public ResourceLocation next() {
				var texture = baseName.withSuffix("_" + (reverse ? numOfTextures - this.counter - 1 : this.counter));
				this.counter++;
				return texture;
			}
		});
	}

	protected void spriteSet(ParticleType<?> type, ResourceLocation texture, ResourceLocation... textures) {
		this.spriteSet(type, Stream.concat(Stream.of(texture), Arrays.stream(textures))::iterator);
	}

	protected void spriteSet(ParticleType<?> type, Iterable<ResourceLocation> textures) {
		// Make sure particle type is registered
		var particle = Preconditions.checkNotNull(BuiltInRegistries.PARTICLE_TYPE.getKey(type), "The particle type is not registered");

		// Validate textures
		List<String> desc = new ArrayList<>();
		for (var texture : textures) {
			Preconditions.checkArgument(this.fileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures/particle"),
				"Texture '%s' does not exist in any known resource pack", texture);
			desc.add(texture.toString());
		}
		Preconditions.checkArgument(!desc.isEmpty(), "The particle type '%s' must have one texture", particle);

		// Insert into map
		if (this.descriptions.putIfAbsent(particle, desc) != null)
			throw new IllegalArgumentException(String.format("The particle type '%s' already has a description associated with it", particle));
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		this.addDescriptions();

		return CompletableFuture.allOf(
			this.descriptions.entrySet().stream().map(entry -> {
				// Map entries to the description format
				var textures = new JsonArray();
				entry.getValue().forEach(textures::add);
				return DataProvider.saveStable(cache,
					Util.make(new JsonObject(), obj -> obj.add("textures", textures)),
					this.particlesPath.json(entry.getKey()));
			}).toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Particle Descriptions";
	}
}
