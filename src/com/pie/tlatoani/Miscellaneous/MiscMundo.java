package com.pie.tlatoani.Miscellaneous;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.util.Slot;
import ch.njol.skript.util.Timespan;
import com.pie.tlatoani.Miscellaneous.ArmorStand.*;
import com.pie.tlatoani.Miscellaneous.Hanging.*;
import com.pie.tlatoani.Miscellaneous.Matcher.*;
import com.pie.tlatoani.Miscellaneous.MiscBukkit.*;
import com.pie.tlatoani.Miscellaneous.Random.*;
import com.pie.tlatoani.Miscellaneous.ServerListPing.*;
import com.pie.tlatoani.Miscellaneous.TabCompletion.*;
import com.pie.tlatoani.Miscellaneous.Thread.*;
import com.pie.tlatoani.Util.*;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Created by Tlatoani on 8/8/17.
 */
public class MiscMundo {
    
    public static void load() {
        //Allow MundoSK 'conditions' to work in absence of SkQuery, which provides a condition like the below
        if (!MundoUtil.serverHasPlugin("SkQuery")) {
            Registration.registerCondition(CondBoolean.class, "%boolean%");
        }

        Registration.registerExpression(ExprReturnTypeOfFunction.class,ClassInfo.class,ExpressionType.PROPERTY,"return type of function %string%");
        Registration.registerExpression(ExprLoadedScripts.class,String.class,ExpressionType.SIMPLE, "loaded script[ name]s");
        Registration.registerExpression(ExprAllTypes.class, ClassInfo.class, ExpressionType.SIMPLE, "all types");
        Registration.registerExpression(ExprThatAre.class, Object.class, ExpressionType.COMBINED, "%objects% that are %object%");
        Registration.registerExpression(ExprNumber.class, Number.class, ExpressionType.PROPERTY, "%*number%[ ](0¦b|1¦d|2¦f|3¦s|4¦l)");
        Registration.registerScope(ScopeWhen.class, "when %boolean%");
        Registration.registerExpression(ExprLoopWhile.class,Object.class,ExpressionType.PROPERTY,"%objects% (0¦while|1¦until|2¦if|3¦unless) %boolean%");
        Registration.registerExpression(ExprTreeOfListVariable.class, Object.class, ExpressionType.PROPERTY, "tree of %objects%");
        Registration.registerExpression(ExprIndexesOfListVariable.class, String.class, ExpressionType.PROPERTY, "[all [of]] [the] indexes (of|in) [value] %objects%");
        Registration.registerExpression(ExprBranch.class, String.class, ExpressionType.PROPERTY, "branch");

        loadArmorStand();
        loadHanging();
        loadMatcher();
        loadMiscBukkit();
        loadRandom();
        loadServerListPing();
        loadTabCompletion();
        loadThread();
    }
    
    private static void loadArmorStand() {
        Registration.registerEvent("Armor Stand Interact Event", SimpleEvent.class, PlayerArmorStandManipulateEvent.class, "armor stand (manipulate|interact)");
        Registration.registerEventValue(PlayerArmorStandManipulateEvent.class, ItemStack.class, PlayerArmorStandManipulateEvent::getArmorStandItem);
        Registration.registerEventValue(PlayerArmorStandManipulateEvent.class, Slot.class, e ->
                new ArmorStandEquipmentSlot(e.getRightClicked(), ArmorStandEquipmentSlot.EquipSlot.getByEquipmentSlot(e.getSlot())));
        Registration.registerEvent("Armor Stand Place Event", EvtArmorStandPlace.class, EntitySpawnEvent.class, "armor stand place");
    }

    private static void loadHanging() {
        Registration.registerEvent("Hang Event", SimpleEvent.class, HangingPlaceEvent.class, "hang");
        Registration.registerEventValue(HangingPlaceEvent.class, Block.class, HangingPlaceEvent::getBlock);
        Registration.registerEvent("Unhang Event", EvtUnhang.class, HangingBreakEvent.class, "unhang [due to %-hangingremovecauses%]");
        Registration.registerEventValue(HangingBreakByEntityEvent.class, Entity.class, HangingBreakByEntityEvent::getRemover);
        Registration.registerEventValue(HangingBreakEvent.class, HangingBreakEvent.RemoveCause.class, HangingBreakEvent::getCause);
        Registration.registerExpression(ExprHangedEntity.class,Entity.class, ExpressionType.SIMPLE,"hanged entity");
    }
    
    private static void loadMatcher() {
        Registration.registerScope(ScopeMatcher.class, "(switch|match) %object%");
        Registration.registerScope(ScopeMatches.class, "(case|matches) %object%");
    }
    
