import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/Login.vue'
import HomePage from '../components/HomePage.vue'
import AccountManage from '../components/AccountManage.vue'
import FriendsPage from '../components/FriendsPage.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/home', component: HomePage },
  { path: '/account', component: AccountManage },
  { path: '/friends', component: FriendsPage }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const fromLogin = sessionStorage.getItem('fromLogin')
  const protectedPaths = ['/home', '/account', '/friends']

  if (protectedPaths.includes(to.path) && !fromLogin) {
    return '/login'
  }
  if (to.path === '/login' && fromLogin) {
    return '/home'
  }
  return true
})

export default router