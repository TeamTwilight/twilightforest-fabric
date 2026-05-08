package twilightforest.util.multiparts;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.TFPart;

import java.util.Iterator;

public class MultipartEntityIteratorWrapper implements Iterator<Entity> {

    private final Iterator<Entity> delegate;
    private TFPart<?> @Nullable [] parts;
    private int partIndex;

    MultipartEntityIteratorWrapper(Iterator<Entity> iter) {
        this.delegate = iter;
    }

    @Override
    public boolean hasNext() {
        return this.parts != null || this.delegate.hasNext();
    }

    @Override
    public Entity next() {
        if (this.parts != null) {
            Entity next = this.parts[this.partIndex];
            this.partIndex++;
            if (this.partIndex >= this.parts.length) {
                this.parts = null;
            }
            return next;
        }
        Entity next = this.delegate.next();
        if (next instanceof TFPart.Owner owner) {
            TFPart<?>[] arr = owner.getParts();
            if (arr != null && arr.length > 0) {
                this.partIndex = 0;
                this.parts = arr;
            }
        }
        return next;
    }

    @Override
    public void remove() {
        if (this.parts == null || this.partIndex <= 0) {
            this.delegate.remove();
        } else if (this.partIndex >= this.parts.length) {
            this.parts = null;
        } else {
            System.arraycopy(this.parts, this.partIndex, this.parts, this.partIndex - 1, this.parts.length - this.partIndex);
        }
    }
}
