package com.pie.tlatoani.Tablist;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Created by Tlatoani on 7/13/16.
 */
public class EffDeleteTab extends Effect {
    private Expression<String> id;
    private Expression<Player> playerExpression;

    @Override
    protected void execute(Event event) {
        TabListManager tabListManager;
        if ((tabListManager = TabListManager.getForPlayer(playerExpression.getSingle(event))) != null) {
            tabListManager.deleteTab(id.getSingle(event));
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return "delete tab id " + id + " for " + playerExpression;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        id = (Expression<String>) expressions[0];
        playerExpression = (Expression<Player>) expressions[1];
        return true;
    }
}