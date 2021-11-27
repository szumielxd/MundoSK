package com.pie.tlatoani.Miscellaneous.MiscBukkit;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by Tlatoani on 1/3/17.
 */
public class ExprTreeAtLoc extends SimpleExpression<Block> {
    private Expression<Location> locationExpression;
    
    
    private static Optional<Material> LOG = Optional.ofNullable(Material.getMaterial("LOG"));
    private static Optional<Material> LOG_2 = Optional.ofNullable(Material.getMaterial("LOG_2"));
    private static Optional<Material> LEAVES = Optional.ofNullable(Material.getMaterial("LEAVES"));
    private static Optional<Material> LEAVES_2 = Optional.ofNullable(Material.getMaterial("LEAVES_2"));

    public static Collection<Block> treeAt(Location location) {
        Block block = location.getBlock();
        if (!isLog(block.getType())) {
            return Collections.emptyList();
        }
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(block);
        for (int i = 0; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            Block[] adjacentBlocks = {
                    currentBlock.getRelative(1, 0, 0),
                    currentBlock.getRelative(-1, 0, 0),
                    currentBlock.getRelative(0, 1, 0),
                    currentBlock.getRelative(0, -1, 0),
                    currentBlock.getRelative(0, 0, 1),
                    currentBlock.getRelative(0, 0, -1)
            };
            for (Block adjacentBlock : adjacentBlocks) {
                if ((isLog(adjacentBlock.getType()) || isLeaves(adjacentBlock.getType())) && !blocks.contains(adjacentBlock)) {
                    blocks.add(adjacentBlock);
                }
            }
        }
        return blocks;
    }
    
    private static boolean isLog(Material material) {
    	if (LOG.isPresent()) return material == LOG.get() || material == LOG_2.get();
    	switch(material) {
    	case ACACIA_LOG:
    	case BIRCH_LOG:
    	case DARK_OAK_LOG:
    	case JUNGLE_LOG:
    	case OAK_LOG:
    	case SPRUCE_LOG:
    	case STRIPPED_ACACIA_LOG:
    	case STRIPPED_BIRCH_LOG:
    	case STRIPPED_DARK_OAK_LOG:
    	case STRIPPED_JUNGLE_LOG:
    	case STRIPPED_OAK_LOG:
    	case STRIPPED_SPRUCE_LOG:
    	return true;
    	default: return false;
    	}
    }
    
    private static boolean isLeaves(Material material) {
    	if (LEAVES.isPresent()) return material == LEAVES.get() || material == LEAVES_2.get();
    	switch(material) {
    	case ACACIA_LEAVES:
    	case BIRCH_LEAVES:
    	case DARK_OAK_LEAVES:
    	case JUNGLE_LEAVES:
    	case OAK_LEAVES:
    	case SPRUCE_LEAVES:
    	return true;
    	default: return false;
    	}
    }

    @Override
    protected Block[] get(Event event) {
        return treeAt(locationExpression.getSingle(event)).toArray(new Block[0]);
    }

    @Override
    public Iterator<Block> iterator(Event event) {
        return treeAt(locationExpression.getSingle(event)).iterator();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Block> getReturnType() {
        return Block.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "tree at " + locationExpression;
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        locationExpression = (Expression<Location>) expressions[0];
        return true;
    }
}
