# Twilight Forest

[![Discord](https://img.shields.io/discord/313006291012288521.svg?colorB=7289DA&logo=discord&style=flat-square)](https://discord.gg/6v3z26B)
[![Crowdin](https://badges.crowdin.net/twilight-forest/localized.svg)](https://crowdin.com/project/twilight-forest)
[![CurseForge](http://cf.way2muchnoise.eu/full_227639_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest)
[![For MC](http://cf.way2muchnoise.eu/versions/For%20MC_227639_all.svg)](https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest)
[![Codex Fork](https://img.shields.io/badge/Codex%20fork-1.21.1-4c6fff?style=flat-square)](https://github.com/Asakitan/twilightforest-fabric-1.21.1/tree/1.21.1)
[![Loader](https://img.shields.io/badge/loader-Fabric%20%2F%20Arclight-f5a623?style=flat-square)](https://fabricmc.net/)

This is the Codex 1.21.1 fork of Twilight Forest Fabric for a Fabric/Arclight Minecraft server and paired Fabric client.

The upstream Twilight Forest project remains the original mod and creative/code base. This fork keeps the Twilight Forest ids, data, assets, and gameplay surface while providing the current Fabric/Arclight implementation used by the Codex server profile.

## Implementation

This fork currently includes:

- Fabric Loom build for Minecraft 1.21.1 and Java 21.
- `codex_twilight` mod metadata, access widener, mixins, main entrypoint, client entrypoint, and early grass-color hook.
- Server-side Twilight registries for blocks, block entities, items, entities, particles, sounds, recipes, menus, stats, advancements, mob effects, loot hooks, structures, features, density functions, biome sources, map decorations, data components, data serializers, damage types, and structure processors.
- Twilight entity implementations across monsters, bosses, passives, projectiles, AI goals, and movement controls.
- Twilight worldgen/data content under the `catty`, `codex_twilight`, `minecraft`, and `twilightforest` namespaces.
- Paired Fabric client renderers, model layers, block-entity renderers, particles, custom model loading, resource reload hooks, and entity visual support.
- Bundled client/server resources including blockstates, models, lang files, textures, sounds, shaders, loot tables, recipes, tags, structures, functions, dimensions, and biome data.
- Compatibility shims for translated Twilight/NeoForge-style code used by the Fabric/Arclight runtime.

## Layout

```text
src/main/java/        shared/server Fabric implementation and translated Twilight runtime code
src/tfjava/           larger translated Twilight common/server source surface
src/client/java/      Fabric client initializer, renderer bootstrap, particles, model layers, client hooks
src/tfjava-client/    translated Twilight client models, renderers, and renderer layers
src/main/resources/   mod metadata, mixins, assets, data packs, structures, sounds, shaders, and lang files
local/                local upstream/reference files used by this fork
```

## Build

From this directory:

```powershell
.\gradlew.bat buildAndInstall
```

The build uses Fabric Loader `0.18.1`, Fabric API `0.116.4+1.21.1`, official Mojang mappings, and Loom split environment source sets. The `buildAndInstall` task installs the remapped mod jar into the server `mods/` directory and mirrors `src/main/resources/data` into the server required data-pack directory.

## Downloads

Official Twilight Forest releases are available on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest).

This Codex fork branch is maintained at [Asakitan/twilightforest-fabric-1.21.1](https://github.com/Asakitan/twilightforest-fabric-1.21.1/tree/1.21.1).

## Community

Keep in touch with upstream Twilight Forest development on the [Twilight Forest Discord server](https://discord.gg/6v3z26B).

## Translation

Upstream Twilight Forest translations are handled through the [Crowdin project](https://crowdin.com/project/twilight-forest). This fork also carries local `en_us` and `zh_cn` language files for Codex-specific content.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for this fork's contribution notes. Keep upstream Twilight Forest attribution intact when adapting code or assets.

## Licensing

Code in this fork is covered by [LICENSE](LICENSE). Codex-original code is MIT-licensed, while files copied or adapted from upstream Twilight Forest retain the upstream Twilight Forest license terms described in the license notice.

Non-code Twilight Forest-derived assets are covered by [ASSET_LICENSE](ASSET_LICENSE) and upstream Twilight Forest asset notices. Sound assets and structure assets inherited from Twilight Forest remain All Rights Reserved under the upstream notices.

Upstream projects:

- [TeamTwilight/twilightforest](https://github.com/TeamTwilight/twilightforest)
- [TeamTwilight/twilightforest-fabric](https://github.com/TeamTwilight/twilightforest-fabric)
