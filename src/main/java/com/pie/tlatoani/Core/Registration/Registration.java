package com.pie.tlatoani.Core.Registration;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.*;
import ch.njol.skript.classes.Comparator;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.*;
import ch.njol.skript.util.Getter;
import com.pie.tlatoani.Core.Skript.CustomScope;
import com.pie.tlatoani.Util.Skript.EventSpecificExpression;
import com.pie.tlatoani.Core.Skript.MundoPropertyExpression;
import com.pie.tlatoani.Core.Static.Logging;
import org.bukkit.event.Event;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Tlatoani on 8/9/17.
 */
public final class Registration {

    private static String currentCategory = null;
    private static String[] currentRequiredPlugins = new String[0];

    public static void register(String category, Runnable registerer, String... requiredPlugins) {
        String prevCategory = currentCategory;
        String[] prevPlugins = currentRequiredPlugins;
        currentCategory = category;
        currentRequiredPlugins = requiredPlugins;
        registerer.run();
        currentCategory = prevCategory;
        currentRequiredPlugins = prevPlugins;
    }

    public static String getCurrentCategory() {
        return currentCategory;
    }

    public static String[] getCurrentRequiredPlugins() {
        return currentRequiredPlugins;
    }

    public static DocumentationBuilder.Effect registerEffect(Class<? extends Effect> effectClass, String... patterns) {
        Skript.registerEffect(effectClass, patterns);
        return new DocumentationBuilder.Effect(currentCategory, patterns).requiredPlugins(currentRequiredPlugins);
    }

    public static <T> DocumentationBuilder.Expression registerExpression(Class<? extends Expression<T>> expressionClass, Class<T> type, ExpressionType expressionType, String... patterns) {
        Skript.registerExpression(expressionClass, type, expressionType, patterns);
        return new DocumentationBuilder.Expression(currentCategory, patterns, type, expressionClass).requiredPlugins(currentRequiredPlugins);
    }

    public static DocumentationBuilder.Condition registerExpressionCondition(Class<? extends Expression<Boolean>> expressionClass, ExpressionType expressionType, String... patterns) {
        Skript.registerExpression(expressionClass, Boolean.class, expressionType, patterns);
        return new DocumentationBuilder.Condition(currentCategory, patterns, expressionClass).requiredPlugins(currentRequiredPlugins);
    }

    public static <T> DocumentationBuilder.Expression registerPropertyExpression(Class<? extends Expression<T>> expressionClass, Class<T> type, String possessorType, String... properties) {
        ArrayList<String> patternList = new ArrayList<>(properties.length);
        ArrayList<String> propertyList = new ArrayList<>(properties.length);
        for (int i = 0; i < properties.length; i++) {
            String property = properties[i];
            if (property.contains("%")) {
                patternList.add(property.replace("%", "%" + possessorType + "%"));
                propertyList.add(property);
            } else {
                patternList.add("[the] " + property + " of %" + possessorType + "%");
                patternList.add("%" + possessorType + "%'[s] " + property);
                propertyList.add(property);
                propertyList.add(property);
            }
        }
        String[] patterns = patternList.toArray(new String[0]);
        Skript.registerExpression(expressionClass, type, ExpressionType.PROPERTY, patterns);
        if (MundoPropertyExpression.class.isAssignableFrom(expressionClass)) {
            MundoPropertyExpression.registerPropertyExpressionInfo((Class<? extends MundoPropertyExpression>) expressionClass, type, propertyList);
        }
        return new DocumentationBuilder.Expression(currentCategory, patterns, type, expressionClass).requiredPlugins(currentRequiredPlugins);
    }

    public static DocumentationBuilder.Condition registerPropertyExpressionCondition(Class<? extends Expression<Boolean>> expressionClass, String possessorType, String... properties) {
        ArrayList<String> patternList = new ArrayList<>(properties.length);
        ArrayList<String> propertyList = new ArrayList<>(properties.length);
        for (int i = 0; i < properties.length; i++) {
            String property = properties[i];
            if (property.contains("%")) {
                patternList.add(property.replace("%", "%" + possessorType + "%"));
                propertyList.add(property);
            } else {
                patternList.add("[the] " + property + " of %" + possessorType + "%");
                patternList.add("%" + possessorType + "%'[s] " + property);
                propertyList.add(property);
                propertyList.add(property);
            }
        }
        String[] patterns = patternList.toArray(new String[0]);
        Skript.registerExpression(expressionClass, Boolean.class, ExpressionType.PROPERTY, patterns);
        if (MundoPropertyExpression.class.isAssignableFrom(expressionClass)) {
            MundoPropertyExpression.registerPropertyExpressionInfo((Class<? extends MundoPropertyExpression>) expressionClass, Boolean.class, propertyList);
        }
        return new DocumentationBuilder.Condition(currentCategory, patterns, expressionClass).requiredPlugins(currentRequiredPlugins);
    }