    private static void loadMiscBukkit() {
        Registration.registerEnum(Difficulty.class, "difficulty", Difficulty.values());
        Registration.registerEnum(PlayerLoginEvent.Result.class, "playerloginresult", PlayerLoginEvent.Result.values ());
        Registration.registerEnum(HangingBreakEvent.RemoveCause.class, "hangingremovecause", HangingBreakEvent.RemoveCause.values());
        Registration.registerEffect(EffWait.class, "[(2¦async)] wait (0¦until|1¦while) %boolean% [for %-timespan%]");
        if (Reflection.methodExists(Entity.class, "addPassenger", Entity.class)) {
            Registration.registerEffect(EffMountVehicle.class, "mount %entities% on %entity%");
        }
        Registration.registerExpression(ExprWorldString.class,World.class,ExpressionType.PROPERTY,"world %string%");
        Registration.registerExpression(ExprHighestSolidBlock.class,Block.class,ExpressionType.PROPERTY,"highest [(solid|non-air)] block at %location%");
        Registration.registerExpression(ExprDifficulty.class,Difficulty.class,ExpressionType.PROPERTY,"difficulty of %world%");
        Registration.registerExpression(ExprGameRule.class,String.class,ExpressionType.PROPERTY,"value of [game]rule %string% in %world%");
        Registration.registerExpression(ExprRemainingAir.class,Timespan.class,ExpressionType.PROPERTY,"breath of %livingentity%", "%livingentity%'s breath", "max breath of %livingentity%", "%livingentity%'s max breath");
        Registration.registerExpression(ExprLoginResult.class, PlayerLoginEvent.Result.class, ExpressionType.SIMPLE, "(login|connect[ion]) result");
        Registration.registerExpression(ExprServerIP.class, String.class, ExpressionType.SIMPLE, "[mundo[sk]] [the] ip of server", "[mundo[sk]] [the] server's ip");
        Registration.registerExpression(ExprServerPort.class, Number.class, ExpressionType.SIMPLE, "[mundo[sk]] [the] port of server", "[mundo[sk]] [the] server's port");
        Registration.registerExpression(ExprEntityCanCollide.class, Boolean.class, ExpressionType.PROPERTY, "%livingentity% is collidable");
        Registration.registerExpression(ExprTreeAtLoc.class, Block.class, ExpressionType.PROPERTY, "tree at %location%");
        Registration.registerExpression(ExprRespawnLocation.class, Location.class, ExpressionType.SIMPLE, "respawn location");
        Registration.registerExpression(ExprDestination.class, Location.class, ExpressionType.SIMPLE, "destination");
        Registration.registerExpression(ExprNewPortal.class, Location.class, ExpressionType.PROPERTY, "new nether portal within [[a] radius of] %number% (block|meter)s of %location%");
        Registration.registerExpression(ExprFlying.class, Boolean.class, ExpressionType.PROPERTY, "[%player% is] flying");
    }
    
    private static void loadRandom() {
        Registration.registerExpression(ExprNewRandom.class, Random.class, ExpressionType.PROPERTY, "new random [from seed %number%]");
        Registration.registerExpression(ExprRandomValue.class, Object.class, ExpressionType.PROPERTY, "random (0¦int|1¦long|2¦float|3¦double|4¦gaussian|5¦int less than %-number%|6¦boolean) [from [random] %random%]");
    }
    
    private static void loadServerListPing() {
        Registration.registerEvent("Server List Ping", SimpleEvent.class, ServerListPingEvent.class, "[[(server|player)] list] ping");
        Registration.registerExpression(ExprAmountOfPlayers.class, Number.class, ExpressionType.SIMPLE, "(shown|sent) (0¦amount of|1¦max [amount of]) players");
        Registration.registerExpression(ExprMotd.class, String.class, ExpressionType.SIMPLE, "(shown|sent) motd");
        Registration.registerExpression(ExprIP.class, String.class, ExpressionType.SIMPLE, "pinger's ip");
    }
    
    private static void loadTabCompletion() {
        Registration.registerEvent("Chat Tab Complete Event", SimpleEvent.class, PlayerChatTabCompleteEvent.class, "chat tab complete");
        Registration.registerEventValue(PlayerChatTabCompleteEvent.class, String.class, PlayerChatTabCompleteEvent::getChatMessage);
        if (Reflection.classExists("org.bukkit.event.server.TabCompleteEvent")) {
            Registration.registerEvent("Tab Complete Event", SimpleEvent.class, TabCompleteEvent.class, "tab complete");
            Registration.registerEventValue(TabCompleteEvent.class, String.class, TabCompleteEvent::getBuffer);
            Registration.registerExpression(ExprCompletions.class,String.class,ExpressionType.SIMPLE,"completions");
            Registration.registerExpression(ExprLastToken.class, String.class, ExpressionType.SIMPLE, "last token");
        } else {
            Registration.registerExpression(ExprCompletionsOld.class,String.class,ExpressionType.SIMPLE,"completions");
            Registration.registerExpression(ExprLastTokenOld.class, String.class, ExpressionType.SIMPLE, "last token");
        }
    }
    
    private static void loadThread() {
        Registration.registerEffect(EffWaitAsync.class, "async wait %timespan%");
        Registration.registerScope(ScopeAsync.class, "async [in %-timespan%]");
        Registration.registerScope(ScopeSync.class, "(sync|in %-timespan%)");
    }

}
