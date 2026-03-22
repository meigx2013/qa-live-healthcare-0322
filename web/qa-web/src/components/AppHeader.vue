<template>
  <a-layout-header class="header">
    <div class="header-content">
      <div class="logo" @click="navigateTo('/')">
        <img src="https://images.pexels.com/photos/40568/medical-appointment-doctor-healthcare-40568.jpeg?auto=compress&cs=tinysrgb&w=100" alt="QA Live Healthcare" />
        <span>QA Live Healthcare</span>
      </div>
      
      <!-- 桌面端菜单 -->
      <a-menu v-model:selectedKeys="selectedKeys" mode="horizontal" class="nav-menu desktop-menu">
        <a-menu-item key="home" @click="navigateTo('/')">
          <HomeOutlined />
          首页
        </a-menu-item>
        <a-menu-item key="consultation" @click="navigateTo('/consultation')">
          <MessageOutlined />
          问诊
        </a-menu-item>
        <a-menu-item key="doctors" @click="navigateTo('/doctors')">
          <TeamOutlined />
          医生
        </a-menu-item>
        <a-menu-item key="about" @click="navigateTo('/about')">
          <InfoCircleOutlined />
          关于
        </a-menu-item>
      </a-menu>
      <a-button type="primary" class="login-btn desktop-login" @click="navigateTo('/doctor/login')">
        <UserOutlined />
        医生登录
      </a-button>
      
      <!-- 移动端汉堡包菜单按钮 -->
      <a-button class="mobile-menu-btn" type="text" @click="mobileMenuVisible = true">
        <MenuOutlined />
      </a-button>
    </div>
    
    <!-- 移动端抽屉菜单 -->
    <a-drawer
      v-model:open="mobileMenuVisible"
      placement="right"
      :closable="false"
      class="mobile-drawer"
      width="280"
    >
      <div class="drawer-header">
        <span class="drawer-title">菜单</span>
        <a-button type="text" class="close-btn" @click="mobileMenuVisible = false">
          <CloseOutlined />
        </a-button>
      </div>
      
      <div class="drawer-menu">
        <div class="menu-item" @click="handleMenuClick('/')">
          <HomeOutlined />
          <span>首页</span>
        </div>
        <div class="menu-item" @click="handleMenuClick('/consultation')">
          <MessageOutlined />
          <span>问诊</span>
        </div>
        <div class="menu-item" @click="handleMenuClick('/doctors')">
          <TeamOutlined />
          <span>医生</span>
        </div>
        <div class="menu-item" @click="handleMenuClick('/about')">
          <InfoCircleOutlined />
          <span>关于</span>
        </div>
        
        <a-divider />
        
        <a-button type="primary" class="mobile-login-btn" @click="handleMenuClick('/doctor/login')">
          <UserOutlined />
          医生登录
        </a-button>
      </div>
    </a-drawer>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { HomeOutlined, MessageOutlined, TeamOutlined, InfoCircleOutlined, UserOutlined, MenuOutlined, CloseOutlined } from '@ant-design/icons-vue';

const router = useRouter();
const route = useRoute();
const selectedKeys = ref<string[]>(['home']);
const mobileMenuVisible = ref(false);

watch(() => route.path, (newPath) => {
  if (newPath === '/') {
    selectedKeys.value = ['home'];
  } else if (newPath.startsWith('/consultation')) {
    selectedKeys.value = ['consultation'];
  } else if (newPath.startsWith('/doctors')) {
    selectedKeys.value = ['doctors'];
  } else if (newPath.startsWith('/about')) {
    selectedKeys.value = ['about'];
  }
}, { immediate: true });

const navigateTo = (path: string) => {
  router.push(path);
};

const handleMenuClick = (path: string) => {
  mobileMenuVisible.value = false;
  navigateTo(path);
};
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  padding: 0;
  height: 64px;
  line-height: 64px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo img {
  height: 40px;
  width: 40px;
  border-radius: 8px;
  object-fit: cover;
}

.logo span {
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
}

.nav-menu {
  flex: 1;
  border: none;
  margin: 0 40px;
  line-height: 64px;
}

.login-btn {
  background: #52c41a;
  border-color: #52c41a;
}

.login-btn:hover {
  background: #73d13d;
  border-color: #73d13d;
}

/* 移动端菜单按钮 - 默认隐藏 */
.mobile-menu-btn {
  display: none;
  font-size: 20px;
  color: #333;
}

/* 抽屉样式 */
.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 8px;
}

.drawer-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.close-btn {
  font-size: 18px;
  color: #666;
}

.drawer-menu {
  padding: 0;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 0;
  cursor: pointer;
  transition: all 0.3s;
  color: #333;
  font-size: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.menu-item:hover {
  color: #1890ff;
  background: #f0f5ff;
  padding-left: 8px;
}

.menu-item:last-of-type {
  border-bottom: none;
}

.mobile-login-btn {
  width: 100%;
  background: #52c41a;
  border-color: #52c41a;
  margin-top: 8px;
}

.mobile-login-btn:hover {
  background: #73d13d;
  border-color: #73d13d;
}

/* 响应式布局 */
/* iPad及以下设备 (宽度 <= 1024px) */
@media (max-width: 1024px) {
  .desktop-menu,
  .desktop-login {
    display: none;
  }
  
  .mobile-menu-btn {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .header-content {
    padding: 0 16px;
  }
  
  .logo span {
    font-size: 18px;
  }
  
  .logo img {
    height: 36px;
    width: 36px;
  }
}

/* 手机端优化 */
@media (max-width: 768px) {
  .logo span {
    font-size: 16px;
  }
  
  .logo img {
    height: 32px;
    width: 32px;
  }
  
  .header-content {
    padding: 0 12px;
  }
}

/* 桌面端样式确保 */
@media (min-width: 1025px) {
  .mobile-menu-btn {
    display: none;
  }
  
  .desktop-menu,
  .desktop-login {
    display: flex;
  }
}
</style>
