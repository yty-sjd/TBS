import { watch, onUnmounted } from 'vue'

export function useProjectile(canvasRef, currentEventRef) {
  let animId = null

  function animateAttack(evt) {
    const canvas = canvasRef.value
    if (!canvas) return

    const attacker = document.querySelector(`[data-team="${evt.team}"][data-pos="${evt.pos}"]`)
    const target = document.querySelector(`[data-team="${evt.targetTeam}"][data-pos="${evt.targetPos}"]`)
    if (!attacker || !target) return

    const cr = canvas.getBoundingClientRect()
    canvas.width = cr.width
    canvas.height = cr.height

    const ar = attacker.getBoundingClientRect()
    const tr = target.getBoundingClientRect()
    const ax = ar.left + ar.width / 2 - cr.left
    const ay = ar.top + ar.height / 2 - cr.top
    const tx = tr.left + tr.width / 2 - cr.left
    const ty = tr.top + tr.height / 2 - cr.top

    const ctx = canvas.getContext('2d')
    const start = performance.now()
    const DURATION = 300

    function draw(now) {
      const p = Math.min((now - start) / DURATION, 1)
      const t = 1 - Math.pow(1 - p, 3) // ease-out

      ctx.clearRect(0, 0, canvas.width, canvas.height)

      const cx = ax + (tx - ax) * t
      const cy = ay + (ty - ay) * t
      const sx = ax + (tx - ax) * Math.max(0, t - 0.3)
      const sy = ay + (ty - ay) * Math.max(0, t - 0.3)

      ctx.save()
      ctx.strokeStyle = '#ffd54f'
      ctx.lineWidth = 3
      ctx.shadowColor = '#ffd54f'
      ctx.shadowBlur = 12
      ctx.beginPath()
      ctx.moveTo(sx, sy)
      ctx.lineTo(cx, cy)
      ctx.stroke()

      ctx.shadowBlur = 20
      ctx.fillStyle = '#fff'
      ctx.beginPath()
      ctx.arc(cx, cy, 4, 0, Math.PI * 2)
      ctx.fill()
      ctx.restore()

      if (p < 1) {
        animId = requestAnimationFrame(draw)
      } else {
        ctx.save()
        ctx.shadowColor = '#ffd54f'
        ctx.shadowBlur = 30
        ctx.fillStyle = '#fff'
        ctx.globalAlpha = 0.8
        ctx.beginPath()
        ctx.arc(tx, ty, 12, 0, Math.PI * 2)
        ctx.fill()
        ctx.restore()
        setTimeout(() => ctx.clearRect(0, 0, canvas.width, canvas.height), 200)
      }
    }

    if (animId) cancelAnimationFrame(animId)
    animId = requestAnimationFrame(draw)
  }

  watch(currentEventRef, (evt) => {
    if (evt && evt.type === 'attack') {
      setTimeout(() => animateAttack(evt), 50)
    }
  })

  onUnmounted(() => {
    if (animId) cancelAnimationFrame(animId)
  })
}