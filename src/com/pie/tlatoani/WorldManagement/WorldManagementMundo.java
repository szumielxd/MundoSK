package com.pie.tlatoani.WorldManagement;

import ch.njol.skript.classes.Converter;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.registrations.Converters;
import com.pie.tlatoani.Registration.Registration;
import com.pie.tlatoani.WorldCreator.WorldCreatorData;
import com.pie.tlatoani.WorldManagement.WorldLoader.*;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Created by Tlatoani on 8/8/17.
 */
public class WorldManagementMundo {
    
    public static void load() {
        Converters.registerConverter(World.class, WorldCreator.class, new Converter<World, WorldCreator>() {
            @Override
            public WorldCreator convert(World world) {
                WorldCreator worldCreator = new WorldCreator(world.getName());
                worldCreator.copy(world);
                worldCreator.type(world.getWorldType());
                worldCreator.generateStructures(world.canGenerateStructures());
                worldCreator.generatorSettings("");
                return worldCreator;
            }
        });
        Registration.registerEffect(EffCreateWorld.class, "create [new] world named %string%[( with|,)] [(dim[ension]|env[ironment]) %-dimension%][,] [seed %-string%][,] [type %-worldtype%][,] [gen[erator] %-string%][,] [gen[erator] settings %-string%][,] [struct[ures] %-boolean%]")
                .document("Create World", "1.8", "Creates a world with the specified name, optionally with a few settings. "
                        + "See the environment type and worldtype type for valid environments and worldtypes respectively. "
                        + "Generator settings can either be custom superflat codes or customized world codes (for customized world codes the worldtype needs to be 'customized').");
        Registration.registerEffect(EffCreateWorldUsingCreator.class, "create [new] world [named %-string%] using %creator%")
                .document("Create World Using Creator", "1.8", "Creates a world using the specified creator, optionally specifying the world's name (this is required if the creator doesn't specify a name)."
                        + "See the creator expressions for more information on how to specify the world's name and other settings. "
                        + "If a world with the name (specified or from the creator) already exists, this will just load that world instead of creating a new one.");
        Registration.registerEffect(EffUnloadWorld.class, "unload %world% [save %-boolean%]")
                .document("Unload World", "1.8", "Unloads the specified world. You can specify whether or not to save before unloading (this defaults to true).");
        Registration.registerEffect(EffDeleteWorld.class, "delete %world%")
                .document("Delete World", "1.8", "Deletes the specified world. The specified world must be loaded in order to be deleted.");
        Registration.registerEffect(EffDuplicateWorld.class, "duplicate %world% (with|using) name %string%")
                .document("Duplicate World", "1.8", "Creates a copy of the specified world using the specified string as a name. The specified world must be loaded in order for this to work.");
        Registration.registerExpression(ExprCurrentWorlds.class,World.class, ExpressionType.SIMPLE,"[all] current worlds")
                .document("All Current Worlds", "1.8", "An expression for all worlds that are currently loaded. "
                        + "This differs from Skript's 'all worlds' expression in that it still parses as being a list even if there is only one world at the time of parsing.");

        loadWorldLoader();
    }
    
    private static void loadWorldLoader() {
        Registration.registerEffect(EffLoadWorldAutomatically.class, "[(1¦don't|1¦do not)] load %world% automatically")
                .document("Load World Automatically", "1.8", "Tells MundoSK whether it should load the specified world automatically on server start. "
                        + "This is useful for simple and straightforward world management without the need for a world management plugin. "
                        + "Don't run this effect with the main world, as Bukkit will already load that world automatically, and this effect can't be used to enable/disable that behavior.");
        Registration.registerExpression(ExprAllAutomaticCreators.class, WorldCreatorData.class, ExpressionType.SIMPLE, "[all] automatic creators")
                .document("All Automatic Creators", "1.8", "An expression for all of the world creators that MundoSK is currently set to automatically run on server start. "
                        + "This expression can be added to (world creators), removed from (world names or world creators), or cleared in order to specify that certain worlds should/shouldn't be loaded automatically.");
        Registration.registerPropertyExpression(ExprAutomaticCreator.class, WorldCreatorData.class, "string", "automatic creator %", "automatic creator for world %", "automatic creator for world named %")
                .document("Automatic Creator", "1.8", "An expression for the automatic creator (if there is one) that MundoSK is currently set to run for the world with the specified name. "
                        + "This expression can be set in order to specify an automatic creator for the world, or cleared/deleted in order to specify that the world should not be loaded automatically.");
    }
}
