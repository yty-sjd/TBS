<template>
  <div class="login-container">
    <div class="login-bg">
      <img :src="startBgUrl('1.webp')" :class="{ active: activeBg === 0 }" />
      <img :src="startBgUrl('2.webp')" :class="{ active: activeBg === 1 }" />
      <img :src="startBgUrl('3.webp')" :class="{ active: activeBg === 2 }" />
    </div>
    <div class="login-card" v-show="!showStart">
      <h1 class="login-title">TBS 团队对战模拟器</h1>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
            </el-form-item>
            <el-form-item>
              <div class="login-actions">
                <el-button type="primary" :loading="loading" @click="handleLogin" style="flex: 1">登录</el-button>
                <el-button @click="showReset = true">修改密码</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="registerForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item label="邮箱" required>
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" :loading="loading" @click="handleRegister" style="width: 100%">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <!-- 修改密码弹窗 -->
      <el-dialog v-model="showReset" title="修改密码" width="360px">
        <el-form :model="resetForm" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="resetForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="resetForm.email" placeholder="请输入绑定邮箱" />
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="resetForm.password" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="resetForm.password2" type="password" placeholder="请再次输入新密码" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showReset = false">取消</el-button>
          <el-button type="primary" @click="handleReset">确认</el-button>
        </template>
      </el-dialog>
    </div>

    <div class="start-overlay" :class="{ 'start-visible': showStart, 'start-leaving': leaving }" @click="goHome">
      <div class="top-panel">
        <div class="top-datetime">
          <div class="top-time">{{ now.time }}</div>
          <div class="top-date">{{ now.date }}</div>
        </div>
        <div class="top-fullscreen" @click.stop="toggleFullscreen" :title="isFullscreen ? '退出全屏' : '全屏'">
          <div class="fs-icon" :class="{ 'fs-active': isFullscreen }">
            <div class="fs-box fs-box-1" />
            <div class="fs-box fs-box-2" />
          </div>
        </div>
      </div>
      <div class="start-panel">
        <div class="start-diamond-wrap">
          <div class="start-ring" />
          <div class="start-diamond">
            <div class="start-diamond-inner">
              <div class="start-play" />
              <span class="start-text">start</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register, resetPassword } from '../api/auth'
import { startBgUrl } from '../config/cdn'

const activeBg = ref(0)
let bgTimer = null

onMounted(() => {
  bgTimer = setInterval(() => {
    activeBg.value = (activeBg.value + 1) % 3
  }, 15000)
  document.addEventListener('fullscreenchange', onFullscreenChange)
  isFullscreen.value = !!document.fullscreenElement
})

onUnmounted(() => {
  clearInterval(bgTimer)
  clearInterval(timeTimer)
  document.removeEventListener('fullscreenchange', onFullscreenChange)
})

const showStart = ref(false)
const leaving = ref(false)
const isFullscreen = ref(false)
const now = ref({ time: '', date: '' })
let timeTimer = null

function updateTime() {
  const d = new Date()
  now.value.time = d.toLocaleTimeString('zh-CN', { hour12: false, hour: '2-digit', minute: '2-digit' })
  now.value.date = d.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', weekday: 'short' })
}

const activeTab = ref('login')
const loading = ref(false)
const showReset = ref(false)
const resetForm = ref({ username: '', email: '', password: '', password2: '' })
const router = useRouter()

const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  username: '',
  password: '',
  email: ''
})

