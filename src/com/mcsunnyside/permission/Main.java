package com.mcsunnyside.permission;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {
	boolean asyncInit = false;
	boolean syncInit = false;
	boolean timeCheck = false;
	ArrayList<String> CommandList = new ArrayList<String>();
	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
	    asyncInit = false;
	    syncInit = false;
		Bukkit.getPluginManager().registerEvents(this, this);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!asyncInit) {
					getLogger().info("Async module inited.");
					asyncInit=true;
				}
				Collection<? extends Player> online = Bukkit.getServer().getOnlinePlayers();
				for (Player player : online) {
					WARNpermissionCheck(player);
				}
				
			}
		}.runTaskTimer(this, 10, 10);
	}
	private void WARNpermissionCheck(Player player) {
		if(player.isOp() || player.hasPermission("*") || player.hasPermission("removePermission.admin")) {
			boolean isTrusted = getConfig().getBoolean(player.getName());
			if(!isTrusted) {
				String playername = player.getName();
				player.setOp(false);
				player.closeInventory();
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"deop "+playername);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"pex user "+playername+" remove *");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"pex user "+playername+" remove *.*");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"pex group admin user remove "+playername);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"ban "+playername+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"cmi ban "+playername+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"kick "+playername+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"cmi ban "+playername+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"cmi kick "+playername+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"banip "+playername+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"banip "+player.getAddress().getHostName()+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"cmi banip "+player.getAddress().getHostName()+" [PermissionCheck]��ӵ�зǷ�Ȩ�޶������");
				Bukkit.banIP(player.getAddress().getHostString());
				getLogger().info("WARNING: Player "+player.getName()+" have admin permission,banned.");
				getLogger().info("WARNING: IP:"+player.getAddress().getHostString()+":"+player.getAddress().getPort());
				getLogger().info("WARNING: GameMode:"+player.getGameMode());
				getConfig().set("banned."+playername, System.currentTimeMillis());
				Bukkit.broadcastMessage("��c��lWARNING:��b��⵽�Ƿ�����Ա��� ��a"+playername+" ��bӵ�й���ԱȨ�ޣ���ִ�з������");
				Bukkit.broadcastMessage("��c��lWARNING:��a���������˼����Ϣ���ͼ�����͸�����������Ա лл!");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"mail send Ghost_chu "+playername+"banned because have admin permission.");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"cmi mail send Ghost_chu "+playername+"banned because have admin permission.");
				Bukkit.broadcastMessage("��b��lINFO:��a���ڶԷǷ�����Ա���24Сʱ�ڵ����������в���ִ��ȫ�ֻص�...");
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"co rollback p:"+playername+" r:#global t:1d");
				Bukkit.broadcastMessage("��b��lINFO:��a�ص�ִ�����.");
				saveConfig();
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player)sender;
			WARNpermissionCheck(player);
		}
		if(!label.equals("removepermission")) {
			return false;
		}
		if(args.length==1) {
			if(args[0].equals("reload")) {
				reloadConfig();
				getLogger().info("Reloaded");
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onJoin (PlayerJoinEvent e) {
		Player player = e.getPlayer();
		WARNpermissionCheck(player);
	}
	@EventHandler
	public void onQuit (PlayerQuitEvent e) {
		Player player = e.getPlayer();
		WARNpermissionCheck(player);
	}
}
