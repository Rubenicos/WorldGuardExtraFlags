package net.goldtreeservers.worldguardextraflags.wg.handlers;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import net.goldtreeservers.worldguardextraflags.wg.WorldGuardUtils;
import net.goldtreeservers.worldguardextraflags.wg.wrappers.HandlerWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CommandOnEntryFlagHandler extends HandlerWrapper
{
	public static final Factory FACTORY(Plugin plugin)
	{
		return new Factory(plugin);
	}
	
    public static class Factory extends HandlerWrapper.Factory<CommandOnEntryFlagHandler>
    {
        public Factory(Plugin plugin)
        {
			super(plugin);
		}

		@Override
        public CommandOnEntryFlagHandler create(Session session)
        {
            return new CommandOnEntryFlagHandler(this.getPlugin(), session);
        }
    }
	
	private Collection<Set<String>> lastCommands;
	    
	protected CommandOnEntryFlagHandler(Plugin plugin, Session session)
	{
		super(plugin, session);
		
		this.lastCommands = new ArrayList<>();
	}

	@Override
	public boolean onCrossBoundary(Player player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType)
	{
		Collection<Set<String>> commands = WorldGuardUtils.queryAllValues(player, to.getWorld(), toSet.getRegions(), Flags.COMMAND_ON_ENTRY);

		for(Set<String> commands_ : commands)
		{
			if (!this.lastCommands.contains(commands_) && commands_.size() > 0)
			{
				for(String command : commands_)
				{
					this.getPlugin().getServer().dispatchCommand(player, command.substring(1).replace("%username%", player.getName())); //TODO: Make this better
				}
				
				break;
			}
		}
		
		this.lastCommands = new ArrayList<Set<String>>(commands);
		
		if (!this.lastCommands.isEmpty())
		{
			for (ProtectedRegion region : toSet)
			{
                Set<String> commands_ = region.getFlag(Flags.COMMAND_ON_ENTRY);
                if (commands_ != null)
                {
                	this.lastCommands.add(commands_);
                }
            }
		}
		
		return true;
	}
}