    /*public static <T, E extends Event> DocumentationBuilder.Expression registerEventSpecificExpression(Class<? extends EventSpecificExpression<T, E>> expressionClass, Class<T> type, Class<E> event, String invalidEventError, String... patterns) {
        Skript.registerExpression(expressionClass, type, ExpressionType.SIMPLE, patterns);
        EventSpecificExpression.registerEventSpecificExpression(expressionClass, type, event, patterns[0], invalidEventError);
        return new DocumentationBuilder.Expression(currentCategory, patterns, type, expressionClass).requiredPlugins(currentRequiredPlugins);
    }

    public static <E extends Event> DocumentationBuilder.Condition registerEventSpecificExpressionCondition(Class<? extends EventSpecificExpression<Boolean, E>> expressionClass, Class<E> event, String invalidEventError, String... patterns) {
        Skript.registerExpression(expressionClass, Boolean.class, ExpressionType.SIMPLE, patterns);
        EventSpecificExpression.registerEventSpecificExpression(expressionClass, Boolean.class, event, patterns[0], invalidEventError);
        return new DocumentationBuilder.Condition(currentCategory, patterns, expressionClass).requiredPlugins(currentRequiredPlugins);
    }*/

    public static void registerCondition(Class<? extends Condition> conditionClass, String... patterns) {
        Skript.registerCondition(conditionClass, patterns);
    }

    public static DocumentationBuilder.Event registerEvent(String name, Class<? extends SkriptEvent> eventClass, Class<? extends Event> eventType, String... patterns) {
        Skript.registerEvent(name, eventClass, eventType, patterns);
        return new DocumentationBuilder.Event(currentCategory, patterns, eventType).requiredPlugins(currentRequiredPlugins);
    }

    public static DocumentationBuilder.Scope registerScope(Class<? extends CustomScope> conditionClass, String... patterns) {
        Skript.registerCondition(conditionClass, patterns);
        return new DocumentationBuilder.Scope(currentCategory, patterns).requiredPlugins(currentRequiredPlugins);
    }

    public static <E extends Event, R> void registerEventValue(Class<E> tClass, Class<R> rClass, Function<E, R> function) {
        EventValues.registerEventValue(tClass, rClass, new Getter<R, E>() {
            @Override
            public R get(E event) {
                try {
                    return function.apply(event);
                } catch (ClassCastException e) {
                    Logging.debug(Registration.class, "tClass = " + tClass + ", rClass = " + rClass + ", function = " + function);
                    Logging.debug(Registration.class, e);
                    return null;
                }
            }
        }, 0);
    }

    public static <A, B> void registerComparator(Class<A> aClass, Class<B> bClass, boolean supportsOrdering, BiFunction<A, B, Comparator.Relation> comparator) {
        Comparators.registerComparator(aClass, bClass, new Comparator<A, B>() {
            @Override
            public Relation compare(A a, B b) {
                return comparator.apply(a, b);
            }

            @Override
            public boolean supportsOrdering() {
                return supportsOrdering;
            }
        });
    }

    public static <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> function) {
        Converters.registerConverter(from, to, (Converter<F, T>) function::apply);
    }

    public static Boolean classInfoSafe(Class c, String name) {
        return Classes.getExactClassInfo(c) == null && Classes.getClassInfoNoError(name) == null;
    }

    public static <T> MundoClassInfo<T> registerType(Class<T> type, String name, String... alternateNames) {
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(alternateNames));
        names.add(0, name);
        MundoClassInfo<T> result = new MundoClassInfo<T>(type, names.toArray(new String[0]), currentCategory);
        result.parser(new SimpleParser<T>() {
            @Override
            public T parse(String s, ParseContext parseContext) {
                return null;
            }
        });
        if (classInfoSafe(type, name)) {
            Classes.registerClass(result);
        }
        return result.requiredPlugins(currentRequiredPlugins);
    }

    public static <E> EnumClassInfo<E> registerEnum(Class<E> enumClass, String name, E... values) {
        EnumClassInfo<E> enumClassInfo = new EnumClassInfo<E>(enumClass, new String[]{name}, currentCategory, values);
        if (classInfoSafe(enumClass, name)) {
            Classes.registerClass(enumClassInfo);
            ExprEnumValues.addEnumClassInfo(enumClassInfo);
        }
        return enumClassInfo.requiredPlugins(currentRequiredPlugins);
    }

    //Keys should be UPPERCASE
    public static <E> EnumClassInfo<E> registerEnum(Class<E> enumClass, String name, Map<String, E> valueMap) {
        EnumClassInfo<E> enumClassInfo = new EnumClassInfo<E>(enumClass, new String[]{name}, currentCategory, valueMap);
        if (classInfoSafe(enumClass, name)) {
            Classes.registerClass(enumClassInfo);
            ExprEnumValues.addEnumClassInfo(enumClassInfo);
        }
        return enumClassInfo.requiredPlugins(currentRequiredPlugins);
    }

    public static abstract class SimpleParser<T> extends Parser<T> {

        @Override
        public String toString(T t, int flags) {
            Logging.debug(Registration.class, "toString() for " + t + "; flags: " + flags);
            return t.toString();
        }

        @Override
        public String toVariableNameString(T t) {
            return toString(t, 0);
        }

        @Override
        public String getVariableNamePattern() {
            return ".+";
        }
    }
}
