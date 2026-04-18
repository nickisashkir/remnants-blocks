# Remnants Building Blocks

Custom Blocks for The Remnants Vanilla Plus SMP server for Java 26.1

A server-side Fabric mod for Minecraft 26.1 that adds new slab types using [Polymer](https://modrinth.com/mod/polymer). No client mod required -- vanilla clients connect and see everything via the server resource pack.

## Blocks

### Natural Slabs
- Dirt Slab
- Coarse Dirt Slab
- Grass Slab (biome tinted -- works with Terralith and other biome mods)
- Podzol Slab

### Stone Slabs
- Amethyst Slab
- Obsidian Slab
- Crying Obsidian Slab

### Concrete Slabs (all 16 colors)
White, Orange, Magenta, Light Blue, Yellow, Lime, Pink, Gray, Light Gray, Cyan, Purple, Blue, Brown, Green, Red, Black

**23 slab types total.**

## How It Works

Each slab maps to an invisible `petrified_oak_slab` for collision and selection. A `BlockDisplayElement` (Polymer virtual entity) renders the correct vanilla block texture at slab scale. For grass slabs, the client applies biome-appropriate tinting to the display entity automatically.

All block models and item definitions are generated programmatically during resource pack creation -- no static model JSONs to maintain.

## Crafting

Standard slab recipe: 3 source blocks in a row = 6 slabs.

## Dependencies

- Minecraft 26.1
- Fabric Loader 0.18.5+
- Fabric API 0.144.3+26.1
- Polymer 0.16.0-pre.3+26.1 (core, resource-pack, virtual-entity, autohost)

## Commands

```
/give @s remnants_blocks:dirt_slab
/give @s remnants_blocks:grass_block_slab
/give @s remnants_blocks:obsidian_slab
/give @s remnants_blocks:white_concrete_slab
```

All IDs follow the pattern `remnants_blocks:<block>_slab`.

## Building

```
./gradlew build
```

JAR output: `build/libs/`

## License

MIT