async function handleLogin() {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请填写所有必填字段')
    return
  }
  loading.value = true
  try {
    const res = await login(loginForm.value)
    if (res.success) {
      ElMessage.success(res.message)
      localStorage.setItem('token', res.token)
      localStorage.setItem('user', JSON.stringify(res.user))
      sessionStorage.setItem('fromLogin', 'true')
      showStart.value = true
      updateTime()
      timeTimer = setInterval(updateTime, 1000)
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function handleRegister() {
  if (!registerForm.value.username || !registerForm.value.password || !registerForm.value.email) {
    ElMessage.warning('请填写所有必填字段')
    return
  }
  loading.value = true
  try {
    const res = await register(registerForm.value)
    if (res.success) {
      ElMessage.success('注册成功，请登录')
      registerForm.value = { username: '', password: '', email: '' }
      activeTab.value = 'login'
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('注册失败')
  } finally {
    loading.value = false
  }
}

async function handleReset() {
  if (!resetForm.value.username || !resetForm.value.email) {
    ElMessage.warning('请输入用户名和邮箱')
    return
  }
  if (!resetForm.value.password) {
    ElMessage.warning('请输入新密码')
    return
  }
  if (resetForm.value.password !== resetForm.value.password2) {
    ElMessage.warning('两次密码不一致')
    return
  }
  try {
    const res = await resetPassword(resetForm.value)
    if (res.success) {
      ElMessage.success('密码修改成功，请登录')
      showReset.value = false
      resetForm.value = { username: '', email: '', password: '', password2: '' }
    } else {
      ElMessage.error(res.message || '修改失败')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('修改失败')
  }
}

function goHome() {
  if (leaving.value) return
  leaving.value = true
  setTimeout(() => {
    router.push('/home')
  }, 500)
}

function toggleFullscreen() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

function onFullscreenChange() {
  isFullscreen.value = !!document.fullscreenElement
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #1a1a2e;
  position: relative;
  overflow: hidden;
}
.login-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}
.login-bg img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0;
  transition: opacity 1.5s ease;
}
.login-bg img.active {
  opacity: 1;
}
.login-card {
  position: relative;
  z-index: 1;
  width: 400px;
  padding: 32px;
  background: rgba(255, 255, 255, 0.85);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.5);
}
.login-title {
  text-align: center;
  color: #ffd54f;
  margin-bottom: 24px;
  font-size: 24px;
}
.login-actions {
  display: flex; gap: 8px;
}

.start-overlay {
  position: fixed;
  inset: 0;
  z-index: 100;
  pointer-events: none;
  opacity: 0;
  transition: opacity 0.5s;
}
.start-overlay.start-visible {
  opacity: 1;
  pointer-events: auto;
}
.start-overlay.start-leaving {
  opacity: 0;
  background: #000;
  transition: opacity 0.5s, background 0.5s;
}
.top-panel {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 108px;
  background: #000;
  transform: translateY(-100%);
  transition: transform 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}
.start-overlay.start-visible .top-panel {
  transform: translateY(0);
}
.top-datetime {
  position: absolute;
  bottom: 16px;
  left: 24px;
  color: #ffd54f;
}
.top-time {
  font-size: 22px;
  font-weight: 300;
  letter-spacing: 2px;
}
.top-date {
  font-size: 12px;
  opacity: 0.7;
  margin-top: 2px;
}
.top-fullscreen {
  position: absolute;
  bottom: 16px;
  right: 24px;
  cursor: pointer;
  opacity: 0.5;
  transition: opacity 0.2s;
}
.top-fullscreen:hover {
  opacity: 1;
}
.top-fullscreen::before {
  content: '';
  position: absolute;
  inset: -8px;
  border: 2px solid #ffd54f;
  border-radius: 4px;
  animation: fsRingPulse 1.5s ease-out infinite;
}
@keyframes fsRingPulse {
  0% { transform: scale(0.8); opacity: 1; }
  100% { transform: scale(1.6); opacity: 0; }
}
.fs-icon {
  position: relative;
  width: 20px;
  height: 20px;
}
.fs-box {
  position: absolute;
  width: 14px;
  height: 14px;
  border: 2px solid #ffd54f;
  transition: all 0.3s;
}
.fs-box-1 {
  top: 0;
  left: 0;
}
.fs-box-2 {
  bottom: 0;
  right: 0;
}
.fs-active .fs-box-1 {
  top: 2px;
  left: 2px;
  width: 10px;
  height: 10px;
}
.fs-active .fs-box-2 {
  bottom: 2px;
  right: 2px;
  width: 16px;
  height: 16px;
}
.start-panel {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 135px;
  background: #000;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateY(100%);
  transition: transform 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}
.start-overlay.start-visible .start-panel {
  transform: translateY(0);
}
.start-diamond-wrap {
  position: relative;
  width: 50px;
  height: 50px;
}
.start-ring {
  position: absolute;
  inset: -20px;
  border: 2px solid #ffd54f;
  transform: rotate(45deg);
  animation: ringPulse 1.5s ease-out infinite;
}
@keyframes ringPulse {
  0% { transform: rotate(45deg) scale(0.8); opacity: 1; }
  100% { transform: rotate(45deg) scale(1.6); opacity: 0; }
}
.start-diamond {
  position: absolute;
  inset: 0;
  border: 5px solid #ffd54f;
  background: transparent;
  transform: rotate(45deg);
  display: flex;
  align-items: center;
  justify-content: center;
}
.start-diamond-inner {
  transform: rotate(-45deg);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}
.start-play {
  width: 0;
  height: 0;
  border-left: 9px solid #ffd54f;
  border-top: 5.7px solid transparent;
  border-bottom: 5.7px solid transparent;
  margin-left: 3px;
}
.start-text {
  color: #ffd54f;
  font-size: 8px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
}
</style>