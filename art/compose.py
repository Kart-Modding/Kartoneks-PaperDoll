# Usage (needs Pillow + psd-tools): python3 compose.py render.png screenshot.webp .
import sys
from PIL import Image, ImageDraw, ImageFilter, ImageEnhance
from psd_tools import PSDImage
from psd_tools.api.layers import PixelLayer

render, shot, out = sys.argv[1:4]
N = 1024  # PSD size; PNG icon is downscaled to 512

r = Image.open(render).convert('RGBA')
x0, y0, w, h = r.getbbox()[0], r.getbbox()[1], 0, 0
# Square head-and-shoulders crop: head top plus a margin, bottom of the frame
top = r.getbbox()[1] - 180
side = r.height - top
cx = (r.getbbox()[0] + r.getbbox()[2]) // 2
char = r.crop((cx - side // 2, top, cx + side // 2, r.height)).resize((N, N), Image.LANCZOS)

# Background: the cherry grove from the screenshot, blurred and brightened a touch
s = Image.open(shot).convert('RGBA').crop((960, 0, 1920, 960)).resize((N, N), Image.LANCZOS)
bg = ImageEnhance.Brightness(s.filter(ImageFilter.GaussianBlur(18))).enhance(1.05)

# Vignette to pull focus to the character
vig = Image.new('L', (N, N), 0)
ImageDraw.Draw(vig).ellipse([-N * 0.25, -N * 0.2, N * 1.25, N * 1.3], fill=255)
vig = vig.filter(ImageFilter.GaussianBlur(120))
vignette = Image.new('RGBA', (N, N), (20, 10, 30, 0))
vignette.putalpha(vig.point(lambda v: (255 - v) * 150 // 255))

a = char.getchannel('A')
shadow = Image.new('RGBA', (N, N), (10, 5, 20, 0))
shadow.putalpha(a.filter(ImageFilter.GaussianBlur(28)).point(lambda v: v * 170 // 255))
shadow = shadow.transform((N, N), Image.AFFINE, (1, 0, -18, 0, 1, -10))

# Soft glow in the background's own (lightened) colour instead of a hard outline
tint = tuple(min(255, c + 70) for c in bg.resize((1, 1), Image.LANCZOS).getpixel((0, 0))[:3])
glow = Image.new('RGBA', (N, N), tint + (0,))
glow.putalpha(a.filter(ImageFilter.MaxFilter(9)).filter(ImageFilter.GaussianBlur(36)).point(lambda v: v * 140 // 255))

layers = [('Background', bg), ('Vignette', vignette), ('Glow', glow), ('Shadow', shadow), ('Kartonek', char)]

mask = Image.new('L', (N, N), 0)
ImageDraw.Draw(mask).rounded_rectangle([0, 0, N - 1, N - 1], radius=180, fill=255)
flat = Image.new('RGBA', (N, N))
for _, l in layers:
    flat = Image.alpha_composite(flat, l)
icon = Image.new('RGBA', (N, N), (0, 0, 0, 0))
icon.paste(flat, (0, 0), mask)
icon.resize((512, 512), Image.LANCZOS).save(f'{out}/kartonek_icon.png', optimize=True)

psd = PSDImage.new('RGBA', (N, N))
for name, l in layers:
    psd.append(PixelLayer.frompil(l, psd, name, 0, 0))
psd.save(f'{out}/kartonek_icon.psd')
