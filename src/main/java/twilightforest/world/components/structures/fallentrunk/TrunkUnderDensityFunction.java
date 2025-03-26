package twilightforest.world.components.structures.fallentrunk;

import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import twilightforest.world.components.chunkgenerators.TanhHillFunction;

public class TrunkUnderDensityFunction extends Beardifier {
	private final boolean isBigTree;
	private final boolean isXOriented;
	private final RandomSource random;  // used to create dirt mounds
	private final BoundingBox boundingBox;
	private final TanhHillFunction[] tanhHillFunctions;
	protected final BoundingBox moundBase;
	protected static final float MOUND_RADIUS = 2.5F;
	protected static final int MAX_RANDOM_RADIUS_INCREASE_BIG_TREE = 4;
	protected static final int MAX_RANDOM_RADIUS_INCREASE_NON_BIG_TREE = 2;
	protected static final float BIG_TREE_MOUND_HEIGHT = 3F;
	protected static final float NON_BIG_TREE_MOUND_HEIGHT = 1.5F;

	public TrunkUnderDensityFunction(ObjectListIterator<Rigid> pieceIterator, FallenTrunkPiece piece, boolean isBigTree, int minMounds, int maxMounds) {
		super(pieceIterator, (ObjectListIterator<JigsawJunction>) ObjectIterators.<JigsawJunction>emptyIterator());
		this.isBigTree = isBigTree;
		boundingBox = getFallenTrunkPiece().box();
		random = RandomSource.create(boundingBox.minX() * 14413411L + boundingBox.minZ() * 43387781L);
		isXOriented = boundingBox.maxX() - boundingBox.minX() > boundingBox.maxZ() - boundingBox.minZ();
		int length = isXOriented ? boundingBox.getXSpan() : boundingBox.getZSpan();
		Vec3i moundBaseCorner = new Vec3i(
			isXOriented ?  length / 2 : 0,
			1,
			!isXOriented ? length / 2 : 0);
		this.moundBase = BoundingBox.fromCorners(moundBaseCorner, moundBaseCorner);
		this.tanhHillFunctions = new TanhHillFunction[random.nextInt(minMounds, maxMounds + 1)];
		for(int i = 0; i < tanhHillFunctions.length; i++) {
			tanhHillFunctions[i] = getTanhHillFunctionWithoutCoveringHole(piece, i % 2 == 0);
		}
	}

	@Override
	public double compute(FunctionContext context) {
		int x = context.blockX();
		int y = context.blockY();
		int z = context.blockZ();

		int groundLevelDelta = getFallenTrunkPiece().groundLevelDelta();
		int horizontalDistanceX = Math.max(0, Math.max(boundingBox.minX() - x, x - boundingBox.maxX()));
		int horizontalDistanceZ = Math.max(0, Math.max(boundingBox.minZ() - z, z - boundingBox.maxZ()));
		int adjustedGroundLevel = boundingBox.minY() + groundLevelDelta + (isBigTree ? 1 : 0);
		int verticalDistance = y - adjustedGroundLevel;

		double moundContribution = computeMoundsContribution(context);
		if (moundContribution > 0)
			return moundContribution;
		return flatSurroundingTerrain(horizontalDistanceX, verticalDistance, horizontalDistanceZ);
	}

	public static double flatSurroundingTerrain(double x, double y, double z) {
		if (y > 20 || y < -10)
			return 0;
		if (x == 0 && z == 0 && y > 0)
			return -1;  // flat everything inside the trunk
		final double a = 12.0, c = 12.0;
		final double verticalScale = 0.7;
		double normX = Math.abs(x / a);
		double normZ = Math.abs(z / c);
		double ellipseValue = normX * normX + normZ * normZ;

		if (ellipseValue >= 1.0)
			return 0.0;

		ellipseValue = Math.pow(ellipseValue, 1 / 8D);

		double factor = 1.0 - ellipseValue;
		if (y < 0)  // make less steep to avoid vertical walls around the trunk
			y = Math.tanh(Math.pow(ellipseValue, 4) * 0.5 + y / 5);  // add ellipseValue to avoid axis aligned "stairs"
		// divide by exponent to make the beardifier weaker with distance
		return -y * factor / verticalScale / (Math.exp(ellipseValue));
	}

	protected double computeMoundsContribution(FunctionContext context) {
		int x = context.blockX() - boundingBox.minX();
		int y = context.blockY() - boundingBox.minY();
		int z = context.blockZ() - boundingBox.minZ();
		int radius = getRadius(boundingBox);
		double ax = Math.abs((isXOriented ? z : x) - radius + 1);
		double az = Math.abs(y - radius + 1);
		if (radius == 2D) {  // This case is generated differently
			if (Math.abs((isXOriented ? z : x) - 1.5) + Math.abs(y - 1.5) <= 2)
				return -1;
		} else {
			if ((int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5)) < radius)
				return -1;
		}

		double max = -1D;
		for (TanhHillFunction h : tanhHillFunctions) {
			double value = h.compute(context);
			if (value > max)
				max = value;
		}
		return max;
	}

	protected TanhHillFunction getTanhHillFunctionWithoutCoveringHole(FallenTrunkPiece piece, boolean isOnRightSide) {
		if (isBigTree)
			return getTanhHillFunction((BlockPos) Util.getRandom(piece.getAllPotentialBaseMoundBlockPos(isOnRightSide).toArray(), random), isOnRightSide);  // Big trees don't have holes

		BlockPos moundBLockPos = (BlockPos) Util.getRandom(piece.getAllowedOrPotentialBaseMoundBlockPos(isOnRightSide).toArray(), random);
		return getTanhHillFunction(moundBLockPos, isOnRightSide);
	}

	protected TanhHillFunction getTanhHillFunction(BlockPos baseBlockPos, boolean isOnRightSide) {
		int maxRandomRadiusIncrease = isBigTree ? MAX_RANDOM_RADIUS_INCREASE_BIG_TREE : MAX_RANDOM_RADIUS_INCREASE_NON_BIG_TREE;
		float moundRadius = MOUND_RADIUS + random.nextInt(0, maxRandomRadiusIncrease);
		float moundHeight = isBigTree ? BIG_TREE_MOUND_HEIGHT : NON_BIG_TREE_MOUND_HEIGHT;

		float moundBiasAngle = Mth.TWO_PI * random.nextFloat();
		return new TanhHillFunction(baseBlockPos.getX(), baseBlockPos.getY(), baseBlockPos.getZ(), moundRadius, moundHeight, moundBiasAngle, isXOriented, isOnRightSide);
	}


	protected Beardifier.Rigid getFallenTrunkPiece() {
		Beardifier.Rigid piece = pieceIterator.next();
		this.pieceIterator.back(Integer.MAX_VALUE);
		return piece;
	}

	private static int getRadius(BoundingBox box) {
		return getRadius(box.getYSpan());
	}

	private static int getRadius(int diameter) {
		return (int) Math.ceil(diameter / 2D);
	}
}