// Usage (needs playwright + `npm install skinview3d@3.1.0`): node render.js kartonek_skin.png render.png slim
const { chromium } = require('playwright');
const fs = require('fs');
const [skinPath, out, model] = process.argv.slice(2);
const bundle = fs.readFileSync(require.resolve('skinview3d/bundles/skinview3d.bundle.js'), 'utf8');
const skin = 'data:image/png;base64,' + fs.readFileSync(skinPath).toString('base64');
const html = `<html><body style="margin:0;background:transparent"><canvas id="c"></canvas>
<script>${bundle}</script><script>
const v = new skinview3d.SkinViewer({ canvas: document.getElementById('c'), width: 2048, height: 2048,
  preserveDrawingBuffer: true, renderPaused: true });
v.background = null;
v.fov = 30;
v.globalLight.intensity = 3.0;
v.cameraLight.intensity = 1.4;
v.cameraLight.position.set(12, 20, 12);
const p = v.playerObject;
p.rotation.y = 0.5;              // body turned like the in-game paper doll
p.skin.head.rotation.y = -0.2;   // head looking slightly back at the viewer
p.skin.head.rotation.x = 0.06;
p.skin.leftArm.rotation.z = 0.08;
p.skin.rightArm.rotation.z = -0.08;
v.camera.position.set(-4, 19.5, 44); // slightly above: head-and-shoulders framing
v.controls.target.set(0, 14, 0);
v.camera.lookAt(0, 14, 0);
v.loadSkin(${JSON.stringify(skin)}, { model: ${JSON.stringify(model || 'auto-detect')} }).then(() => {
  v.camera.lookAt(0, 14, 0); v.render(); window.done = true; });
</script></body></html>`;
(async () => {
  const b = await chromium.launch({ args: ['--use-gl=angle', '--use-angle=swiftshader', '--enable-unsafe-swiftshader'] });
  const pg = await b.newPage({ viewport: { width: 2048, height: 2048 } });
  pg.on('pageerror', e => console.error(e));
  await pg.setContent(html);
  await pg.waitForFunction('window.done === true', null, { timeout: 30000 });
  await (await pg.$('#c')).screenshot({ path: out, omitBackground: true });
  await b.close();
})();
