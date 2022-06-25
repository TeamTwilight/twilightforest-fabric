package twilightforest.world.components.layer.vanillalegacy;

import twilightforest.world.components.layer.vanillalegacy.area.Area;
import twilightforest.world.components.layer.vanillalegacy.context.BigContext;
import twilightforest.world.components.layer.vanillalegacy.traits.AreaTransformer1;

public enum ZoomLayer implements AreaTransformer1 {
	NORMAL,
	FUZZY {
		protected int modeOrRandom(BigContext<?> p_76979_, int p_76980_, int p_76981_, int p_76982_, int p_76983_) {
			return p_76979_.random(p_76980_, p_76981_, p_76982_, p_76983_);
		}
	};

	private static final int ZOOM_BITS = 1;
	private static final int ZOOM_MASK = 1;

	public int getParentX(int p_76959_) {
		return p_76959_ >> 1;
	}

	public int getParentY(int p_76971_) {
		return p_76971_ >> 1;
	}

	public int applyPixel(BigContext<?> p_76966_, Area p_76967_, int p_76968_, int p_76969_) {
		int i = p_76967_.get(this.getParentX(p_76968_), this.getParentY(p_76969_));
		p_76966_.initRandom(p_76968_ >> 1 << 1, p_76969_ >> 1 << 1);
		int j = p_76968_ & 1;
		int k = p_76969_ & 1;
		if (j == 0 && k == 0) {
			return i;
		} else {
			int l = p_76967_.get(this.getParentX(p_76968_), this.getParentY(p_76969_ + 1));
			int i1 = p_76966_.random(i, l);
			if (j == 0 && k == 1) {
				return i1;
			} else {
				int j1 = p_76967_.get(this.getParentX(p_76968_ + 1), this.getParentY(p_76969_));
				int k1 = p_76966_.random(i, j1);
				if (j == 1 && k == 0) {
					return k1;
				} else {
					int l1 = p_76967_.get(this.getParentX(p_76968_ + 1), this.getParentY(p_76969_ + 1));
					return this.modeOrRandom(p_76966_, i, j1, l, l1);
				}
			}
		}
	}

	protected int modeOrRandom(BigContext<?> p_76960_, int p_76961_, int p_76962_, int p_76963_, int p_76964_) {
		if (p_76962_ == p_76963_ && p_76963_ == p_76964_) {
			return p_76962_;
		} else if (p_76961_ == p_76962_ && p_76961_ == p_76963_) {
			return p_76961_;
		} else if (p_76961_ == p_76962_ && p_76961_ == p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76963_ && p_76961_ == p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76962_ && p_76963_ != p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76963_ && p_76962_ != p_76964_) {
			return p_76961_;
		} else if (p_76961_ == p_76964_ && p_76962_ != p_76963_) {
			return p_76961_;
		} else if (p_76962_ == p_76963_ && p_76961_ != p_76964_) {
			return p_76962_;
		} else if (p_76962_ == p_76964_ && p_76961_ != p_76963_) {
			return p_76962_;
		} else {
			return p_76963_ == p_76964_ && p_76961_ != p_76962_ ? p_76963_ : p_76960_.random(p_76961_, p_76962_, p_76963_, p_76964_);
		}
	}
}
