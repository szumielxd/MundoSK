package com.pie.tlatoani.Json;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.pie.tlatoani.Json.API.*;
import com.pie.tlatoani.Mundo;
import org.bukkit.event.Event;

import java.util.TreeMap;
import java.util.function.BiConsumer;

/**
 * Created by Tlatoani on 5/7/16.
 */
public class ExprListVariableAsJson extends SimpleExpression<JsonObject> {
    private Variable<?> listVariable;

    private static JsonObject getJsonObject(TreeMap<String, Object> treeMap) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        treeMap.forEach(new BiConsumer<String, Object>() {
            public void accept(String key, Object val) {
                if (val instanceof String) {
                    Mundo.classDebug(ExprListVariableAsJson.class, "String found");
                    Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                    Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                    builder.add(key, (String) val);
                } else if (val instanceof Number) {
                    Mundo.classDebug(ExprListVariableAsJson.class, "Number found");
                    Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                    Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                    builder.add(key, ((Number) val).doubleValue());
                } else if (val instanceof TreeMap) {
                    if (((TreeMap) val).containsKey("1")) {
                        Mundo.classDebug(ExprListVariableAsJson.class, "JSONArray found");
                        Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                        JsonArray valarray = getJsonArray((TreeMap<String, Object>) val);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Polished Val: " + valarray);
                        builder.add(key, valarray);
                    } else {
                        Mundo.classDebug(ExprListVariableAsJson.class, "JSONObject found");
                        Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                        JsonObject valobject = getJsonObject((TreeMap<String, Object>) val);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Polished Val: " + valobject);
                        builder.add(key, valobject);
                    }
                }
            }
        });
        return builder.build();
    }

    private static JsonArray getJsonArray(TreeMap<String, Object> treeMap) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        treeMap.forEach(new BiConsumer<String, Object>() {
            public void accept(String key, Object val) {
                if (val instanceof String) {
                    Mundo.classDebug(ExprListVariableAsJson.class, "String found");
                    Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                    Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                    builder.add((String) val);
                } else if (val instanceof Number) {
                    Mundo.classDebug(ExprListVariableAsJson.class, "Number found");
                    Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                    Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                    builder.add(((Number) val).doubleValue());
                } else if (val instanceof TreeMap) {
                    if (((TreeMap) val).containsKey("1")) {
                        Mundo.classDebug(ExprListVariableAsJson.class, "JSONArray found");
                        Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                        JsonArray valarray = getJsonArray((TreeMap<String, Object>) val);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Polished Val: " + valarray);
                        builder.add(valarray);
                    } else {
                        Mundo.classDebug(ExprListVariableAsJson.class, "JSONObject found");
                        Mundo.classDebug(ExprListVariableAsJson.class, "Key: " + key);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Value: " + val);
                        JsonObject valobject = getJsonObject((TreeMap<String, Object>) val);
                        Mundo.classDebug(ExprListVariableAsJson.class, "Polished Val: " + valobject);
                        builder.add(valobject);
                    }
                }
            }
        });
        return builder.build();
    }

    @Override
    protected JsonObject[] get(Event event) {
        TreeMap<String, Object> treeMap = (TreeMap) Variables.getVariable(listVariable.isLocal() ? listVariable.toString().substring(2, listVariable.toString().length() - 1) : listVariable.toString().substring(1, listVariable.toString().length() - 1), event, listVariable.isLocal());
        JsonObject result = getJsonObject(treeMap);
        Mundo.debug(this, "Final Json: " + result);
        return new JsonObject[] {result};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends JsonObject> getReturnType() {
        return JsonObject.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "list variable %objects% as json";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        Mundo.debug(this, "Expression class: " + exprs[0].getClass());
        Mundo.debug(this, "Return type: " + exprs[0].getReturnType());
        if (exprs[1] instanceof Variable && ((Variable) exprs[0]).isList()) {
            listVariable = (Variable) exprs[0];
            return true;
        }
        Skript.error("'list variable %objects% as json' must be used with a list variable!");;
        return false;
    }
}
