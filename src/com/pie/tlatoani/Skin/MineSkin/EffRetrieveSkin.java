package com.pie.tlatoani.Skin.MineSkin;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.pie.tlatoani.Core.Static.Scheduling;
import com.pie.tlatoani.Skin.ProfileManager;
import com.pie.tlatoani.Skin.Skin;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import java.util.Arrays;

public class EffRetrieveSkin extends Effect {
    private Variable<?> variable;
    private ExprRetrievedSkin expression;

    @Override
    protected TriggerItem walk(Event event) {
        if (expression.mode == ExprRetrievedSkin.RetrieveMode.OFFLINE_PLAYER) {
            OfflinePlayer offlinePlayer = expression.offlinePlayerExpr.getSingle(event);
            if (offlinePlayer.isOnline()) {
                Skin delta = ProfileManager.getProfile(offlinePlayer.getPlayer()).getActualSkin();
                variable.change(event, new Skin[]{delta}, Changer.ChangeMode.SET);
                return getNext();
            } else {
                Timespan timeout = expression.timeoutExpr.getSingle(event);
                long timeoutMillis = timeout == null ? 0 : timeout.getMilliSeconds();
                Scheduling.async(() -> {
                    Skin delta = PlayerSkinRetrieval.retrieveOfflineSkin(offlinePlayer, (int) timeoutMillis);
                    Scheduling.sync(() -> {
                        variable.change(event, new Skin[]{delta}, Changer.ChangeMode.SET);
                        TriggerItem.walk(getNext(), event);
                    });
                });
                return null;
            }
        } else {
            Scheduling.async(() -> {
                Skin delta = expression.getSkin(event);
                Scheduling.sync(() -> {
                    variable.change(event, new Skin[]{delta}, Changer.ChangeMode.SET);
                    TriggerItem.walk(getNext(), event);
                });
            });
            return null;
        }
    }

    @Override
    protected void execute(Event event) {}

    @Override
    public String toString(Event event, boolean b) {
        return expression.toString(event, b).replace("retrieved", "retrieve") + " into " + variable;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!(expressions[0] instanceof Variable)) {
            Skript.error("The 'retrieve skin' effect can only retrieve into variables!");
            return false;
        }
        variable = (Variable) expressions[expressions.length - 1];
        expression = new ExprRetrievedSkin();
        expression.init(Arrays.copyOfRange(expressions, 0, expressions.length - 1), i, kleenean, parseResult);
        return true;
    }
}
