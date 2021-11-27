package com.pie.tlatoani.Generator;

import org.bukkit.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

/**
 * Created by Tlatoani on 7/26/16.
 */
public class ChunkGeneratorWithID extends ChunkGenerator {
    public final String id;
    public final ChunkGenerator wrappedGenerator;

    private ChunkGeneratorWithID(String id, ChunkGenerator wrappedGenerator) {
        this.id = id;
        this.wrappedGenerator = wrappedGenerator;
    }

    public static ChunkGeneratorWithID getGenerator(String id) {
        WorldCreator worldCreator = new WorldCreator("util");
        ChunkGenerator generator = worldCreator.generator(id).generator();
        return generator == null ? null : new ChunkGeneratorWithID(id, generator);
    }

    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biome) {
        return wrappedGenerator.generateChunkData(world, random, x, z, biome);
    }

    @Deprecated
    public byte[] generate(World world, Random random, int x, int z) {
        try {
			return (byte[]) wrappedGenerator.getClass().getMethod("generate", World.class, Random.class, Integer.TYPE, Integer.TYPE).invoke(wrappedGenerator, world, random, x, z);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
    }

    @Deprecated
    public short[][] generateExtBlockSections(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        try {
			return (short[][]) wrappedGenerator.getClass().getMethod("generateExtBlockSections", World.class, Random.class, Integer.TYPE, Integer.TYPE, ChunkGenerator.BiomeGrid.class).invoke(wrappedGenerator, world, random, x, z, biomes);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
    }

    @Deprecated
    public byte[][] generateBlockSections(World world, Random random, int x, int z, ChunkGenerator.BiomeGrid biomes) {
        try {
			return (byte[][]) wrappedGenerator.getClass().getMethod("generateBlockSections", World.class, Random.class, Integer.TYPE, Integer.TYPE, ChunkGenerator.BiomeGrid.class).invoke(wrappedGenerator, world, random, x, z, biomes);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return wrappedGenerator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return wrappedGenerator.getDefaultPopulators(world);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return wrappedGenerator.getFixedSpawnLocation(world, random);
    }
}
