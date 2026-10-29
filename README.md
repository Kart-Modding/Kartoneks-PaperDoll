# Ayame PaperDoll

### [Modrinth](https://modrinth.com/mod/ayame-paperdoll) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ayame-paperdoll) | [McMod](https://www.mcmod.cn/class/17015.html)


Ayame PaperDoll is a highly configurable paper doll mod and a fork of [Extra Player Renderer](https://modrinth.com/mod/extraplayerrenderer). It adds support for **NeoForge, Forge, Quilt and higher versions of Minecraft**, along with **more useful key bindings**, **additional configuration options** (such as **Rotation Unlock**), and a **Visual Configuration Editor**.

### Versions and loaders
One build covers **Minecraft 1.21.1 – 26.3** on every loader:

| Loader | Jar | Minecraft | Config screen |
|---|---|---|---|
| Fabric | `ayame-paperdoll-fabric` (needs Fabric API) | 1.21 – 26.3 | Mod Menu |
| Quilt | `ayame-paperdoll-fabric` (needs Fabric API / QFAPI) | 1.21 – 26.3 | Mod Menu |
| NeoForge | `ayame-paperdoll-neoforge` | 1.21.1, 1.21.3 – 26.3 | Mods list |
| Forge | `ayame-paperdoll-forge` | 1.21.1, 1.21.3 – 26.3 | Mods list |

Pick the jar whose name ends in your Minecraft version (`+1.21.8`, `+26.3`, ...). Jars for every
version are built by GitHub Actions on each push (Actions tab → latest run → Artifacts) and attached
to each GitHub release.

### Building
The source tree is shared by every version with [Stonecutter](https://stonecutter.kikugie.dev/):
version- and loader-specific code sits in `//? if` comments.

```
./gradlew buildFabric     # or buildNeoforge / buildForge / chiseledBuild for everything
```

Jars land in `build/libs/<mod version>/`. Per-version dependencies are in `stonecutter.properties.toml`.

### Default Position and Style:
![Default Position and Style](https://cdn.modrinth.com/data/cached_images/6e7af18771d006eda3077d33800250bc80cdd647.png)

By default, it renders a player model in the top-left corner of the screen and provides a richly customizable configuration screen, which can be accessed through the mod menu or a key binding (default is none). You can also toggle the visibility of the paper doll using a key binding (default is F8).

### Config Screen:
![Config Screen](https://cdn.modrinth.com/data/cached_images/44e1b59e5019df8f03c34fa5d677841eacc26896.png)
![Config Screen](https://cdn.modrinth.com/data/cached_images/c3e90083ab02fc63bda0da3bab7b851f8308382e.png)
### Visual Configuration Editor:
![Visual Configuration Editor](https://cdn.modrinth.com/data/cached_images/0bda8a26fc0822669f736d4caa0fee140354bdb2.png)
