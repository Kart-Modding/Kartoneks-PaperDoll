# Promotional images

| Folder | What's in it |
|---|---|
| `Original/` | The in-game screenshots, untouched (1920x1200) |
| `Compressed/` | The same screenshots as JPEGs, small enough for the mod pages |
| `Infographics/` | The finished images used on Modrinth and CurseForge |
| `Infographics/PSD/` | Layered Photoshop files for each infographic: open them to edit by hand |
| `Source/` | What the infographics are built from: one HTML page each, plus `build.js` |

## Editing in Photoshop (or Photopea, GIMP, Affinity)

Every PSD has named layers and groups, the same way the HeadTracker ones do. Headings and captions are live
**text layers** set in Inter and DejaVu Sans Mono, so you can retype them; install
[Inter](https://rsms.me/inter/) first, or Photoshop swaps in another font. The screenshots, cards and icons are
plain pixel layers. After editing, export the PNG over the matching file in `Infographics/`.

## Rebuilding from the HTML

Changing a page in `Source/pages/` and rebuilding regenerates both the PNG and the PSD (this overwrites
hand edits made to the PSD):

```bash
cd "Promotional Images/Source"
npm install
npx playwright install chromium
node build.js              # every page, or e.g. `node build.js banner`
```

Inter and DejaVu Sans Mono must be installed for the pages to render the same way.
How pages turn into layers is described at the top of `build.js`.

## Icons

The icons in `features` (Any corner, Rotation modes, Client-side) are from [Lucide](https://lucide.dev)
(`lucide-static` 1.68.0, the same set Spoty Widget uses), under the ISC license. The license allows use and
changes, as long as its notice is kept: see `LUCIDE-LICENSE.txt`.
