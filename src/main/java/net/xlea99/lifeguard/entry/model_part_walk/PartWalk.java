package net.xlea99.lifeguard.entry.model_part_walk;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.client.model.geom.ModelPart;

/**
 * Every part of a model tree, as {@code ModelPart.getAllParts} gives them (a part, then each child's parts in their map's
 * order, depth first), but by one walker with a small stack instead of a new stream pipeline at every part.
 *
 * <p>Vanilla's {@code Stream.concat(Stream.of(this), children.values().stream().flatMap(ModelPart::getAllParts))} is
 * lazy, and so is this: a part is only looked at when the caller asks for the next one, so a caller that stops early stops
 * the walk there. Like vanilla, a part's children map is taken just before the part is handed out and walked after it, read
 * live (EMF swaps a part's children at runtime).
 */
public final class PartWalk implements Spliterator<ModelPart> {
    private final Function<ModelPart, Map<String, ModelPart>> childrenOf;
    private final Deque<Iterator<ModelPart>> stack = new ArrayDeque<>();
    /** The children of the part last handed out, walked next. */
    private Map<String, ModelPart> pending;

    public PartWalk(ModelPart root, Function<ModelPart, Map<String, ModelPart>> childrenOf) {
        this.childrenOf = childrenOf;
        this.stack.push(List.of(root).iterator());
    }

    public static Stream<ModelPart> of(ModelPart root, Function<ModelPart, Map<String, ModelPart>> childrenOf) {
        return StreamSupport.stream(new PartWalk(root, childrenOf), false);
    }

    @Override
    public boolean tryAdvance(Consumer<? super ModelPart> action) {
        if (this.pending != null) {
            this.stack.push(this.pending.values().iterator());
            this.pending = null;
        }
        while (!this.stack.isEmpty()) {
            Iterator<ModelPart> level = this.stack.peek();
            if (level.hasNext()) {
                ModelPart part = level.next();
                this.pending = this.childrenOf.apply(part);
                action.accept(part);
                return true;
            }
            this.stack.pop();
        }
        return false;
    }

    @Override
    public Spliterator<ModelPart> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return ORDERED | NONNULL;
    }
}
