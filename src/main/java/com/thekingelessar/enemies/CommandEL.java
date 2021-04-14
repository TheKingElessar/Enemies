package com.thekingelessar.enemies;

import com.google.common.collect.Lists;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.thekingelessar.enemies.Enemies.enemiesMap;
import static com.thekingelessar.enemies.Enemies.getRandomInsult;
import static net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord;

public class CommandEL implements ICommand
{
    private static final String seperator = EnumChatFormatting.BLUE + "------------------------------------\n";
    private static final String add = EnumChatFormatting.YELLOW + "/e add <player>" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "Add a player as an enemy\n";
    private static final String remove = EnumChatFormatting.YELLOW + "/e remove <player>" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "Remove a player from your enemies\n";
    private static final String list = EnumChatFormatting.YELLOW + "/e list" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "List your enemies\n";
    private static final String help = EnumChatFormatting.YELLOW + "/e help" + EnumChatFormatting.GRAY + " - " + EnumChatFormatting.AQUA + "Prints all available enemy commands\n";
    private static final String seperator2 = EnumChatFormatting.BLUE + "------------------------------------";
    
    @Override
    public String getCommandName()
    {
        return "el";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        
        return "/l";
    }
    
    @Override
    public List<String> getCommandAliases()
    {
        return Lists.newArrayList("el");
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerSP player = (EntityPlayerSP) sender;
        
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
    
    @Override
    public int compareTo(ICommand o)
    {
        return 0;
    }
}