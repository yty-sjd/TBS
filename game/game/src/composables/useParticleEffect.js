import { watch, onUnmounted } from 'vue'

export function useParticleEffect(canvasRef, effectRef) {
  let animId = null
  let particles = []

  function createParticles(effect) {
    const canvas = canvasRef.value
    if (!canvas) return
    const w = canvas.width
    const h = canvas.height
    const cx = w / 2
    const cy = h / 2
    particles = []

    if (effect.type === 'damage') {
      for (let i = 0; i < 14; i++) {
        const angle = Math.random() * Math.PI * 2
        const speed = 1.5 + Math.random() * 4
        particles.push({
          x: cx + (Math.random() - 0.5) * 20,
          y: cy + (Math.random() - 0.5) * 20,
          vx: Math.cos(angle) * speed,
          vy: Math.sin(angle) * speed,
          life: 1,
          decay: 0.015 + Math.random() * 0.035,
          size: 2 + Math.random() * 5,
          color: `hsl(${Math.random() * 20}, 100%, ${50 + Math.random() * 20}%)`
        })
      }

      if (effect.value > 0) {
        particles.push({
          x: cx,
          y: cy - 10,
          vx: (Math.random() - 0.5) * 1.5,
          vy: -2.5 - Math.random() * 1.5,
          life: 1,
          decay: 0.015,
          size: 0,
          color: '#ff4444',
          text: '-' + effect.value
        })
      }
    }
  }

  function animate() {
    const canvas = canvasRef.value
    if (!canvas) return
    const ctx = canvas.getContext('2d')
    ctx.clearRect(0, 0, canvas.width, canvas.height)

    particles = particles.filter(p => p.life > 0)
    if (particles.length === 0) return

    for (const p of particles) {
      p.x += p.vx
      p.y += p.vy
      if (!p.text) p.vy += 0.06
      p.life -= p.decay
      if (p.life <= 0) continue

      if (p.text) {
        ctx.save()
        ctx.globalAlpha = p.life
        ctx.font = 'bold 22px sans-serif'
        ctx.fillStyle = p.color
        ctx.textAlign = 'center'
        ctx.shadowColor = 'rgba(0,0,0,0.6)'
        ctx.shadowBlur = 4
        ctx.fillText(p.text, p.x, p.y)
        ctx.restore()
      } else {
        ctx.globalAlpha = p.life
        ctx.fillStyle = p.color
        ctx.beginPath()
        ctx.arc(p.x, p.y, p.size * p.life, 0, Math.PI * 2)
        ctx.fill()
      }
    }
    ctx.globalAlpha = 1
    animId = requestAnimationFrame(animate)
  }

  function startEffect(effect) {
    if (!effect || !effect.type) return
    const canvas = canvasRef.value
    if (!canvas) return
    const parent = canvas.parentElement
    if (parent) {
      canvas.width = parent.clientWidth
      canvas.height = parent.clientHeight
    }
    if (animId) cancelAnimationFrame(animId)
    createParticles(effect)
    animate()
  }

  watch(effectRef, (newVal) => {
    if (newVal && newVal.type) startEffect(newVal)
  })

  onUnmounted(() => {
    if (animId) cancelAnimationFrame(animId)
  })
}