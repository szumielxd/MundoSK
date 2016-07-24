package com.pie.tlatoani.ListUtil;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tlatoani on 6/10/16.
 */
public final class ListUtil {
    private final static Map<String, Class<? extends Transformer>> dictionary = new HashMap<String, Class<? extends Transformer>>();
    private final static ArrayList<String> patternlist = new ArrayList<>();

    //Cannot be instantiated
    private ListUtil() {}

    public static void registerTransformer(Class<? extends Transformer> transformer, String... patterns) {
        for (int i = 0; i < patterns.length; i++) {
            dictionary.putIfAbsent(patterns[i], transformer);
            patternlist.add(patterns[i]);
        }
    }

    public static String retrievePattern(int index) {
        return patternlist.get(index);
    }

    public static Transformer retrieveTransformer(String pattern, Expression expression) {
        /*if (expression instanceof PersonalTransformer) {
            Transformer personalTransformer = ((PersonalTransformer) expression).getPersonalTransformer(pattern);
            if (personalTransformer != null ) {
                return personalTransformer;
            }
        }*/
        Transformer transformer = null;
        if (dictionary.containsKey(pattern)) {
            try {
                transformer = dictionary.get(pattern).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        /*if (transformer == null) {
            Skript.error("'" + pattern + (expression != null ? "s of " + expression + "'" : "s'") + " is not a list!");
            return null;
        }*/
        return transformer.init(expression) ? transformer : null;
    }

    /*public static Transformer retrieveTransformerByPlural(String pattern, Expression expression) {
        if (pattern.charAt(pattern.length() - 1) == 's') {
            return retrieveTransformer(pattern.substring(0, pattern.length() - 1), expression);
        }
        Skript.error("'" + pattern + (expression != null ? " of " + expression + "'" : "'") + " is not a list!");
        return null;
    }*/

    public static void register() {
        List<String> insertItem = new ArrayList<>();
        List<String> item = new ArrayList<>();
        List<String> items = new ArrayList<>();
        List<String> someItems = new ArrayList<>();
        List<String> itemCount = new ArrayList<>();
        for (int i = 0; i < patternlist.size(); i++) {
            String j = patternlist.get(i);
            insertItem.add("(add|insert) %objects% (1¦before|0¦after) (" + j + " %-number%|last " + j + ")[ (of|in) %-object/objects%]");
            item.add("(" + j + " %-number%|last " + j + ")[ of %-object/objects%]");
            if (j != "item")
                items.add(j + "s[ of %-object/objects%]");
            someItems.add(j + "s %-number% to (%-number%|last)[ of %-object/objects%]");
            itemCount.add(j + " count[ of %-objects%]");
        }
        Skript.registerEffect(EffInsertItem.class,insertItem.toArray(new String[0]));
        Skript.registerExpression(ExprItem.class,Object.class, ExpressionType.PROPERTY,item.toArray(new String[0]));
        Skript.registerExpression(ExprItems.class,Object.class,ExpressionType.PROPERTY,items.toArray(new String[0]));
        Skript.registerExpression(ExprSomeItems.class,String.class,ExpressionType.PROPERTY,someItems.toArray(new String[0]));
        Skript.registerExpression(ExprItemCount.class,Number.class,ExpressionType.PROPERTY,itemCount.toArray(new String[0]));
    }

    public interface TransformerUser {

        Transformer getTransformer();
    }

    /*public interface PersonalTransformer {

        Transformer getPersonalTransformer(String pattern);
    }*/

    public interface Moveable {

        void move(Event event, Integer movement);

        Boolean isMoveable();
    }
}
