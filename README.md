# Remnants Building Blocks

> **Status: SHELVED**

Custom Blocks for The Remnants Vanilla Plus SMP server for Java 26.1.

This was intended as a server-side Fabric mod for Minecraft 26.1 adding custom slabs using [Polymer](https://modrinth.com/mod/polymer). No client mod required -- vanilla clients connect and see everything via the server resource pack.

## Why this is shelved

After deep research into Polymer's architecture, the surrounding ecosystem (Filament, PolyFactory, Polyposa, Better-Amethyst-Polymerized, Slabee, polymer-qol), and vanilla Minecraft's client rendering pipeline, it became clear this mod cannot ship at the scope we wanted without making compromises we're unwilling to make.

### The core problem

Polymer works by sacrificing unused vanilla block states -- specifically state combinations that are visually identical to some other vanilla state, so the "sacrifice" is invisible. For slabs, Polymer found only **5 such lossless pairs in all of vanilla**:

- 4 cut copper slab variants (each maps to its waxed twin)
- Oak slab (maps to petrified oak slab)

This means Polymer's slab pool has exactly 5 state slots per (type, waterlogged) combination. `requestBlock` refuses to allocate the last slot, effectively capping custom slabs at **4 per orientation** -- a hard ceiling baked into vanilla's block inventory.

### Why we can't just extend the pool

Expanding Polymer's pool requires picking additional vanilla slab types to sacrifice. But unlike the cut copper / waxed copper pairs, no other vanilla slabs have visually-identical twins. Any additional sacrifice = visible change to an existing vanilla block's appearance, which affects anyone who legitimately places that slab.

We don't want to sacrifice any vanilla block that already exists in the game. That rule is non-negotiable.

### Why we're not using display entities

Polymer's `requestEmpty` + `BlockDisplayElement` approach (what Filament's `virtual: true` uses) allows unlimited custom slab types, but costs **one display entity per placed slab**. On megabuilds with thousands of decorative slabs, this meaningfully impacts client FPS on weaker machines. Minecraft's entity systems still cause lag at scale even for "static" display entities, and Mojang has not addressed this for vanilla's rendering pipeline.

We aren't willing to ship a mod that introduces performance problems on megabuilds.

### What Mojang could do to unblock this

- Add new "waxed twin" patterns to more slab types (similar to copper)
- Add a dedicated "custom slab" block type for server mods to target
- Fix display entity performance at scale

None of these are on any announced roadmap.

## Current landscape of options (all rejected)

| Option | Why rejected |
|---|---|
| Ship only 4 slabs | Too little value for the effort |
| Extend Polymer's pool with sacrifices | Violates our no-sacrifice-of-vanilla rule |
| Virtual slabs via display entities | Performance cost on megabuilds |
| Client companion mod (Slabee-style) | Breaks our "no client install" constraint |
| Wait for Polymer pool expansion | Nothing upstream left to attach to -- vanilla doesn't have more redundant slab states for Polymer to harvest |

## What's preserved in this repo

Even though the mod is shelved, the code remains as reference for anyone working on Polymer-based mods in the future:

- `PolymerSlabBlock` using `polymer-blocks` API correctly for 26.1 (with `setId` handling and pool logging)
- `SlabRegistry` demonstrating `modifyBasePolymerItemStack` for correct Jade/tooltip display names
- Programmatic grass texture baking in `ResourcePackGenerator` (RGB multiplication + overlay compositing for Terralith-matched colors)
- `JadePlugin` using Jade's `IServerDataProvider` / `StreamServerDataProvider` API (no client-side Polymer-Jade integration needed)
- Resource pack generation at `RESOURCE_PACK_CREATION_EVENT` for model/item definition JSONs
- Tags (mineable/pickaxe, shovel, slabs, needs_diamond_tool) covering all 51 planned slabs
- Recipe + loot table generation patterns for slabs with stonecutting fallbacks

Intended slab roster (51 total):
- Natural: dirt, coarse dirt, podzol
- Grass variants (12 Terralith-matched colors via baked-tint textures)
- Stone: amethyst, obsidian, crying obsidian
- Concrete (16 colors)
- Terracotta (plain + 16 colors)

## When to revisit

Unshelve and resume if any of the following change:

- **Polymer adds a public API for extending slab pools** -- mod authors can register their own sacrificial pairs
- **Mojang adds more waxed-twin slab patterns in vanilla** -- lossless pool expansion becomes possible
- **Mojang fixes display entity performance at scale** -- virtual slabs become megabuild-safe
- **Server owner decides to ship a client companion mod** -- the Slabee-style approach unlocks unlimited slabs with no entity cost
- **Server owner relaxes the no-sacrifice rule** for specific rarely-used vanilla slabs

## Dependencies (as-was)

- Minecraft 26.1
- Fabric Loader 0.18.5+
- Fabric API 0.144.3+26.1
- Polymer 0.16.0-pre.3+26.1
- Jade 26.0.8+fabric (optional, for correct block tooltips)

## Building

```
./gradlew build
```

JAR output: `build/libs/`

## License

MIT
