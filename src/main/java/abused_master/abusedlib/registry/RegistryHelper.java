package abused_master.abusedlib.registry;

import abused_master.abusedlib.blocks.BlockBase;
import abused_master.abusedlib.items.ItemBase;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.GenerationStep;
//import net.minecraft.world.gen.decorator.Decorator;
//import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RegistryHelper {

    /**
     * Blocks and Item registry Helpers
     */
    public static void registerBlock(String modid, BlockBase block) {
        Registry.register(Registry.BLOCK, block.getNameIdentifier(modid), block);
        Registry.register(Registry.ITEM, block.getNameIdentifier(modid), new BlockItem(block, new Item.Settings().group(block.getTab())));
    }

    public static void registerBlock(String modid, BlockBase block, BlockItem blockItem) {
        Registry.register(Registry.BLOCK, block.getNameIdentifier(modid), block);
        Registry.register(Registry.ITEM, block.getNameIdentifier(modid), blockItem);
    }

    public static void registerBlock(Identifier identifier, ItemGroup itemGroup, Block block) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, new BlockItem(block, new Item.Settings().group(itemGroup)));
    }

    public static void registerBlock(Identifier identifier, Block block, BlockItem blockItem) {
        Registry.register(Registry.BLOCK, identifier, block);
        Registry.register(Registry.ITEM, identifier, blockItem);
    }

    public static void registerItem(String modid, ItemBase item) {
        Registry.register(Registry.ITEM, item.getNameIdentifier(modid), item);
    }

    public static void registerItem(Identifier identifier, Item item) {
        Registry.register(Registry.ITEM, identifier, item);
    }

    /**
     * Tile entity registry
     * EX: BlockEntityType<BlockEntityTest> BlockEntityTest = registerTile(new Identifier(MODID, NAME), BlockEntityTest.class);
     */
    public static BlockEntityType registerTile(Identifier identifier, Class<? extends BlockEntity> blockEntity, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, FabricBlockEntityTypeBuilder.create((FabricBlockEntityTypeBuilder.Factory<? extends BlockEntity>)(Supplier<BlockEntity>) () -> {
            try {
                return blockEntity.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, blocks).build(null));
    }

    /**
     * World Gen Ore Registry
     */
    public static void generateOreInStone(Block block, int veinSize, int spawnRate, int maxHeight) {
        generateOreInStone(new Identifier("abusedlib", Registry.ITEM.getId(block.asItem()).getPath() + "_ore"), block, veinSize, spawnRate, maxHeight);

    }
    public static void generateOreInStone(Identifier identifier, Block block, int veinSize, int spawnRate, int maxHeight) {
        ConfiguredFeature<?, ?> CONFIGURED_FEATURE = new ConfiguredFeature(Feature.ORE,
                new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, block.getDefaultState(), veinSize));

        PlacedFeature PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(CONFIGURED_FEATURE),
                Arrays.asList(CountPlacementModifier.of(spawnRate), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(maxHeight))));

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                identifier, CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier,
                PLACED_FEATURE);

        Predicate<BiomeSelectionContext> predicate = BiomeSelectors.all();
        BiomeModifications.addFeature(predicate, GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, identifier));
    }

    public static void generateOre(Block block, OreFeatureConfig.Target target, int veinSize, int spawnRate, int maxHeight) {
        generateOre(new Identifier("abusedlib", Registry.ITEM.getId(block.asItem()).getPath() + "_ore"), block, target, veinSize, spawnRate, maxHeight);
    }

    public static void generateOre(Identifier identifier, Block block, OreFeatureConfig.Target target, int veinSize, int spawnRate, int maxHeight) {
        ConfiguredFeature<?, ?> CONFIGURED_FEATURE = new ConfiguredFeature(Feature.ORE,
                new OreFeatureConfig(target.target, block.getDefaultState(), veinSize));

        PlacedFeature PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(CONFIGURED_FEATURE),
                Arrays.asList(CountPlacementModifier.of(spawnRate), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(maxHeight))));

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                identifier, CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier,
                PLACED_FEATURE);

        Predicate<BiomeSelectionContext> predicate = BiomeSelectors.all();
        BiomeModifications.addFeature(predicate, GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, identifier));
    }

    public static void generateOreInStone(Biome biome, Block block, int veinSize, int spawnRate, int maxHeight) {
        generateOreInStone(new Identifier("abusedlib", Registry.ITEM.getId(block.asItem()).getPath() + "_ore"), biome, block, veinSize, spawnRate, maxHeight);
    }

    public static void generateOreInStone(Identifier identifier, Biome biome, Block block, int veinSize, int spawnRate, int maxHeight) {
        ConfiguredFeature<?, ?> CONFIGURED_FEATURE = new ConfiguredFeature(Feature.ORE,
                new OreFeatureConfig(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, block.getDefaultState(), veinSize));

        PlacedFeature PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(CONFIGURED_FEATURE),
                Arrays.asList(CountPlacementModifier.of(spawnRate), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(maxHeight))));

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                identifier, CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier,
                PLACED_FEATURE);

        Predicate<BiomeSelectionContext> predicate = BiomeSelectors.includeByKey(BuiltinRegistries.BIOME.getKey(biome).get());
        BiomeModifications.addFeature(predicate, GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, identifier));

    }
}
