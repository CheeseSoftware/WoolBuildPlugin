package ostkaka34.WoolBuildPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ostkaka34.OstEconomyPlugin.IOstEconomy;

public class WoolBuildPlugin extends JavaPlugin implements Listener {
	IOstEconomy economyPlugin = null;
	
	List<Material> blocks = new ArrayList<Material>();
	
	@Override
	public void onEnable(){
		blocks.add(Material.WOOL);
		blocks.add(Material.GLASS);
		blocks.add(Material.TORCH);
		blocks.add(Material.WEB);
		blocks.add(Material.LADDER);
		blocks.add(Material.THIN_GLASS);
		blocks.add(Material.CAKE);
		blocks.add(Material.CACTUS);
		blocks.add(Material.CARPET);
		blocks.add(Material.MELON);
		blocks.add(Material.PUMPKIN);
		blocks.add(Material.JACK_O_LANTERN);
		blocks.add(Material.GRAVEL);
		
		//getCommand("testcommand").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		
		Plugin[] plugins = getServer().getPluginManager().getPlugins();
		
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i] instanceof IOstEconomy) {
				economyPlugin = (IOstEconomy) plugins[i];
				break;
			}
		}
		
		//economyPlugin = (IOstEconomy) getServer().getPluginManager().getPlugin("OstEconomyPlugin");
		
		//if (economyPlugin != null) {
			//economyPlugin.RegisterShopItem(Material.WOOL, 2000);
		//}
	}
	
	@Override
	public void onDisable(){
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return false; 
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		Material handType = player.getItemInHand().getType();
		Location loc;
		//
		//double distance = loc.distance(player.getEyeLocation());
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE && false) {
			/*if (action == Action.RIGHT_CLICK_BLOCK) {
				loc = player.getTargetBlock(null, 200).getLocation();
				
				if (handType == Material.WOOL || handType == Material.WEB) {
					if (RemoveFromHand(player)) {
						player.getWorld().getBlockAt(loc).setType(handType);
						player.playSound(player.getEyeLocation(), Sound.WOOD_CLICK, 1, 1);
					}
				}
			}
			elseif (action == Action.LEFT_CLICK_BLOCK) {
				loc = event.getClickedBlock().getLocation();
				
				Block block = player.getWorld().getBlockAt(loc);
				
				if (blocks.contains(block.getType()))
					block.breakNaturally();
			}*/
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event) {
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Material type = event.getBlock().getType();
			
			if (!blocks.contains(type))
				event.setCancelled(true);
			else if (type == Material.WEB)
				event.setInstaBreak(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Player player = event.getPlayer();
			Block block = event.getBlock();
			
			if (blocks.contains(block.getType())) {
				ItemStack item = new ItemStack(block.getType());
				item.setData(new MaterialData(block.getType(), block.getData()));
				player.getInventory().addItem(item);
			}
			
			block.setType(Material.AIR);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Material type = event.getBlock().getType();
			
			if (!(blocks.contains(type)))
				event.setCancelled(true);
		}
	}

	/*@SuppressWarnings("deprecation")
	private boolean RemoveFromHand(Player player) {
		ItemStack hand = player.getItemInHand();
		
		if (hand.getType() != Material.AIR) {
			if (hand.getAmount() == 1)
			{
				hand.setType(null);
			}
			else
			{
				hand.setAmount(hand.getAmount()-1);
				//inventory.setItem(index, item);
			}
			player.updateInventory();
			
			return true;
		}
		return false;
	}*/
}
