package com.rogoshum.magickcore.common.init;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.common.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MagickCore.MOD_ID);
    public static RegistryObject<TileEntityType<MagickCraftingTileEntity>> magick_crafting_tileentity = TILE_ENTITY.register("magick_crafting_tileentity"
            , () -> TileEntityType.Builder.create(MagickCraftingTileEntity::new
                    , ModBlocks.magick_crafting.get()).build(null));

    public static RegistryObject<TileEntityType<SpiritCrystalTileEntity>> spirit_crystal_tileentity = TILE_ENTITY.register("spirit_crystal_tileentity"
            , () -> TileEntityType.Builder.create(SpiritCrystalTileEntity::new
                    , ModBlocks.spirit_crystal.get()).build(null));
    public static RegistryObject<TileEntityType<ElementCrystalTileEntity>> element_crystal_tileentity = TILE_ENTITY.register("element_crystal_tileentity"
            , () -> TileEntityType.Builder.create(ElementCrystalTileEntity::new
                    , ModBlocks.element_crystal.get()).build(null));

    public static RegistryObject<TileEntityType<ElementWoolTileEntity>> element_wool_tileentity = TILE_ENTITY.register("element_wool"
            , () -> TileEntityType.Builder.create(ElementWoolTileEntity::new
                    , ModBlocks.element_wool.get()).build(null));
    public static RegistryObject<TileEntityType<GlowAirTileEntity>> GLOW_AIR_TILE_ENTITY = TILE_ENTITY.register("glow_air_tile_entity"
            , () -> TileEntityType.Builder.create(GlowAirTileEntity::new
                    , ModBlocks.fake_air.get(), ModBlocks.fake_cave_air.get(), ModBlocks.fake_water.get()).build(null));

    public static RegistryObject<TileEntityType<MaterialJarTileEntity>> MATERIAL_JAR_TILE_ENTITY = TILE_ENTITY.register("material_jar_tile_entity"
            , () -> TileEntityType.Builder.create(MaterialJarTileEntity::new
                    , ModBlocks.MATERIAL_JAR.get()).build(null));
}
