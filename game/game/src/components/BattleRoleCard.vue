<template>
  <div :class="['role-card', { 'role-active': active, 'role-dead': !role.alive }, 'role-effect-' + effect.type]"
    @mouseenter="$emit('hover')" @mouseleave="$emit('leave')" @click="$emit('pin')">
    <div class="portrait-wrap">
      <img v-if="role.portraitId && !imgFailed"
        :src="roleBgUrl(role.portraitId)"
        class="role-portrait"
        @error="imgFailed = true" />
      <img v-else-if="!fallbackFailed"
        :src="roleBgUrl('default')"
        class="role-portrait"
        @error="fallbackFailed = true" />
      <div v-else class="portrait-placeholder">{{ role.name.charAt(0) }}</div>
      <canvas ref="effectCanvas" class="effect-canvas"></canvas>
    </div>
    <div class="role-name">{{ role.name }}</div>
    <div class="hp-bar-wrap">
      <el-progress :percentage="role.hpPercent" :color="hpColor(role.hpPercent)" :stroke-width="8" :show-text="false" />
      <span class="hp-text">{{ role.hp }} / {{ role.maxHp }}</span>
    </div>
    <div class="role-stats">
      <span>🔵 {{ role.mana }} &nbsp; ⚔ {{ role.attack }}</span>
      <span class="role-buffs" v-if="role.buffList.length > 0">
        <span v-for="b in role.buffList" :key="b.name" class="buff-dot" :style="{ background: buffColor(b.type) }" :title="b.label"></span>
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, toRef } from 'vue'
import { useParticleEffect } from '../composables/useParticleEffect'
import { roleBgUrl } from '../config/cdn'

const props = defineProps({
  role: { type: Object, required: true },
  active: { type: Boolean, default: false },
  effect: { type: Object, default: () => ({ type: '', value: 0 }) }
})

defineEmits(['hover', 'leave', 'pin'])

const imgFailed = ref(false)
const fallbackFailed = ref(false)
const effectCanvas = ref(null)

useParticleEffect(effectCanvas, toRef(props, 'effect'))

function hpColor(pct) {
  if (pct > 60) return '#67c23a'
  if (pct > 30) return '#e6a23c'
  return '#f56c6c'
}
function buffColor(type) {
  const map = { danger: '#f56c6c', success: '#67c23a', warning: '#e6a23c', info: '#409eff' }
  return map[type] || '#409eff'
}
</script>

<style scoped>
.role-card {
  width: 220px; padding: 8px; border-radius: 8px;
  background: #1e3a5f; border: 2px solid transparent;
  transition: all 0.3s; text-align: center; cursor: pointer;
}
.role-card:hover { border-color: #ffd54f; }
.role-active {
  border-color: #ffd54f;
  box-shadow: 0 0 12px rgba(255, 213, 79, 0.5);
  transform: scale(1.05);
}
.role-dead { opacity: 0.4; background: #3a1a1a; }

.portrait-wrap {
  width: 100%; height: 300px; margin-bottom: 8px;
  border-radius: 6px; overflow: hidden; background: #0d1b2a;
  display: flex; align-items: center; justify-content: center;
  position: relative;
}
.role-portrait { width: 100%; height: 100%; object-fit: contain; transform: scale(1.6); }
.portrait-placeholder { font-size: 60px; font-weight: bold; color: #4a6fa5; }
.effect-canvas {
  position: absolute; top: 0; left: 0; width: 100%; height: 100%;
  pointer-events: none; z-index: 2;
}

.role-name {
  font-weight: bold; font-size: 18px; margin-bottom: 6px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.role-hp { font-size: 12px; margin-top: 4px; }
.hp-bar-wrap { position: relative; display: flex; align-items: center; }
.hp-bar-wrap :deep(.el-progress) { flex: 1; }
.hp-text {
  position: absolute; left: 50%; transform: translateX(-50%);
  font-size: 14px; font-weight: bold; color: #fff;
  text-shadow: 0 1px 3px rgba(0,0,0,0.8); pointer-events: none;
}
.role-stats { font-size: 15px; margin-top: 4px; display: flex; align-items: center; justify-content: center; gap: 6px; }
.role-buffs { display: flex; flex-wrap: wrap; gap: 2px; }
.buff-dot {
  width: 10px; height: 10px; border-radius: 50%;
  border: 1.5px solid #fff;
  box-shadow: 0 1px 3px rgba(0,0,0,0.5);
}
.role-passives { margin-top: 4px; display: flex; flex-wrap: wrap; gap: 2px; justify-content: center; }

/* ====== 攻击特效 ====== */
.role-effect-attack {
  animation: attackFlash 0.4s ease;
}
@keyframes attackFlash {
  0%, 100% { box-shadow: 0 0 0px transparent; }
  50% { box-shadow: 0 0 20px #ffd54f, 0 0 40px rgba(255, 213, 79, 0.3); }
}

/* ====== 受伤特效 ====== */
.role-effect-damage {
  animation: hitShake 0.4s ease;
  border-color: #ef5350 !important;
}
@keyframes hitShake {
  0%, 100% { transform: translateX(0); }
  20% { transform: translateX(-5px); }
  40% { transform: translateX(5px); }
  60% { transform: translateX(-3px); }
  80% { transform: translateX(3px); }
}

/* ====== 阵亡特效 ====== */
.role-effect-death {
  animation: deathFade 0.8s ease forwards;
}
@keyframes deathFade {
  0% { opacity: 1; filter: none; }
  100% { opacity: 0.3; filter: grayscale(1); }
}

/* ====== 治疗特效 ====== */
.role-effect-heal {
  animation: healGlow 0.5s ease;
}
@keyframes healGlow {
  0%, 100% { box-shadow: 0 0 0px transparent; }
  50% { box-shadow: 0 0 16px #4fc3f7, 0 0 32px rgba(79, 195, 247, 0.3); }
}
</style>