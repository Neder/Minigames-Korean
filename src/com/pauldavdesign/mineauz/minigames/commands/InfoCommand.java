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
		return "미니게임의 정보를 봅니다.";
	}

	
	public String[] getParameters() {
		return null;
	}

	
	public String[] getUsage() {
		return new String[] {"/minigame info <미니게임> [페이지]"};
	}

	
	public String getPermissionMessage() {
		return "미니게임 정보를 볼 수 있는 권한이 없습니다!";
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
				
				lines.add(ChatColor.GRAY + "게임 타입: " + ChatColor.GREEN + mgm.getType());
				if(!mgm.getType().equals("th")){
					lines.add(ChatColor.GRAY + "점수 타입: " + ChatColor.GREEN + mgm.getScoreType());
					if(!mgm.getType().equals("sp")){
						lines.add(ChatColor.GRAY + "최소 스코어: " + ChatColor.GREEN + mgm.getMinScore());
						lines.add(ChatColor.GRAY + "최대 스코어: " + ChatColor.GREEN + mgm.getMaxScore());
					}
					if(mgm.getType().equals("teamdm")){
						if(mgm.getDefaultWinner().equals("red")){
							lines.add(ChatColor.GRAY + "기본 승리 팀: " + ChatColor.RED + mgm.getDefaultWinner());
						}
						else if(mgm.getDefaultWinner().equals("blue")){
							lines.add(ChatColor.GRAY + "기본 승리 팀: " + ChatColor.BLUE + mgm.getDefaultWinner());
						}
						else{
							lines.add(ChatColor.GRAY + "기본 승리 팀: " + ChatColor.GRAY + mgm.getDefaultWinner());
						}
					}
					
					if(mgm.isEnabled()){
						lines.add(ChatColor.GRAY + "활성화: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "활성화: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.getUsePermissions()){
						lines.add(ChatColor.GRAY + "펄미션 사용: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "펄미션 사용: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.getLives() == 0){
						lines.add(ChatColor.GRAY + "목숨: " + ChatColor.GREEN + "무한");
					}
					else{
						lines.add(ChatColor.GRAY + "목숨: " + ChatColor.GREEN + mgm.getLives());
					}
					
					if(mgm.getStartLocations().size() > 0){
						lines.add(ChatColor.GRAY + "시작 지점 갯수: " + ChatColor.GREEN + mgm.getStartLocations().size());
					}
					else{
						lines.add(ChatColor.GRAY + "시작 지점 갯수: " + ChatColor.RED + "0");
					}
					if(mgm.getType().equals("teamdm")){
						if(mgm.getStartLocationsRed().size() > 0){
							lines.add(ChatColor.GRAY + "레드 팀 시작 지점 갯수: " + ChatColor.GREEN + mgm.getStartLocationsRed().size());
						}
						else{
							lines.add(ChatColor.GRAY + "레드 팀 시작 지점 갯수: " + ChatColor.RED + "0");
						}
						if(mgm.getStartLocationsBlue().size() > 0){
							lines.add(ChatColor.GRAY + "블루 팀 시작 지점 갯수: " + ChatColor.GREEN + mgm.getStartLocationsBlue().size());
						}
						else{
							lines.add(ChatColor.GRAY + "블루 팀 시작 지점 갯수:" + ChatColor.RED + "0");
						}
					}
					
					if(mgm.getEndPosition() != null){
						lines.add(ChatColor.GRAY + "승리자 이동 지점: " + ChatColor.GREEN + "설정됨");
					}
					else{
						lines.add(ChatColor.GRAY + "승리자 이동 지점: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(mgm.getQuitPosition() != null){
						lines.add(ChatColor.GRAY + "패배자 이동 지점: " + ChatColor.GREEN + "설정됨");
					}
					else{
						lines.add(ChatColor.GRAY + "패배자 이동 지점: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(!mgm.getType().equals("sp")){
						if(mgm.getLobbyPosition() != null){
							lines.add(ChatColor.GRAY + "로비 지점: " + ChatColor.GREEN + "설정됨");
						}
						else{
							lines.add(ChatColor.GRAY + "로비 지점: " + ChatColor.RED + "설정되지 않음");
						}
						
						if(mgm.getSpleefFloor1() != null){
							lines.add(ChatColor.GRAY + "바닥 파괴기 위치 1: " + ChatColor.GREEN + "네");
						}
						else{
							lines.add(ChatColor.GRAY + "바닥 파괴기 위치 1: " + ChatColor.RED + "아니오");
						}
						
						if(mgm.getSpleefFloor2() != null){
							lines.add(ChatColor.GRAY + "바닥 파괴기 위치 2: " + ChatColor.GREEN + "네");
						}
						else{
							lines.add(ChatColor.GRAY + "바닥 파괴기 위치 2: " + ChatColor.RED + "아니오");
						}
					}
					
					if(mgm.getRegenArea1() != null){
						lines.add(ChatColor.GRAY + "리젠 지역 위치 1: " + ChatColor.GREEN + "네");
					}
					else{
						lines.add(ChatColor.GRAY + "리젠 지역 위치 1: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.getRegenArea2() != null){
						lines.add(ChatColor.GRAY + "리젠 지역 위치 2: " + ChatColor.GREEN + "네");
					}
					else{
						lines.add(ChatColor.GRAY + "리젠 지역 위치 2: " + ChatColor.RED + "아니오");
					}
					
					lines.add(ChatColor.GRAY + "플레이어 게임모드: " + ChatColor.GREEN + mgm.getDefaultGamemode().name().toLowerCase());
					
					if(mgm.hasDeathDrops()){
						lines.add(ChatColor.GRAY + "죽을 시 아이템 드롭: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "죽을 시 아이템 드롭: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.hasItemDrops()){
						lines.add(ChatColor.GRAY + "플레이어 아이템 드롭: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "플레이어 아이템 드롭: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.hasItemPickup()){
						lines.add(ChatColor.GRAY + "플레이어 아이템 픽업: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "플레이어 아이템 픽업: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.canBlockBreak()){
						lines.add(ChatColor.GRAY + "블록 부수기: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "블록 부수기: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.canBlockPlace()){
						lines.add(ChatColor.GRAY + "블록 놓기: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "블록 놓기: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.canBlocksdrop()){
						lines.add(ChatColor.GRAY + "블록이 아이템 드롭하기: " + ChatColor.GREEN + "네");
					}else{
						lines.add(ChatColor.GRAY + "블록이 아이템 드롭하기: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.getBlockRecorder().getWhitelistMode()){
						lines.add(ChatColor.GRAY + "블록 놓기/부수기: " + ChatColor.GREEN + "화이트리스트 모드");
					}
					else{
						lines.add(ChatColor.GRAY + "블록 놓기/부수기: " + ChatColor.RED + "블랙리스트 모드");
					}
					
					if(mgm.hasFlags()){
						lines.add(ChatColor.GRAY + "깃발 수: " + ChatColor.GREEN + mgm.getFlags().size());
					}else{
						lines.add(ChatColor.GRAY + "깃발 수: " + ChatColor.RED + "0");
					}
					
					if(mgm.hasDefaultLoadout()){
						lines.add(ChatColor.GRAY + "기본 로드아웃 아이템 수: " + ChatColor.GREEN + mgm.getDefaultPlayerLoadout().getItems().size() + " 아이템");
					}else{
						lines.add(ChatColor.GRAY + "기본 로드아웃 아이템 수: " + ChatColor.RED + "0 아이템");
					}
					
					if(!mgm.getLoadouts().isEmpty()){
						lines.add(ChatColor.GRAY + "추가 로드아웃 수: " + ChatColor.GREEN + mgm.getLoadouts().size());
					}else{
						lines.add(ChatColor.GRAY + "추가 로드아웃 수: " + ChatColor.RED + "0");
					}
					
					if(!mgm.getType().equals("sp")){
						lines.add(ChatColor.GRAY + "최대 플레이어: " + ChatColor.GREEN + mgm.getMaxPlayers());
						lines.add(ChatColor.GRAY + "최소 플레이어: " + ChatColor.GREEN + mgm.getMinPlayers());
					}
					
					if(mgm.getRewardItem() != null){
						lines.add(ChatColor.GRAY + "아이템 보상: " + ChatColor.GREEN + MinigameUtils.getItemStackName(mgm.getRewardItem()) + "(" + mgm.getRewardItem().getAmount() + " 개)");
					}else{
						lines.add(ChatColor.GRAY + "아이템 보상: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(mgm.getRewardPrice() != 0){
						lines.add(ChatColor.GRAY + "돈 보상: " + ChatColor.GREEN +mgm.getRewardPrice() + "원");
					}
					else{
						lines.add(ChatColor.GRAY + "돈 보상: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(mgm.getSecondaryRewardItem() != null){
						lines.add(ChatColor.GRAY + "두번째 아이템 보상: " + ChatColor.GREEN + MinigameUtils.getItemStackName(mgm.getSecondaryRewardItem()) + "(" + mgm.getSecondaryRewardItem().getAmount() + " 개)");
					}else{
						lines.add(ChatColor.GRAY + "두번째 아이템 보상: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(mgm.getSecondaryRewardPrice() != 0){
						lines.add(ChatColor.GRAY + "두번째 돈 보상: " + ChatColor.GREEN + mgm.getSecondaryRewardPrice() + "원");
					}
					else{
						lines.add(ChatColor.GRAY + "두번째 돈 보상: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(!mgm.getType().equals("sp")){
						if(mgm.getTimer() != 0){
							lines.add(ChatColor.GRAY + "게임 타이머: " + ChatColor.GREEN + MinigameUtils.convertTime(mgm.getTimer()));
						}
						else{
							lines.add(ChatColor.GRAY + "게임 타이머: " + ChatColor.RED + "설정되지 않음");
						}
					}
					
					if(mgm.hasPaintBallMode()){
						lines.add(ChatColor.GRAY + "페인트볼 모드: " + ChatColor.GREEN + "네");
						lines.add(ChatColor.GRAY + "페인트볼 데미지: " + ChatColor.GREEN + mgm.getPaintBallDamage());
					}
					else{
						lines.add(ChatColor.GRAY + "페인트볼 모드: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.hasUnlimitedAmmo()){
						lines.add(ChatColor.GRAY + "무한 총알: " + ChatColor.GREEN + "네");
					}
					else{
						lines.add(ChatColor.GRAY + "무한 총알: " + ChatColor.RED + "아니오");
					}
					
					if(mgm.getType().equals("sp")){
						if(mgm.canSaveCheckpoint()){
							lines.add(ChatColor.GRAY + "체크포인트 저장: " + ChatColor.GREEN + "네");
						}
						else{
							lines.add(ChatColor.GRAY + "체크포인트 저장: " + ChatColor.RED + "아니오");
						}
					}
					else{
						if(mgm.canLateJoin()){
							lines.add(ChatColor.GRAY + "시작한 후 들어가기: " + ChatColor.GREEN + "네");
						}
						else{
							lines.add(ChatColor.GRAY + "시작한 후 들어가기: " + ChatColor.RED + "아니오");
						}
					}
					
					if(mgm.isRandomizeChests()){
						lines.add(ChatColor.GRAY + "랜덤 체스트: " + ChatColor.GREEN + "네");
					}
					else{
						lines.add(ChatColor.GRAY + "랜덤 체스트: " + ChatColor.RED + "아니오");
					}
				}
				else{
					if(mgm.getStartLocations().size() > 0){
						lines.add(ChatColor.GRAY + "시작 지점: " + ChatColor.GREEN + mgm.getStartLocations().get(0).getBlockX() + "x, " + 
								mgm.getStartLocations().get(0).getBlockY() + "y, " + 
								mgm.getStartLocations().get(0).getBlockZ() + "z");
					}
					else{
						lines.add(ChatColor.GRAY + "시작 지점: " + ChatColor.RED + "설정되지 않음");
					}
					
					if(mgm.getLocation() != null){
						lines.add(ChatColor.GRAY + "위치 이름: " + ChatColor.GREEN + mgm.getLocation());
					}
					else{
						lines.add(ChatColor.GRAY + "위치 이름: " + ChatColor.RED + "설정되지 않음");
					}
					
					lines.add(ChatColor.GRAY + "최대 거리: " + ChatColor.GREEN + mgm.getMaxRadius());
					lines.add(ChatColor.GRAY + "최소 보물: " + ChatColor.GREEN + mgm.getMinTreasure());
					lines.add(ChatColor.GRAY + "최대 보물: " + ChatColor.GREEN + mgm.getMaxTreasure());
					
					if(mgm.hasDefaultLoadout()){
						lines.add(ChatColor.GRAY + "기본 로드아웃 아이템 갯수: " + ChatColor.GREEN + mgm.getDefaultPlayerLoadout().getItems().size() + " 아이템");
					}else{
						lines.add(ChatColor.GRAY + "기본 로드아웃 아이템 갯수: " + ChatColor.RED + "0 아이템");
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
				sender.sendMessage(ChatColor.GREEN + "-------------------페이지 " + page + "/" + pages + "-------------------");
				
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
				sender.sendMessage(ChatColor.RED + args[0] + "라는 이름을 가진 미니게임은 없습니다!");
			}
			return true;
		}
		return false;
	}
}
