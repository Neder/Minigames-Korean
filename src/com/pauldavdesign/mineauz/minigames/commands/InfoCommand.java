package com.pauldavdesign.mineauz.minigames.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.pauldavdesign.mineauz.minigames.Minigame;
import com.pauldavdesign.mineauz.minigames.MinigameUtils;

public class InfoCommand implements ICommand{

	
	public String getName() {
		return "info";
	}

	
	public String[] getAliases() {
		return null;
	}

	
	public boolean canBeConsole() {
		return true;
	}

	
	public String getDescription() {
		return "�̴ϰ����� ������ ���ϴ�.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame info <�̴ϰ���> [������]"};
	}

	
	public String getPermissionMessage() {
		return "�̴ϰ��� ������ �� �� �ִ� ������ �����ϴ�!";
	}

	
	public String getPermission() {
		return "minigame.info";
	}

	
	public boolean onCommand(CommandSender sender, Minigame minigame,
			String label, String[] args) {
		if(args != null){
			Minigame mgm = plugin.mdata.getMinigame(args[0]);
		
			if(mgm != null){
				//Ten lines per page
				List<String> lines = new ArrayList<String>();
				
				lines.add(ChatColor.GRAY + "���� Ÿ��: " + ChatColor.GREEN + mgm.getType());
				if(!mgm.getType().equals("th")){
					lines.add(ChatColor.GRAY + "���� Ÿ��: " + ChatColor.GREEN + mgm.getScoreType());
					if(!mgm.getType().equals("sp")){
						lines.add(ChatColor.GRAY + "�ּ� ���ھ�: " + ChatColor.GREEN + mgm.getMinScore());
						lines.add(ChatColor.GRAY + "�ִ� ���ھ�: " + ChatColor.GREEN + mgm.getMaxScore());
					}
					if(mgm.getType().equals("teamdm")){
						if(mgm.getDefaultWinner().equals("red")){
							lines.add(ChatColor.GRAY + "�⺻ �¸� ��: " + ChatColor.RED + mgm.getDefaultWinner());
						}
						else if(mgm.getDefaultWinner().equals("blue")){
							lines.add(ChatColor.GRAY + "�⺻ �¸� ��: " + ChatColor.BLUE + mgm.getDefaultWinner());
						}
						else{
							lines.add(ChatColor.GRAY + "�⺻ �¸� ��: " + ChatColor.GRAY + mgm.getDefaultWinner());
						}
					}
					
					if(mgm.isEnabled()){
						lines.add(ChatColor.GRAY + "Ȱ��ȭ: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "Ȱ��ȭ: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.getUsePermissions()){
						lines.add(ChatColor.GRAY + "�޹̼� ���: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "�޹̼� ���: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.getLives() == 0){
						lines.add(ChatColor.GRAY + "���: " + ChatColor.GREEN + "����");
					}
					else{
						lines.add(ChatColor.GRAY + "���: " + ChatColor.GREEN + mgm.getLives());
					}
					
					if(mgm.getStartLocations().size() > 0){
						lines.add(ChatColor.GRAY + "���� ���� ����: " + ChatColor.GREEN + mgm.getStartLocations().size());
					}
					else{
						lines.add(ChatColor.GRAY + "���� ���� ����: " + ChatColor.RED + "0");
					}
					if(mgm.getType().equals("teamdm")){
						if(mgm.getStartLocationsRed().size() > 0){
							lines.add(ChatColor.GRAY + "���� �� ���� ���� ����: " + ChatColor.GREEN + mgm.getStartLocationsRed().size());
						}
						else{
							lines.add(ChatColor.GRAY + "���� �� ���� ���� ����: " + ChatColor.RED + "0");
						}
						if(mgm.getStartLocationsBlue().size() > 0){
							lines.add(ChatColor.GRAY + "��� �� ���� ���� ����: " + ChatColor.GREEN + mgm.getStartLocationsBlue().size());
						}
						else{
							lines.add(ChatColor.GRAY + "��� �� ���� ���� ����:" + ChatColor.RED + "0");
						}
					}
					
					if(mgm.getEndPosition() != null){
						lines.add(ChatColor.GRAY + "�¸��� �̵� ����: " + ChatColor.GREEN + "������");
					}
					else{
						lines.add(ChatColor.GRAY + "�¸��� �̵� ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(mgm.getQuitPosition() != null){
						lines.add(ChatColor.GRAY + "�й��� �̵� ����: " + ChatColor.GREEN + "������");
					}
					else{
						lines.add(ChatColor.GRAY + "�й��� �̵� ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(!mgm.getType().equals("sp")){
						if(mgm.getLobbyPosition() != null){
							lines.add(ChatColor.GRAY + "�κ� ����: " + ChatColor.GREEN + "������");
						}
						else{
							lines.add(ChatColor.GRAY + "�κ� ����: " + ChatColor.RED + "�������� ����");
						}
						
						if(mgm.getSpleefFloor1() != null){
							lines.add(ChatColor.GRAY + "�ٴ� �ı��� ��ġ 1: " + ChatColor.GREEN + "��");
						}
						else{
							lines.add(ChatColor.GRAY + "�ٴ� �ı��� ��ġ 1: " + ChatColor.RED + "�ƴϿ�");
						}
						
						if(mgm.getSpleefFloor2() != null){
							lines.add(ChatColor.GRAY + "�ٴ� �ı��� ��ġ 2: " + ChatColor.GREEN + "��");
						}
						else{
							lines.add(ChatColor.GRAY + "�ٴ� �ı��� ��ġ 2: " + ChatColor.RED + "�ƴϿ�");
						}
					}
					
					if(mgm.getRegenArea1() != null){
						lines.add(ChatColor.GRAY + "���� ���� ��ġ 1: " + ChatColor.GREEN + "��");
					}
					else{
						lines.add(ChatColor.GRAY + "���� ���� ��ġ 1: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.getRegenArea2() != null){
						lines.add(ChatColor.GRAY + "���� ���� ��ġ 2: " + ChatColor.GREEN + "��");
					}
					else{
						lines.add(ChatColor.GRAY + "���� ���� ��ġ 2: " + ChatColor.RED + "�ƴϿ�");
					}
					
					lines.add(ChatColor.GRAY + "�÷��̾� ���Ӹ��: " + ChatColor.GREEN + mgm.getDefaultGamemode().name().toLowerCase());
					
					if(mgm.hasDeathDrops()){
						lines.add(ChatColor.GRAY + "���� �� ������ ���: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "���� �� ������ ���: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.hasItemDrops()){
						lines.add(ChatColor.GRAY + "�÷��̾� ������ ���: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "�÷��̾� ������ ���: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.hasItemPickup()){
						lines.add(ChatColor.GRAY + "�÷��̾� ������ �Ⱦ�: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "�÷��̾� ������ �Ⱦ�: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.canBlockBreak()){
						lines.add(ChatColor.GRAY + "��� �μ���: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "��� �μ���: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.canBlockPlace()){
						lines.add(ChatColor.GRAY + "��� ����: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "��� ����: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.canBlocksdrop()){
						lines.add(ChatColor.GRAY + "����� ������ ����ϱ�: " + ChatColor.GREEN + "��");
					}else{
						lines.add(ChatColor.GRAY + "����� ������ ����ϱ�: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.getBlockRecorder().getWhitelistMode()){
						lines.add(ChatColor.GRAY + "��� ����/�μ���: " + ChatColor.GREEN + "ȭ��Ʈ����Ʈ ���");
					}
					else{
						lines.add(ChatColor.GRAY + "��� ����/�μ���: " + ChatColor.RED + "������Ʈ ���");
					}
					
					if(mgm.hasFlags()){
						lines.add(ChatColor.GRAY + "��� ��: " + ChatColor.GREEN + mgm.getFlags().size());
					}else{
						lines.add(ChatColor.GRAY + "��� ��: " + ChatColor.RED + "0");
					}
					
					if(mgm.hasDefaultLoadout()){
						lines.add(ChatColor.GRAY + "�⺻ �ε�ƿ� ������ ��: " + ChatColor.GREEN + mgm.getDefaultPlayerLoadout().getItems().size() + " ������");
					}else{
						lines.add(ChatColor.GRAY + "�⺻ �ε�ƿ� ������ ��: " + ChatColor.RED + "0 ������");
					}
					
					if(!mgm.getLoadouts().isEmpty()){
						lines.add(ChatColor.GRAY + "�߰� �ε�ƿ� ��: " + ChatColor.GREEN + mgm.getLoadouts().size());
					}else{
						lines.add(ChatColor.GRAY + "�߰� �ε�ƿ� ��: " + ChatColor.RED + "0");
					}
					
					if(!mgm.getType().equals("sp")){
						lines.add(ChatColor.GRAY + "�ִ� �÷��̾�: " + ChatColor.GREEN + mgm.getMaxPlayers());
						lines.add(ChatColor.GRAY + "�ּ� �÷��̾�: " + ChatColor.GREEN + mgm.getMinPlayers());
					}
					
					if(mgm.getRewardItem() != null){
						lines.add(ChatColor.GRAY + "������ ����: " + ChatColor.GREEN + MinigameUtils.getItemStackName(mgm.getRewardItem()) + "(" + mgm.getRewardItem().getAmount() + " ��)");
					}else{
						lines.add(ChatColor.GRAY + "������ ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(mgm.getRewardPrice() != 0){
						lines.add(ChatColor.GRAY + "�� ����: " + ChatColor.GREEN +mgm.getRewardPrice() + "��");
					}
					else{
						lines.add(ChatColor.GRAY + "�� ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(mgm.getSecondaryRewardItem() != null){
						lines.add(ChatColor.GRAY + "�ι�° ������ ����: " + ChatColor.GREEN + MinigameUtils.getItemStackName(mgm.getSecondaryRewardItem()) + "(" + mgm.getSecondaryRewardItem().getAmount() + " ��)");
					}else{
						lines.add(ChatColor.GRAY + "�ι�° ������ ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(mgm.getSecondaryRewardPrice() != 0){
						lines.add(ChatColor.GRAY + "�ι�° �� ����: " + ChatColor.GREEN + mgm.getSecondaryRewardPrice() + "��");
					}
					else{
						lines.add(ChatColor.GRAY + "�ι�° �� ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(!mgm.getType().equals("sp")){
						if(mgm.getTimer() != 0){
							lines.add(ChatColor.GRAY + "���� Ÿ�̸�: " + ChatColor.GREEN + MinigameUtils.convertTime(mgm.getTimer()));
						}
						else{
							lines.add(ChatColor.GRAY + "���� Ÿ�̸�: " + ChatColor.RED + "�������� ����");
						}
					}
					
					if(mgm.hasPaintBallMode()){
						lines.add(ChatColor.GRAY + "����Ʈ�� ���: " + ChatColor.GREEN + "��");
						lines.add(ChatColor.GRAY + "����Ʈ�� ������: " + ChatColor.GREEN + mgm.getPaintBallDamage());
					}
					else{
						lines.add(ChatColor.GRAY + "����Ʈ�� ���: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.hasUnlimitedAmmo()){
						lines.add(ChatColor.GRAY + "���� �Ѿ�: " + ChatColor.GREEN + "��");
					}
					else{
						lines.add(ChatColor.GRAY + "���� �Ѿ�: " + ChatColor.RED + "�ƴϿ�");
					}
					
					if(mgm.getType().equals("sp")){
						if(mgm.canSaveCheckpoint()){
							lines.add(ChatColor.GRAY + "üũ����Ʈ ����: " + ChatColor.GREEN + "��");
						}
						else{
							lines.add(ChatColor.GRAY + "üũ����Ʈ ����: " + ChatColor.RED + "�ƴϿ�");
						}
					}
					else{
						if(mgm.canLateJoin()){
							lines.add(ChatColor.GRAY + "������ �� ����: " + ChatColor.GREEN + "��");
						}
						else{
							lines.add(ChatColor.GRAY + "������ �� ����: " + ChatColor.RED + "�ƴϿ�");
						}
					}
					
					if(mgm.isRandomizeChests()){
						lines.add(ChatColor.GRAY + "���� ü��Ʈ: " + ChatColor.GREEN + "��");
					}
					else{
						lines.add(ChatColor.GRAY + "���� ü��Ʈ: " + ChatColor.RED + "�ƴϿ�");
					}
				}
				else{
					if(mgm.getStartLocations().size() > 0){
						lines.add(ChatColor.GRAY + "���� ����: " + ChatColor.GREEN + mgm.getStartLocations().get(0).getBlockX() + "x, " + 
								mgm.getStartLocations().get(0).getBlockY() + "y, " + 
								mgm.getStartLocations().get(0).getBlockZ() + "z");
					}
					else{
						lines.add(ChatColor.GRAY + "���� ����: " + ChatColor.RED + "�������� ����");
					}
					
					if(mgm.getLocation() != null){
						lines.add(ChatColor.GRAY + "��ġ �̸�: " + ChatColor.GREEN + mgm.getLocation());
					}
					else{
						lines.add(ChatColor.GRAY + "��ġ �̸�: " + ChatColor.RED + "�������� ����");
					}
					
					lines.add(ChatColor.GRAY + "�ִ� �Ÿ�: " + ChatColor.GREEN + mgm.getMaxRadius());
					lines.add(ChatColor.GRAY + "�ּ� ����: " + ChatColor.GREEN + mgm.getMinTreasure());
					lines.add(ChatColor.GRAY + "�ִ� ����: " + ChatColor.GREEN + mgm.getMaxTreasure());
					
					if(mgm.hasDefaultLoadout()){
						lines.add(ChatColor.GRAY + "�⺻ �ε�ƿ� ������ ����: " + ChatColor.GREEN + mgm.getDefaultPlayerLoadout().getItems().size() + " ������");
					}else{
						lines.add(ChatColor.GRAY + "�⺻ �ε�ƿ� ������ ����: " + ChatColor.RED + "0 ������");
					}
				}
				
				int page = 1;
				int pages = 1;
				
				if(lines.size() > 9){
					double pageamnt = Math.ceil(((double)lines.size()) / 9);
					pages = (int) pageamnt;
				}
				
				if(args.length >= 2 && args[1].matches("[0-9]+")){
					page = Integer.parseInt(args[1]);
					if(page > pages){
						page = pages;
					}
				}
				sender.sendMessage(ChatColor.GREEN + "-------------------������ " + page + "/" + pages + "-------------------");
				
				int offset = 0 + (page * 9 - 9);
				int offsetUpper = offset + 8;
				if(offsetUpper >= lines.size()){
					offsetUpper = lines.size() - 1;
				}
				
				for(int i = offset; i <= offsetUpper; i++){
					sender.sendMessage(lines.get(i));
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + args[0] + "��� �̸��� ���� �̴ϰ����� �����ϴ�!");
			}
			return true;
		}
		return false;
	}
}
