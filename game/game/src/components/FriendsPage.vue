<template>
  <div class="friends-page">
    <div class="friends-top">
      <el-button class="btn-back" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
      </el-button>
      <span class="friends-title">好友</span>
    </div>
    <div class="friends-body">
      <div class="friends-tabs">
        <div
          v-for="tab in tabs" :key="tab.key"
          :class="['friends-tab', { active: activeTab === tab.key }]"
          @click="activeTab = tab.key"
        >{{ tab.label }}</div>
      </div>
      <div class="friends-content">
        <div v-if="activeTab === 'profile'" class="tab-panel">
          <div class="profile-card">
            <img :src="avatarUrl(assistantId)" class="profile-avatar" />
            <h3>{{ user.username }}</h3>
            <p>ID: {{ user.id }}</p>
          </div>
        </div>
        <div v-if="activeTab === 'list'" class="tab-panel">
          <div v-if="requests.length > 0" class="section">
            <h4>好友申请 ({{ requests.length }})</h4>
            <div v-for="r in requests" :key="r.id" class="friend-item">
              <img :src="avatarUrl(r.assistantId)" class="search-avatar" />
              <div class="search-info">
                <span class="search-name">{{ r.username }}</span>
              </div>
              <div class="item-actions">
                <el-button size="small" @click="doAccept(r)">同意</el-button>
                <el-button size="small" @click="doReject(r)">拒绝</el-button>
              </div>
            </div>
          </div>
          <div class="section">
            <h4>我的好友 ({{ friends.length }})</h4>
            <div v-if="friends.length === 0" class="empty">暂无好友</div>
            <div v-for="f in friends" :key="f.friendId" class="friend-item">
              <img :src="avatarUrl(f.assistantId)" class="search-avatar" />
              <div class="search-info">
                <span class="search-name">{{ f.username }}</span>
              </div>
              <span class="friend-time">{{ formatTime(f.lastLoginAt) }}</span>
              <el-button size="small" @click="doDelete(f)">删除</el-button>
            </div>
          </div>
        </div>
        <div v-if="activeTab === 'add'" class="tab-panel">
          <div class="search-box">
            <el-input v-model="keyword" placeholder="搜索玩家用户名" @keyup.enter="doSearch" />
            <el-button @click="doSearch">搜索</el-button>
          </div>
          <div v-if="searchResults.length > 0" class="section">
            <div v-for="u in searchResults" :key="u.id" class="friend-item">
              <img :src="avatarUrl(u.assistantId)" class="search-avatar" />
              <div class="search-info">
                <span class="search-name">{{ u.username }}</span>
                <span class="search-id">ID: {{ u.id }}</span>
              </div>
              <el-button v-if="u.relation === 'friend'" size="small" disabled>已是好友</el-button>
              <el-button v-else-if="u.relation === 'pending'" size="small" disabled>已申请</el-button>
              <el-button v-else size="small" type="primary" @click.stop="doAdd(u)">添加好友</el-button>
            </div>
          </div>
          <div v-if="searched && searchResults.length === 0" class="empty">未找到玩家</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { searchUser, addFriend, acceptFriend, rejectFriend, deleteFriend, getFriendList, getFriendRequests } from '../api/friend'
import { avatarUrl } from '../config/cdn'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')
const assistantId = user.assistantId || 30086009

const tabs = [
  { key: 'profile', label: '个人名片' },
  { key: 'list', label: '好友列表' },
  { key: 'add', label: '添加好友' }
]
const activeTab = ref('profile')

const friends = ref([])
const requests = ref([])
const keyword = ref('')
const searchResults = ref([])
const searched = ref(false)

onMounted(() => {
  loadFriends()
  loadRequests()
})

function goBack() {
  router.push('/home')
}

async function loadFriends() {
  const res = await getFriendList(user.id)
  if (res.success) friends.value = res.friends
}

async function loadRequests() {
  const res = await getFriendRequests(user.id)
  if (res.success) requests.value = res.requests
}

async function doSearch() {
  if (!keyword.value.trim()) return
  const res = await searchUser(keyword.value.trim(), user.id)
  searched.value = true
  if (res.success) searchResults.value = res.users
}

function formatTime(time) {
  if (!time) return '从未登录'
  const d = new Date(time)
  const now = new Date()
  const diff = now - d
  if (diff < 60000) return '刚刚在线'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  return d.toLocaleDateString()
}

async function doAdd(u) {
  const res = await addFriend({ userId: user.id, friendId: u.id })
  ElMessage[res.success ? 'success' : 'warning'](res.message)
}

async function doAccept(r) {
  const res = await acceptFriend({ userId: r.userId, friendId: user.id })
  if (res.success) {
    ElMessage.success(res.message)
    loadFriends()
    loadRequests()
  }
}

async function doReject(r) {
  const res = await rejectFriend({ userId: r.userId, friendId: user.id })
  if (res.success) {
    ElMessage.success(res.message)
    loadRequests()
  }
}

async function doDelete(f) {
  const res = await deleteFriend({ userId: user.id, friendId: f.friendId })
  if (res.success) {
    ElMessage.success(res.message)
    loadFriends()
  }
}
</script>

<style scoped>
.friends-page {
  width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.85);
  display: flex; flex-direction: column;
  position: relative;
}
.friends-top {
  display: flex; align-items: center; gap: 16px;
  padding: 20px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.friends-title {
  font-size: 22px; color: #fff;
}
.friends-body {
  flex: 1; display: flex;
}
.friends-tabs {
  width: 180px;
  border-right: 1px solid rgba(255,255,255,0.1);
  padding: 12px 0;
}
.friends-tab {
  padding: 14px 24px;
  font-size: 16px; color: #aaa;
  cursor: pointer;
  transition: all 0.2s;
}
.friends-tab:hover { color: #fff; background: rgba(255,255,255,0.05); }
.friends-tab.active { color: #ffd54f; background: rgba(255,213,79,0.1); }
.friends-content {
  flex: 1; padding: 24px;
  overflow-y: auto;
}
.btn-back {
  background: #333 !important; border: none !important;
  color: #fff !important; border-radius: 0;
  padding: 4px 20px 4px 8px;
}
.tab-panel { color: #fff; }
.profile-card {
  text-align: center; padding: 40px;
}
.profile-avatar {
  width: 90px; height: 90px;
  border-radius: 0;
  object-fit: cover;
  margin-bottom: 16px;
  background: #fff;
}
.profile-card h3 { margin: 0; font-size: 20px; }
.profile-card p { color: #888; }
.section { margin-bottom: 24px; }
.section h4 { margin: 0 0 12px; font-size: 16px; color: #aaa; }
.friend-item {
  display: flex; align-items: center; justify-content: space-between;
  gap: 12px;
  padding: 12px 16px;
  background: rgba(255,255,255,0.05);
  border-radius: 8px; margin-bottom: 8px;
}
.item-actions { display: flex; gap: 8px; }
.search-box {
  display: flex; gap: 12px; margin-bottom: 16px;
}
.search-box .el-input { width: 240px; }
.search-avatar {
  width: 44px; height: 44px;
  border-radius: 0;
  object-fit: cover;
  flex-shrink: 0;
  background: #fff;
}
.search-info {
  flex: 1; display: flex; flex-direction: column;
  gap: 2px;
}
.search-name { font-size: 15px; color: #fff; }
.search-id { font-size: 12px; color: #888; }
.friend-time {
  font-size: 12px; color: #666;
  flex: 1; text-align: center;
}
.empty { color: #666; padding: 20px; text-align: center; }
</style>