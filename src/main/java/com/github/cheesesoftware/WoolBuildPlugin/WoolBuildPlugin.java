package com.github.cheesesoftware.WoolBuildPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.github.cheesesoftware.OstEconomyPlugin.IOstEconomy;

public class WoolBuildPlugin extends JavaPlugin implements Listener {
	IOstEconomy economyPlugin = null;
	
	List<Material> blocks;
	List<Material> weakBlocks;
	Map<Material, Material> replaceBlocks;
	
	@Override
	public void onEnable(){
		blocks = new ArrayList<Material>();
		weakBlocks = new ArrayList<Material>();
		replaceBlocks = new HashMap<Material, Material>();
		
		blocks.add(Material.WOOL);
		
		blocks.add(Material.GLASS);
		blocks.add(Material.THIN_GLASS);
		blocks.add(Material.STAINED_GLASS);
		blocks.add(Material.STAINED_GLASS_PANE);
		
		blocks.add(Material.TORCH);
		blocks.add(Material.WEB);
		blocks.add(Material.LADDER);
		blocks.add(Material.CAKE);
		blocks.add(Material.CACTUS);
		blocks.add(Material.CARPET);
		blocks.add(Material.MELON);
		blocks.add(Material.SNOW_BLOCK);
		blocks.add(Material.PUMPKIN);
		blocks.add(Material.JACK_O_LANTERN);
		blocks.add(Material.GRAVEL);

		blocks.add(Material.CHEST);
		
		weakBlocks.add(Material.GLASS);
		weakBlocks.add(Material.THIN_GLASS);
		weakBlocks.add(Material.LONG_GRASS);
		weakBlocks.add(Material.DEAD_BUSH);
		weakBlocks.add(Material.DOUBLE_PLANT);
		weakBlocks.add(Material.RED_ROSE);
		weakBlocks.add(Material.YELLOW_FLOWER);
		weakBlocks.add(Material.VINE);
		
		replaceBlocks.put(Material.GLASS, Material.WOOD);
		replaceBlocks.put(Material.THIN_GLASS, Material.WOOD);
		replaceBlocks.put(Material.STAINED_GLASS_PANE, Material.WOOD);
		
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

	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		
		Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Material handType = player.getItemInHand().getType();
		Location loc = block.getLocation();
		
		double distance = loc.distance(player.getEyeLocation());
		
		if (action == Action.RIGHT_CLICK_BLOCK && distance < 5)
		{
			if (replaceBlocks.containsKey(block.getType()))
			{
				if (replaceBlocks.get(block.getType()) == handType)
				{
					block.setType(handType);
				
					RemoveFromHand(event.getPlayer());
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event) {
		if (event.isCancelled())
			return;
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Material type = event.getBlock().getType();
			
			if (!blocks.contains(type) && !weakBlocks.contains(type))
				event.setCancelled(true);
			else if (type == Material.WEB)
				event.setInstaBreak(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Player player = event.getPlayer();
			Block block = event.getBlock();
			
			if (blocks.contains(block.getType()) && !weakBlocks.contains(block.getType())) {
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
		if (event.isCancelled())
			return;
		
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			Material type = event.getBlock().getType();
			Block otherBlock = event.getPlayer().getTargetBlock(null, 200);
					
					/*player.getTargetBlock(null, 200).getLocation();*/
			if (replaceBlocks.containsKey(otherBlock.getType()))
			{
				event.setCancelled(true);
			}
			if (!(blocks.contains(type)))
			{
				event.setCancelled(true);
			}
		}
	}

	private boolean RemoveFromHand(Player player) {
		ItemStack hand = player.getItemInHand();
		
		if (hand.getType() != Material.AIR) {
			if (hand.getAmount() == 1)
			{
				hand.setType(Material.AIR);
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
	}
}
