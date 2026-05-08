package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class GiantPickaxeMiningAttachment {
	public static final Codec<GiantPickaxeMiningAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.optionalFieldOf("mining", 0L).forGetter(GiantPickaxeMiningAttachment::getMining),
		Codec.BOOL.optionalFieldOf("breaking", false).forGetter(GiantPickaxeMiningAttachment::getBreaking),
		Codec.INT.optionalFieldOf("giant_block_conversion", 0).forGetter(GiantPickaxeMiningAttachment::getGiantBlockConversion)
	).apply(instance, GiantPickaxeMiningAttachment::new));

	private long mining;
	private boolean breaking;
	private int giantBlockConversion;

	public GiantPickaxeMiningAttachment() {
		this(0L, false, 0);
	}

	public GiantPickaxeMiningAttachment(long mining, boolean breaking, int giantBlockConversion) {
		this.mining = mining;
		this.breaking = breaking;
		this.giantBlockConversion = giantBlockConversion;
	}

	public void setMining(long mining) {
		this.mining = mining;
	}

	public long getMining() {
		return this.mining;
	}

	public void setBreaking(boolean breaking) {
		this.breaking = breaking;
	}

	public boolean getBreaking() {
		return this.breaking;
	}

	public void setGiantBlockConversion(int giantBlockConversion) {
		this.giantBlockConversion = giantBlockConversion;
	}

	public int getGiantBlockConversion() {
		return this.giantBlockConversion;
	}

	public boolean canMakeGiantBlock() {
		return this.giantBlockConversion > 0;
	}
}
