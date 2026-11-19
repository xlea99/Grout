package net.xlea99.grout.entry.am_slightly_sexier_gorilla;

/**
 * What both Gorilla behaviors (the troop's march and the silverbacks' rise and fights) read off Alex's Gorilla, from one
 * shared mixin ({@code mixin.shared.gorilla_state}) instead of each its own copy: two copies of one method on one class are
 * a "Method overwrite conflict" at every launch. Behavior-side.
 */
public interface GorillaState {
    boolean grout$isSilverback();

    boolean grout$forcedSit();
}
