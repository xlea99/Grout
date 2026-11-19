package net.xlea99.grout.entry.am_slightly_sexier_rattlesnake;

/**
 * What a Rattlesnake's mind ({@link SnakeMind}) last told this client about it: how far up its head is, 0 to 1 (NaN until
 * told), and whether it's asleep. Kept on each snake by the Sleepy eyes entry ({@link AmSlightlySexierRattlesnake#SLEEP}),
 * which closes its eyes from it; the Zs and the slithering read it too. Behavior-side; client only.
 */
public final class SnakeSleep {
    public float head = Float.NaN;
    public boolean asleep;

    /** What every Rattlesnake is made to have, on a client running Sleepy eyes. */
    public interface Holder {
        SnakeSleep grout$sleep();
    }
}
