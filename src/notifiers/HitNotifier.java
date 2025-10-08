package notifiers;

/**
 * The HitNotifier interface should be implemented by objects that can be hit
 * and want to notify registered {@link HitListener}s about hit events.
 * Classes implementing this interface maintain a list of hit listeners
 * and notify them whenever a hit occurs.
 */
public interface HitNotifier {

    /**
     * Adds the specified {@link HitListener} to the list of listeners
     * that will be notified of hit events.
     * @param hl the HitListener to add.
     */
    void addHitListener(HitListener hl);

    /**
     * Removes the specified {@link HitListener} from the list of listeners
     * so it will no longer be notified of hit events.
     * @param hl the HitListener to remove.
     */
    void removeHitListener(HitListener hl);
}