<template>
  <div class="account-page">
    <div class="account-bg">
      <div class="account-card">
        <div class="account-header">
          <el-button class="btn-back" @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
          </el-button>
          <span class="account-title">账号管理</span>
        </div>

        <div class="account-body">
          <div class="account-section">
            <h3>修改用户名</h3>
            <p class="section-desc">更改您的登录用户名</p>
            <el-button class="btn-action" @click="openVerify('username')">修改</el-button>
          </div>

          <div class="account-section">
            <h3>修改绑定邮箱</h3>
            <p class="section-desc">更换账号绑定的邮箱地址</p>
            <el-button class="btn-action" @click="openVerify('email')">修改</el-button>
          </div>

          <div class="account-section">
            <h3>修改密码</h3>
            <p class="section-desc">设置新的登录密码</p>
            <el-button class="btn-action" @click="openVerify('password')">修改</el-button>
          </div>
        </div>
      </div>

      <!-- 邮箱验证弹窗 -->
      <el-dialog v-model="showVerify" title="身份验证" width="360px" :close-on-click-modal="false">
        <el-form :model="verifyForm" label-width="auto">
          <el-form-item label="请输入邮箱">
            <el-input v-model="verifyForm.email" placeholder="输入绑定邮箱" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showVerify = false">取消</el-button>
          <el-button type="primary" @click="doVerify">验证</el-button>
        </template>
      </el-dialog>

      <!-- 修改弹窗 -->
      <el-dialog v-model="showEdit" :title="editTitle" width="360px" :close-on-click-modal="false">
        <el-form :model="editForm" label-width="auto">
          <el-form-item v-if="editType === 'username'" label="新用户名">
            <el-input v-model="editForm.username" placeholder="请输入新用户名" />
          </el-form-item>
          <el-form-item v-if="editType === 'email'" label="新邮箱">
            <el-input v-model="editForm.email" placeholder="请输入新邮箱" />
          </el-form-item>
          <el-form-item v-if="editType === 'password'" label="新密码">
            <el-input v-model="editForm.password" type="password" placeholder="请输入新密码" show-password />
          </el-form-item>
          <el-form-item v-if="editType === 'password'" label="确认新密码">
            <el-input v-model="editForm.password2" type="password" placeholder="请再次输入新密码" show-password />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showEdit = false">取消</el-button>
          <el-button type="primary" @click="doEdit">确认修改</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { verifyEmail, updateAccount } from '../api/auth'

const router = useRouter()

const showVerify = ref(false)
const showEdit = ref(false)
const editType = ref('')
const verified = ref(false)

const verifyForm = ref({ email: '' })
const editForm = ref({ username: '', email: '', password: '', password2: '' })

const editTitle = computed(() => {
  const map = { username: '修改用户名', email: '修改邮箱', password: '修改密码' }
  return map[editType.value] || ''
})

function goBack() {
  router.push('/home?tab=user')
}

function openVerify(type) {
  editType.value = type
  verifyForm.value.email = ''
  editForm.value = { username: '', email: '', password: '', password2: '' }
  showVerify.value = true
}

async function doVerify() {
  if (!verifyForm.value.email) {
    ElMessage.warning('请输入邮箱')
    return
  }
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  try {
    const res = await verifyEmail({ userId: user.id, email: verifyForm.value.email })
    if (res.success) {
      verified.value = true
      showVerify.value = false
      showEdit.value = true
    } else {
      ElMessage.error(res.message || '验证失败')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('验证失败')
  }
}

async function doEdit() {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  const payload = { userId: user.id, type: editType.value }
  if (editType.value === 'username') {
    if (!editForm.value.username) { ElMessage.warning('请输入新用户名'); return }
    payload.username = editForm.value.username
  } else if (editType.value === 'email') {
    if (!editForm.value.email) { ElMessage.warning('请输入新邮箱'); return }
    payload.email = editForm.value.email
  } else if (editType.value === 'password') {
    if (!editForm.value.password) { ElMessage.warning('请输入新密码'); return }
    if (editForm.value.password !== editForm.value.password2) { ElMessage.warning('两次密码不一致'); return }
    payload.password = editForm.value.password
  }
  try {
    const res = await updateAccount(payload)
    if (res.success) {
      if (editType.value === 'username') {
        user.username = editForm.value.username
        localStorage.setItem('user', JSON.stringify(user))
      }
      if (editType.value === 'email') {
        user.email = editForm.value.email
        localStorage.setItem('user', JSON.stringify(user))
      }
      ElMessage.success('修改成功')
      showEdit.value = false
    } else {
      ElMessage.error(res.message || '修改失败')
    }
  } catch (e) {
    console.error(e)
    ElMessage.error('修改失败')
  }
}
</script>

<style scoped>
.account-page {
  width: 100%; height: 100%;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  position: relative;
  overflow: hidden;
}
.account-bg {
  width: 100%; height: 100%;
  display: flex; align-items: center; justify-content: center;
}
.account-card {
  width: 500px;
  background: rgba(0,0,0,0.6);
  border-radius: 12px;
  padding: 32px;
}
.account-header {
  display: flex; align-items: center; gap: 16px;
  margin-bottom: 24px;
}
.account-title {
  font-size: 22px; color: #fff;
}
.account-body {
  display: flex; flex-direction: column; gap: 16px;
}
.account-section {
  display: flex; align-items: center; justify-content: space-between;
  padding: 16px;
  background: rgba(255,255,255,0.05);
  border-radius: 8px;
}
.account-section h3 {
  margin: 0; font-size: 16px; color: #fff;
}
.section-desc {
  margin: 4px 0 0; font-size: 13px; color: #888;
}
.btn-back {
  background: #333 !important; border: none !important;
  color: #fff !important; border-radius: 0;
  padding: 4px 20px 4px 8px;
}
.btn-action {
  background: #333 !important; border: none !important;
  color: #fff !important; border-radius: 0;
}
.btn-action:hover { background: #444 !important; }
</style>