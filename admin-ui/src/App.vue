<template>
  <div id="app">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <h2>TBS 管理后台</h2>
        <el-menu :default-active="activeMenu" mode="vertical">
          <el-menu-item index="1" @click="activeMenu = '1'">
            <el-icon><Grid /></el-icon>
            <span>角色管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <RoleManagement />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Grid } from '@element-plus/icons-vue'
import RoleManagement from './components/RoleManagement.vue'

const activeMenu = ref('1')
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

#app {
  height: 100vh;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.sidebar {
  background-color: #2b2f3a;
  color: #fff;
}

.sidebar h2 {
  color: #fff;
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #1f2329;
}

.el-aside {
  height: 100vh;
}

.el-menu {
  border-right: none;
}
</style>