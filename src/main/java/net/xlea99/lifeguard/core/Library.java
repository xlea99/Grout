package net.xlea99.lifeguard.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The shared machinery's own identity, as opposed to any one mod's ({@link ModSpec}): the id its network channels and
 * its data condition live under, the name it speaks in, and its logger. Everything here belongs to whoever ships the
 * machinery. Today that's Lifeguard; at the Great Split these become GuppyLib's (this file is the one place to change).
 */
public final class Library {
    /** Its namespace: network channels ({@code <ID>:server_entries}) and the data condition ({@code <ID>:entry}). */
    public static final String ID = "lifeguard";
    /** The name it speaks in, at the start of its log lines and messages. */
    public static final String NAME = "Lifeguard";
    /**
     * Its logger. Named after {@link #NAME}, so its lines read {@code [Lifeguard/]}; the workspace's launch checks look
     * for that and for the wording of the preflight and mixin lines.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private Library() {
    }
}
