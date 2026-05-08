package twilightforest.entity.boss;

/**
 * 1:1 port of upstream {@code twilightforest.entity.boss.HydraSmallPart} —
 * trivial {@link HydraPart} subclass marker for the smaller Hydra body parts
 * (heads, neck segments). Functionally identical to HydraPart with its own
 * type so renderers/AI can dispatch on the subtype.
 */
public class HydraSmallPart extends HydraPart {

	public HydraSmallPart(Hydra hydra, float w, float h) {
		super(hydra, w, h);
	}
}
