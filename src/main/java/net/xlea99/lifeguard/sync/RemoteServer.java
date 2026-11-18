package net.xlea99.lifeguard.sync;

import java.util.List;
import net.xlea99.lifeguard.boot.Boot;
import net.xlea99.lifeguard.core.AutoRule;
import net.xlea99.lifeguard.core.Library;
import net.xlea99.lifeguard.core.ServerSnapshot;
import net.xlea99.lifeguard.core.Settings;
import net.xlea99.lifeguard.core.Side;

/**
 * Whether this client is following a remote server's entry settings right now, instead of its own. The menu reads it
 * to show server entries read-only.
 *
 * <p>Leaving restores the live state from before the join, not the file: the file may hold edits saved from the menu
 * that are waiting for a restart, and loading them now would apply them half-live.
 */
public final class RemoteServer {
    private static volatile Settings beforeJoin;

    private RemoteServer() {
    }

    public static boolean following() {
        return beforeJoin != null;
    }

    static synchronized void follow(ServerSnapshot snapshot) {
        if (beforeJoin == null)
            beforeJoin = Settings.copyOf(Boot.registry());
        List<String> problems = snapshot.applyTo(Boot.registry());
        for (String problem : problems)
            Library.LOGGER.warn("{}: {}", Library.NAME, problem);
        AutoRule.Setting setting = Boot.registry().rule().setting();
        String choice = setting == null ? "" : setting.key() + " " + Boot.registry().ruleChoice(Side.SERVER) + ", ";
        Library.LOGGER.info("{}: following the server's settings ({}{} server entries)", Library.NAME, choice,
            snapshot.entries().size());
    }

    static synchronized void stopFollowing() {
        if (beforeJoin == null)
            return;
        beforeJoin.applyTo(Boot.registry());
        Boot.registry().clearServerBroken();
        beforeJoin = null;
        Library.LOGGER.info("{}: back to this game's own settings", Library.NAME);
    }
}
