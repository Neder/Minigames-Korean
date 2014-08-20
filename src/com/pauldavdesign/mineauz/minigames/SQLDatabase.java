package com.pauldavdesign.mineauz.minigames;

import lib.PatPeter.SQLibrary.Database;
import lib.PatPeter.SQLibrary.MySQL;

public class SQLDatabase {
	private Database sql = null;
	private static Minigames plugin = Minigames.plugin;
	
	public void loadSQL(){
		if(plugin.getServer().getPluginManager().getPlugin("SQLibrary") != null){
			if(plugin.getConfig().getBoolean("use-sql")){
				setSql(new MySQL(plugin.getLogger(), 
						"[PMGO-L] ", 
						plugin.getConfig().getString("sql-host"), 
						plugin.getConfig().getInt("sql-port"), 
						plugin.getConfig().getString("sql-database"), 
						plugin.getConfig().getString("sql-username"), 
						plugin.getConfig().getString("sql-password")));
			}
		}
		else{
			plugin.getLogger().info("SQLibrary 가 존재하지 않습니다! SQL 로 데이타를 저장할 수 없습니다!");
		}
	}

	public Database getSql() {
		return sql;
	}

	public void setSql(Database sql) {
		this.sql = sql;
	}
}
