package net.goldtreeservers.worldguardextraflags.wg.wrappers;

import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.SessionManager;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.entity.Player;

public abstract class AbstractSessionManagerWrapper {
    protected final SessionManager sessionManager;

    public AbstractSessionManagerWrapper(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public abstract Session get(Player player);
    public abstract Session getIfPresent(Player player);

    public abstract void registerHandler(Handler.Factory<? extends Handler> factory);
}
