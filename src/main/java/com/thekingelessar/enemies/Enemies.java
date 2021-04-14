package com.thekingelessar.enemies;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Mod (modid = Enemies.MODID, version = Enemies.VERSION)
public class Enemies
{
    public static final String MODID = "enemies";
    public static final String VERSION = "1.0";
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        
        ClientCommandHandler.instance.registerCommand(new CommandEnemy());
        ClientCommandHandler.instance.registerCommand(new CommandEL());
    }
    
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent e)
    {
        try
        {
            loadEnemies();
        }
        catch (FileNotFoundException fileNotFoundException)
        {
            fileNotFoundException.printStackTrace();
        }
    }
    
    public static Map<String, List<String>> enemiesMap = new HashMap<>();
    
    public static void loadEnemies() throws FileNotFoundException
    {
        File enemiesFile = new File("./config/enemies.json");
        
        String fileContents;
        if (enemiesFile.exists() && !enemiesFile.isDirectory())
        {
            Scanner scanner = new Scanner(enemiesFile);
            fileContents = scanner.useDelimiter("\\A").next();
            scanner.close();
            
            JSONObject fullJSONObject = new JSONObject(fileContents);
            
            for (String accountUUID : fullJSONObject.keySet())
            {
                JSONArray list = fullJSONObject.getJSONArray(accountUUID);
                
                List<String> tempList = new ArrayList<>();
                for (Object object : list.toList())
                {
                    tempList.add((String) object);
                }
                
                enemiesMap.put(accountUUID, tempList);
            }
        }
        else
        {
            try
            {
                enemiesFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void saveEnemies()
    {
        File enemiesFile = new File("./config/enemies.json");
        
        if (!enemiesFile.exists() || enemiesFile.isDirectory())
        {
            try
            {
                enemiesFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        try
        {
            FileWriter fileWriter = new FileWriter(enemiesFile);
            JSONObject mapJSON = new JSONObject(enemiesMap);
            fileWriter.write(mapJSON.toString());
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static String getRandomInsult()
    {
        List<String> insultList = Arrays.asList(" deserves the worst", " is your mortal enemy", " betrayed you for the last time", " better watch their back", "'s time is limited", " shouldn't have picked a fight");
        Random random = new Random();
        return insultList.get(random.nextInt(insultList.size()));
    }
    
}
