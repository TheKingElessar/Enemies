package com.thekingelessar.enemies;

import com.google.common.collect.Lists;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.thekingelessar.enemies.Enemies.*;
import static net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord;


public class CommandEnemy implements ICommand
{
    private static final String seperator = EnumChatFormatting.BLUE + "------------------------------------\n";
    private static final String add = EnumChatFormatting.YELLOW + "/e add <player>" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "Add a player as an enemy\n";
    private static final String remove = EnumChatFormatting.YELLOW + "/e remove <player>" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "Remove a player from your enemies\n";
    private static final String list = EnumChatFormatting.YELLOW + "/e list" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "List your enemies\n";
    private static final String help = EnumChatFormatting.YELLOW + "/e help" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "Prints all available enemy commands\n";
    private static final String seperator2 = EnumChatFormatting.BLUE + "------------------------------------";
    
    private static final String helpString = seperator + EnumChatFormatting.GREEN + "Enemy Commands:\n" + add + remove + list + help + seperator2;
    
    
    @Override
    public int compareTo(ICommand o)
    {
        return 0;
    }
    
    @Override
    public String getCommandName()
    {
        return "enemy";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        
        return "/e <player>";
    }
    
    @Override
    public List<String> getCommandAliases()
    {
        
        return Lists.newArrayList("e");
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerSP player = (EntityPlayerSP) sender;
        
        if (args.length == 0)
        {
            player.addChatMessage(new ChatComponentText(helpString));
            return;
        }
        
        switch (args[0].toLowerCase())
        {
            case "help":
                player.addChatMessage(new ChatComponentText(helpString));
                break;
            
            case "list":
            case "l":
                String enemiesPrintout = seperator + "                  " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + "<< " + EnumChatFormatting.GOLD + "Enemies" + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + " >>\n";
                
                if (enemiesMap.containsKey(player.getUniqueID().toString()))
                {
                    for (String enemyName : enemiesMap.get(player.getUniqueID().toString()))
                    {
                        enemiesPrintout += EnumChatFormatting.RED + enemyName + EnumChatFormatting.YELLOW + getRandomInsult() + "\n";
                    }
                    
                    if (enemiesMap.get(player.getUniqueID().toString()).size() == 0)
                    {
                        enemiesPrintout += EnumChatFormatting.GREEN + "You have no enemies! :)\n";
                    }
                    
                }
                else
                {
                    enemiesPrintout += EnumChatFormatting.GREEN + "You have no enemies! :)\n";
                }
                
                enemiesPrintout += seperator2;
                player.addChatMessage(new ChatComponentText(enemiesPrintout));
                break;
            
            
            case "r":
            case "remove":
                boolean found = false;
                
                if (enemiesMap.containsKey(player.getUniqueID().toString()))
                {
                    List<String> enemiesList = enemiesMap.get(player.getUniqueID().toString());
                    
                    for (String enemy : enemiesList)
                    {
                        if (enemy.equalsIgnoreCase(args[1]))
                        {
                            enemiesList.remove(enemy);
                            
                            saveEnemies();
                            found = true;
                            
                            player.addChatMessage(new ChatComponentText(seperator + EnumChatFormatting.YELLOW + "You removed " + EnumChatFormatting.RED + enemy + EnumChatFormatting.YELLOW + " from your enemies list!\n" + seperator2));
                            break;
                        }
                    }
                }
                
                if (!found)
                {
                    player.addChatMessage(new ChatComponentText(seperator + EnumChatFormatting.GREEN + args[1] + EnumChatFormatting.YELLOW + " is not in your enemies list!\n" + seperator2));
                    break;
                }
                break;
            
            case "a":
            case "add":
                if (enemiesMap.containsKey(player.getUniqueID().toString()))
                {
                    enemiesMap.get(player.getUniqueID().toString()).add(args[1]);
                }
                else
                {
                    List<String> newEnemyList = new ArrayList<>();
                    newEnemyList.add(args[1]);
                    enemiesMap.put(player.getUniqueID().toString(), newEnemyList);
                }
                
                saveEnemies();
                
                player.addChatMessage(new ChatComponentText(seperator + EnumChatFormatting.YELLOW + "You have added " + EnumChatFormatting.RED + args[1] + EnumChatFormatting.YELLOW + " as an enemy!\n" + seperator2));
                break;
            
            default:
                if (enemiesMap.containsKey(player.getUniqueID().toString()))
                {
                    enemiesMap.get(player.getUniqueID().toString()).add(args[0]);
                }
                else
                {
                    List<String> newEnemyList = new ArrayList<>();
                    newEnemyList.add(args[0]);
                    enemiesMap.put(player.getUniqueID().toString(), newEnemyList);
                }
                
                saveEnemies();
                
                player.addChatMessage(new ChatComponentText(seperator + EnumChatFormatting.YELLOW + "You have added " + EnumChatFormatting.RED + args[0] + EnumChatFormatting.YELLOW + " as an enemy!\n" + seperator2));
                break;
        }
    }
    
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }
    
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        EntityPlayerSP player = (EntityPlayerSP) sender;
        return args.length >= 1 ? getListOfStringsMatchingLastWord(args, player.worldObj.playerEntities) : null;
    }
    
    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }
    
}