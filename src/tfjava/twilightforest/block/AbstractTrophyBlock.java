package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.block.entity.TrophyBlockEntity;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

/**
 * 1:1 port of upstream {@code twilightforest.block.AbstractTrophyBlock} (NeoForge → Fabric:
 * upstream batched particles into a single {@code ParticlePacket} sent via
 * {@code PacketDistributor.sendToPlayersNear}; vanilla Fabric 1.21.1 has no equivalent
 * batched-particle network primitive, so each particle is sent individually through
 * {@code ServerLevel.sendParticles(...)} which the server already broadcasts to nearby
 * tracked clients. Behaviour is otherwise byte-for-byte identical: per-variant sounds,
 * per-variant particle pattern, neighbour-signal POWERED toggle, ticker that drives
 * UR_GHAST_TROPHY animation through {@link TrophyBlockEntity}.).
 *
 * <p>Vanilla-copy of {@code AbstractSkullBlock} except uses {@link BossVariant} instead
 * of {@code ISkullType} and adds sounds when clicked or powered.</p>
 */
public abstract class AbstractTrophyBlock extends BaseEntityBlock implements Equipable {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	private final BossVariant variant;
	private final int comparatorValue;

	protected AbstractTrophyBlock(BossVariant variant, int value, BlockBehaviour.Properties builder) {
		super(builder);
		this.variant = variant;
		this.comparatorValue = value;
		this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
	}

