# Builds the mod icon from an in-game screenshot of the paper doll.
# Usage (needs Pillow + psd-tools): python3 art/paperdoll_icon.py
#   -> art/paperdoll_icon.psd (1024px, layered) and art/paperdoll_icon.png (512px),
#      and copies the PNG to the two icon.png files the loaders read.
import os
import shutil
from PIL import Image, ImageDraw, ImageFilter
from psd_tools import PSDImage
from psd_tools.api.layers import PixelLayer

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SHOT = os.path.join(ROOT, 'Promotional Images', 'Original', '2026-11-03_22.22.25.png')
CROP = (28, 18, 250)        # x, y and side of the square around the paper doll in the screenshot
N = 1024                    # PSD size; the PNG is downscaled to 512
RADIUS = 0.1875 * N         # icon corner radius
YELLOW = (255, 204, 77, 255)
SS = 4                      # supersampling for smooth bracket edges


def rounded_mask():
    m = Image.new('L', (N * SS, N * SS), 0)
    ImageDraw.Draw(m).rounded_rectangle([0, 0, N * SS - 1, N * SS - 1], radius=RADIUS * SS, fill=255)
    return m.resize((N, N), Image.LANCZOS)


def brackets():
    """Viewfinder corners that curve with the icon's own corner radius."""
    S = N * SS
    inset, width, reach = 0.066 * S, 0.028 * S, 0.19 * S
    r = RADIUS * SS - inset             # concentric with the icon corner
    img = Image.new('RGBA', (S, S), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    for sx, sy in ((1, 1), (-1, 1), (1, -1), (-1, -1)):
        cx = inset + r if sx > 0 else S - inset - r      # arc centre
        cy = inset + r if sy > 0 else S - inset - r
        x_edge = inset if sx > 0 else S - inset
        y_edge = inset if sy > 0 else S - inset
        start = {(1, 1): 180, (-1, 1): 270, (1, -1): 90, (-1, -1): 0}[(sx, sy)]
        d.arc([cx - r - width / 2, cy - r - width / 2, cx + r + width / 2, cy + r + width / 2],
              start, start + 90, fill=YELLOW, width=round(width))
        # straight legs running on from the arc, with round ends
        ends = [(x_edge, cy, x_edge, (inset + reach) if sy > 0 else (S - inset - reach)),
                (cx, y_edge, (inset + reach) if sx > 0 else (S - inset - reach), y_edge)]
        for x0, y0, x1, y1 in ends:
            d.line([(x0, y0), (x1, y1)], fill=YELLOW, width=round(width))
            d.ellipse([x1 - width / 2, y1 - width / 2, x1 + width / 2, y1 + width / 2], fill=YELLOW)
    return img.resize((N, N), Image.LANCZOS)


x, y, side = CROP
shot = Image.open(SHOT).convert('RGBA').crop((x, y, x + side, y + side)).resize((N, N), Image.LANCZOS)

vig = Image.new('L', (N, N), 0)
ImageDraw.Draw(vig).ellipse([-N * 0.3, -N * 0.25, N * 1.3, N * 1.35], fill=255)
vig = vig.filter(ImageFilter.GaussianBlur(140))
vignette = Image.new('RGBA', (N, N), (12, 14, 20, 0))
vignette.putalpha(vig.point(lambda v: (255 - v) * 90 // 255))

mask = rounded_mask()
layers = [('Screenshot (22.22.25)', shot), ('Vignette', vignette), ('Corner brackets', brackets())]
for i, (name, im) in enumerate(layers):
    a = Image.composite(im.getchannel('A'), Image.new('L', (N, N), 0), mask)
    im = im.copy()
    im.putalpha(a)
    layers[i] = (name, im)

flat = Image.new('RGBA', (N, N))
for _, im in layers:
    flat = Image.alpha_composite(flat, im)
png = os.path.join(ROOT, 'art', 'paperdoll_icon.png')
flat.resize((512, 512), Image.LANCZOS).save(png, optimize=True)

psd = PSDImage.new('RGBA', (N, N))
for name, im in layers:
    psd.append(PixelLayer.frompil(im, psd, name, 0, 0))
psd.save(os.path.join(ROOT, 'art', 'paperdoll_icon.psd'))

for dest in ('src/main/resources/icon.png', 'src/main/resources/assets/ayame_paperdoll/icon.png'):
    shutil.copyfile(png, os.path.join(ROOT, dest))
