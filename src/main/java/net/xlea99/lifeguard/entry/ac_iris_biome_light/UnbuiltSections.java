package net.xlea99.lifeguard.entry.ac_iris_biome_light;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import javax.annotation.Nullable;
import net.xlea99.lifeguard.boot.BrokenEntries;

/**
 * What {@code RenderSectionManagerMixin} needs of Sodium's sections and build queues, by name: Modrinth's Sodium keeps its code in
 * a nested jar, which Lifeguard can't compile against. Anything missing marks the entry broken.
 */
public final class UnbuiltSections {
    private static final String PACKAGE = "net.caffeinemc.mods.sodium.client.render.chunk.";
    @Nullable
    private static final MethodHandle IS_DISPOSED = method("isDisposed", boolean.class);
    @Nullable
    private static final MethodHandle IS_BUILT = method("isBuilt", boolean.class);
    @Nullable
    private static final MethodHandle PENDING_UPDATE = method("getPendingUpdate", int.class);
    @Nullable
    private static final Object DEFERRED_QUEUE = deferredQueue();

    private UnbuiltSections() {
    }

    /** The key of Sodium's queue for builds nothing is waiting on, or null if it's missing. */
    @Nullable
    public static Object deferredQueue() {
        try {
            return Enum.valueOf(Class.forName(PACKAGE + "TaskQueueType").asSubclass(Enum.class), "ALWAYS_DEFER");
        } catch (ReflectiveOperationException | RuntimeException e) {
            BrokenEntries.mark(AcIrisBiomeLight.ENTRY, "Sodium's TaskQueueType.ALWAYS_DEFER can't be found (" + e + ")");
            return null;
        }
    }

    public static Object queueKey() {
        return DEFERRED_QUEUE;
    }

    /** Whether this section has no mesh yet and is waiting for one. */
    public static boolean waiting(Object section) {
        if (IS_DISPOSED == null || IS_BUILT == null || PENDING_UPDATE == null)
            return false;
        try {
            return !(boolean) IS_DISPOSED.invokeExact(section) && !(boolean) IS_BUILT.invokeExact(section) && (int) PENDING_UPDATE.invokeExact(section) != 0;
        } catch (Throwable e) {
            throw new IllegalStateException("Sodium's RenderSection couldn't be read", e);
        }
    }

    @Nullable
    private static MethodHandle method(String name, Class<?> returns) {
        try {
            Class<?> section = Class.forName(PACKAGE + "RenderSection");
            return MethodHandles.publicLookup().findVirtual(section, name, MethodType.methodType(returns))
                .asType(MethodType.methodType(returns, Object.class));
        } catch (ReflectiveOperationException e) {
            BrokenEntries.mark(AcIrisBiomeLight.ENTRY, "Sodium's RenderSection." + name + " can't be found (" + e + ")");
            return null;
        }
    }
}