	public int getComparatorValue() {
		return this.comparatorValue;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide()) {
			boolean flag = level.hasNeighborSignal(pos);
			if (flag != state.getValue(POWERED)) {
				if (flag) {
					this.playSound(level, pos);
				}
				level.setBlockAndUpdate(pos, state.setValue(POWERED, flag));
			}
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		this.playSound(level, pos);
		this.createParticle(level, pos);
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TrophyBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.TROPHY, TrophyBlockEntity::tick);
	}

	public BossVariant getVariant() {
		return this.variant;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public void playSound(Level level, BlockPos pos) {
		BlockEntity te = level.getBlockEntity(pos);
		if (!level.isClientSide() && te instanceof TrophyBlockEntity) {
			SoundEvent sound = null;
			float volume = 1.0F;
			float pitch = 0.9F;
			switch (this.variant) {
				case NAGA -> {
					sound = TFSounds.NAGA_RATTLE;
					volume = 1.25F;
					pitch = 1.2F;
				}
				case LICH -> {
					sound = TFSounds.LICH_AMBIENT;
					volume = 0.35F;
					pitch = 1.1F;
				}
				case HYDRA -> {
					sound = TFSounds.HYDRA_GROWL;
					pitch = 1.2F;
				}
				case UR_GHAST -> {
					sound = TFSounds.UR_GHAST_AMBIENT;
					pitch = 0.6F;
				}
				case SNOW_QUEEN -> sound = TFSounds.SNOW_QUEEN_AMBIENT;
				case KNIGHT_PHANTOM -> {
					sound = TFSounds.KNIGHT_PHANTOM_AMBIENT;
					pitch = 1.1F;
				}
				case MINOSHROOM -> {
					sound = TFSounds.MINOSHROOM_AMBIENT;
					volume = 0.75F;
					pitch = 0.7F;
				}
				case ALPHA_YETI -> {
					sound = level.getRandom().nextInt(50) == 0 ? TFSounds.ALPHA_YETI_ROAR : TFSounds.ALPHA_YETI_GROWL;
					volume = 0.75F;
					pitch = 0.75F;
				}
				case QUEST_RAM -> {
					sound = TFSounds.QUEST_RAM_AMBIENT;
					pitch = 0.7F;
				}
				default -> {
				}
			}
			if (sound != null) {
				level.playSound(null, pos, sound, SoundSource.BLOCKS, volume, level.getRandom().nextFloat() * 0.1F + pitch);
			}
		}
	}

	public void createParticle(Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof TrophyBlockEntity && level instanceof ServerLevel server) {
			RandomSource rand = level.getRandom();
			switch (this.variant) {
				case NAGA:
					for (int daze = 0; daze < 10; daze++) {
						server.sendParticles(ParticleTypes.CRIT,
							((double) pos.getX() + rand.nextFloat() * 0.5D * 2.0D),
							(double) pos.getY() + 0.25D,
							((double) pos.getZ() + rand.nextFloat() * 0.5D * 2.0D),
							1,
							rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D,
							0.0D);
					}
					break;
				case LICH:
					for (int a = 0; a < 5; a++) {
						server.sendParticles(TFParticleType.ANGRY_LICH,
							(double) pos.getX() + rand.nextFloat() * 0.5D * 2.0F + rand.nextGaussian() * 0.02D,
							(double) pos.getY() + 0.5D + rand.nextFloat() * 0.25 + rand.nextGaussian() * 0.02D,
							(double) pos.getZ() + rand.nextFloat() * 0.5D * 2.0F + rand.nextGaussian() * 0.02D,
							1, 0, 0, 0, 0.0D);
					}
					break;
				case MINOSHROOM:
					ParticleOptions minoshroomParticle = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos.below()));
					for (int g = 0; g < 10; g++) {
						server.sendParticles(minoshroomParticle,
							(double) pos.getX() + rand.nextFloat() * 10F - 5F,
							(double) pos.getY() + 0.1F + rand.nextFloat() * 0.3F,
							(double) pos.getZ() + rand.nextFloat() * 10F - 5F,
							1, 0, 0, 0, 0.0D);
					}
					break;
				case KNIGHT_PHANTOM:
					ParticleOptions knightPhantomParticle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(TFItems.KNIGHTMETAL_SWORD.get()));
					for (int brek = 0; brek < 10; brek++) {
						server.sendParticles(knightPhantomParticle,
							pos.getX() + 0.5D + (rand.nextFloat() - 0.5D),
							pos.getY() + rand.nextFloat() + 0.5D + 0.25D * rand.nextGaussian(),
							pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D),
							1, 0, 0, 0, 0.0D);
					}
					break;
				case UR_GHAST:
					for (int red = 0; red < 10; red++) {
						server.sendParticles(DustParticleOptions.REDSTONE,
							(double) pos.getX() + (rand.nextDouble() * 1),
							(double) pos.getY() + rand.nextDouble() * 0.5 + 0.5,
							(double) pos.getZ() + (rand.nextDouble() * 1),
							1, 0, 0, 0, 0.0D);
					}
					break;
				case ALPHA_YETI:
					for (int sweat = 0; sweat < 10; sweat++) {
						server.sendParticles(ParticleTypes.SPLASH,
							(double) pos.getX() + (rand.nextDouble() * 1),
							(double) pos.getY() + rand.nextDouble() * 0.5 + 0.5,
							(double) pos.getZ() + (rand.nextDouble() * 1),
							1, 0, 0, 0, 0.0D);
					}
					break;
				case SNOW_QUEEN:
					for (int b = 0; b < 20; b++) {
						server.sendParticles(TFParticleType.SNOW_WARNING,
							(double) pos.getX() - 1 + (rand.nextDouble() * 3.25D),
							(double) pos.getY() + 5 + rand.nextGaussian(),
							(double) pos.getZ() - 1 + (rand.nextDouble() * 3.25D),
							1, 0, 0, 0, 0.0D);
					}
					break;
				case QUEST_RAM:
					for (int p = 0; p < 10; p++) {
						server.sendParticles(ColorParticleOption.create(TFParticleType.MAGIC_EFFECT, rand.nextFloat(), rand.nextFloat(), rand.nextFloat()),
							(double) pos.getX() + 0.5 + (rand.nextDouble() - 0.5),
							(double) pos.getY() + (rand.nextDouble() - 0.5),
							(double) pos.getZ() + 0.5 + (rand.nextDouble() - 0.5),
							1,
							rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian(), 0.0D);
					}
					break;
				default:
					break;
			}
		}
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}
}
