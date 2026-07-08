<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const DESIGN_W = 1920
const DESIGN_H = 1080
const scale = ref(1)

function updateScale() {
  const w = window.innerWidth
  const h = window.innerHeight
  scale.value = Math.min(w / DESIGN_W, h / DESIGN_H)
}

const scaleStyle = computed(() => ({
  transform: `scale(${scale.value})`,
  transformOrigin: 'center center',
  width: DESIGN_W + 'px',
  height: DESIGN_H + 'px'
}))

onMounted(() => {
  updateScale()
  window.addEventListener('resize', updateScale)
})

onUnmounted(() => {
  window.removeEventListener('resize', updateScale)
})
</script>

<template>
  <div class="app-scale" :style="scaleStyle">
    <router-view v-slot="{ Component }">
      <transition name="page-fade">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
  background: #000;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
}
* {
  box-sizing: border-box;
}
.app-scale {
  flex-shrink: 0;
}
.page-fade-enter-active,
.page-fade-leave-active {
  transition: opacity 0.4s ease;
}
.page-fade-enter-from,
.page-fade-leave-to {
  opacity: 0;
}
</style>