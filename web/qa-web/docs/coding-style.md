# QA Web 编码规范

## 目录

- [概述](#概述)
- [TypeScript 编码规范](#typescript-编码规范)
- [Vue 组件规范](#vue-组件规范)
- [命名约定](#命名约定)
- [文件组织规范](#文件组织规范)
- [样式规范](#样式规范)
- [注释规范](#注释规范)
- [最佳实践](#最佳实践)

---

## 概述

本文档定义了 QA Live Healthcare 前端项目的编码规范，旨在确保代码质量、可维护性和团队协作的一致性。所有开发人员必须遵循这些规范。

---

## TypeScript 编码规范

### 基本类型

优先使用 TypeScript 类型推断，显式声明复杂类型：

```typescript
// ✅ 推荐 - 简单类型使用类型推断
const count = 0;
const name = 'John';

// ✅ 推荐 - 复杂类型显式声明
interface Doctor {
  id: string;
  username: string;
  name: string;
  title: string;
  department: string;
  isActive: boolean;
}

// ✅ 推荐 - 使用类型别名和接口
type Status = 'pending' | 'answered';

interface Question {
  id: string;
  status: Status;
  answer: string | null;
}
```

### 接口定义

```typescript
// ✅ 推荐 - 接口命名使用 PascalCase
export interface Patient {
  id: string;
  name: string;
  birthday: string;
  phone: string;
  gender: string;
}

// ✅ 推荐 - 使用 Omit、Pick 等工具类型
type NewQuestion = Omit<Question, 'id' | 'submitTime' | 'status'>;

// ✅ 推荐 - 接口继承
interface BaseUser {
  id: string;
  name: string;
}

interface Doctor extends BaseUser {
  title: string;
  department: string;
}
```

### 类型导入

```typescript
// ✅ 推荐 - 明确导入类型
import { Router, RouteRecordRaw } from 'vue-router';
import type { Doctor, Patient } from '../types';

// ✅ 推荐 - 导入 Vue 组合式 API
import { ref, reactive, computed, onMounted } from 'vue';
```

### 函数定义

```typescript
// ✅ 推荐 - 箭头函数用于简单操作
const getDoctorById = (id: string): Doctor | undefined => {
  return state.doctors.find(d => d.id === id);
};

// ✅ 推荐 - 函数方法定义在对象中
export const store = {
  loginDoctor(username: string, password: string): Doctor | null {
    const doctor = state.doctors.find(
      d => d.username === username && d.password === password
    );
    return doctor || null;
  },
  
  // ✅ 推荐 - 明确的返回类型
  getActiveDoctors(): Doctor[] {
    return state.doctors.filter(d => d.isActive);
  }
};
```

---

## Vue 组件规范

### 组件结构

Vue 单文件组件应遵循以下顺序：

```vue
<template>
  <!-- HTML 模板 -->
</template>

<script setup lang="ts">
// TypeScript 逻辑
</script>

<style scoped>
/* CSS 样式 */
</style>
```

### 组合式 API

项目统一使用 `<script setup>` 语法：

```vue
<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { store } from '../store';

// Props 定义
interface Props {
  doctorId: string;
  isActive?: boolean;
}

const props = defineProps<Props>();

// 响应式状态
const loading = ref(false);
const formData = reactive({
  name: '',
  department: ''
});

// 计算属性
const doctor = computed(() => store.getDoctorById(props.doctorId));

// 方法
const handleSubmit = () => {
  // 处理逻辑
};

// 生命周期钩子
onMounted(() => {
  // 初始化逻辑
});
</script>
```

### 组件命名

```typescript
// ✅ 推荐 - 使用 PascalCase 命名组件
import AppHeader from './components/AppHeader.vue';
import DoctorCard from './components/DoctorCard.vue';

// ✅ 推荐 - 组件文件使用 PascalCase
// DoctorCard.vue, PatientForm.vue, ConsultationRoom.vue
```

### Props 和 Emits

```vue
<script setup lang="ts">
// Props 定义
interface Props {
  title: string;
  visible: boolean;
  data?: Doctor;
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  data: undefined
});

// Emits 定义
interface Emits {
  (e: 'update:visible', value: boolean): void;
  (e: 'submit', data: Doctor): void;
}

const emit = defineEmits<Emits>();

// 使用
const handleClose = () => {
  emit('update:visible', false);
};
</script>
```

---

## 命名约定

### 文件命名

```
组件文件：PascalCase
- DoctorCard.vue
- PatientForm.vue
- AppHeader.vue

工具文件：camelCase
- mcpStorage.ts
- dateUtils.ts
- apiClient.ts

类型文件：camelCase
- types.ts
- mcp.ts
- index.ts

配置文件：kebab-case
- vite.config.ts
- tsconfig.json
```

### 变量命名

```typescript
// ✅ 推荐 - 使用 camelCase
const userName = 'John';
const doctorList = [];
const isActive = true;

// ✅ 推荐 - 布尔值使用 is/has/should 前缀
const isLoading = false;
const hasPermission = true;
const shouldRender = computed(() => visible.value);

// ✅ 推荐 - 常量使用 UPPER_SNAKE_CASE
const API_BASE_URL = 'https://api.example.com';
const MAX_RETRY_COUNT = 3;

// ✅ 推荐 - 私有变量使用下划线前缀（可选）
const _privateData = reactive({});
```

### 函数命名

```typescript
// ✅ 推荐 - 动词开头，清晰表达意图
const fetchDoctors = async () => {};
const handleSubmit = () => {};
const validateForm = () => {};
const calculateTotal = () => {};

// ✅ 推荐 - 事件处理使用 handle 前缀
const handleClick = () => {};
const handleChange = (value: string) => {};
const handleSearch = (keyword: string) => {};

// ✅ 推荐 - 计算属性使用名词或形容词
const fullName = computed(() => `${firstName.value} ${lastName.value}`);
const isVisible = computed(() => visible.value && authenticated.value);
```

### CSS 类名

```css
/* ✅ 推荐 - 使用 kebab-case */
.doctor-card {}
.patient-form {}
.active-status {}

/* ✅ 推荐 - BEM 命名法 */
.doctor-card {}
.doctor-card__header {}
.doctor-card__body {}
.doctor-card--active {}

/* ✅ 推荐 - 语义化命名 */
.hero-section {}
.statistics-grid {}
.action-button {}
```

---

## 文件组织规范

### 目录结构

```
src/
├── assets/              # 静态资源（图片、字体等）
├── components/          # 公共组件
│   ├── AppHeader.vue
│   └── AppFooter.vue
├── data/               # 模拟数据（JSON）
├── locales/            # 国际化文件
├── router/             # 路由配置
│   └── index.ts
├── store/              # 状态管理
│   └── index.ts
├── views/              # 页面组件
│   ├── Home.vue
│   └── Doctors.vue
├── App.vue             # 根组件
├── main.ts             # 应用入口
└── style.css           # 全局样式
```

### 导入顺序

```typescript
// 1. Vue 核心库
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';

// 2. 第三方库
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';

// 3. 本地类型定义
import type { Doctor, Patient } from '../types';

// 4. 本地组件
import DoctorCard from './components/DoctorCard.vue';

// 5. 本地工具和状态
import { store } from '../store';
import { formatDate } from '../utils';

// 6. 样式文件
import './styles/custom.css';
```

---

## 样式规范

### 基本原则

1. **使用 scoped 样式**：组件样式默认使用 `scoped` 属性
2. **避免深层选择器**：尽量减少 `:deep()` 和 `>>>` 的使用
3. **响应式设计**：使用媒体查询适配不同屏幕

### CSS 组织

```vue
<style scoped>
/* 1. 布局相关 */
.container {}
.hero-section {}
.grid-wrapper {}

/* 2. 组件样式 */
.doctor-card {}
.patient-form {}

/* 3. 状态样式 */
.active {}
.disabled {}
.loading {}

/* 4. 响应式样式 */
@media (max-width: 768px) {
  .container {
    padding: 16px;
  }
}
</style>
```

### 样式属性顺序

```css
.doctor-card {
  /* 定位 */
  position: relative;
  top: 0;
  z-index: 1;
  
  /* 盒模型 */
  display: flex;
  width: 100%;
  height: auto;
  padding: 24px;
  margin: 16px 0;
  
  /* 边框和背景 */
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  background: #fff;
  
  /* 文字 */
  font-size: 16px;
  color: #333;
  text-align: center;
  
  /* 其他 */
  cursor: pointer;
  transition: all 0.3s;
}
```

### 颜色规范

```css
/* ✅ 推荐 - 使用语义化变量或 Ant Design 色板 */
:root {
  --primary-color: #1890ff;
  --success-color: #52c41a;
  --error-color: #ff4d4f;
  --text-color: #333;
  --border-color: #e8e8e8;
}

/* ✅ 推荐 - 使用 Ant Design 颜色 */
.stat-icon.doctor {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
```

---

## 注释规范

### 文件注释

```typescript
/**
 * 医生数据管理 Store
 * 提供医生登录、状态管理、数据查询等功能
 * 
 * @author Team
 * @date 2026-03-23
 */
```

### 函数注释

```typescript
/**
 * 验证患者身份
 * 根据姓名和生日查找患者，若不存在则创建新患者记录
 * 
 * @param name - 患者姓名
 * @param birthday - 患者生日（格式：YYYY-MM-DD）
 * @returns 患者对象
 * 
 * @example
 * const patient = verifyPatient('张三', '1990-01-01');
 */
verifyPatient(name: string, birthday: string): Patient {
  // 实现逻辑
}
```

### 复杂逻辑注释

```typescript
// ✅ 推荐 - 对复杂逻辑添加注释
const statistics = computed(() => {
  const totalDoctors = state.doctors.length;
  const totalQuestions = state.questions.length;
  // 计算待回复的问诊数量
  const activeSessions = state.questions.filter(q => q.status === 'pending').length;
  // 计算在线医生数量
  const totalSessions = state.doctors.filter(d => d.isActive).length;

  return {
    totalDoctors,
    totalQuestions,
    activeSessions,
    totalSessions,
  };
});
```

### TODO 注释

```typescript
// TODO: 添加医生认证逻辑
// FIXME: 修复时区问题
// HACK: 临时解决方案，后续需要优化
```

---

## 最佳实践

### 性能优化

```typescript
// ✅ 推荐 - 使用 computed 缓存计算结果
const activeDoctors = computed(() => store.getActiveDoctors());

// ✅ 推荐 - 大列表使用虚拟滚动
import { VirtualList } from 'ant-design-vue';

// ✅ 推荐 - 异步组件懒加载
const DoctorRoom = defineAsyncComponent(() => 
  import('./views/DoctorRoom.vue')
);
```

### 错误处理

```typescript
// ✅ 推荐 - 异步操作使用 try-catch
const fetchDoctors = async () => {
  try {
    loading.value = true;
    const data = await api.getDoctors();
    doctors.value = data;
  } catch (error) {
    message.error('获取医生列表失败');
    console.error('Fetch doctors error:', error);
  } finally {
    loading.value = false;
  }
};
```

### 表单验证

```typescript
// ✅ 推荐 - 使用 Ant Design 表单验证
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
};
```

### 国际化

```vue
<template>
  <!-- ✅ 推荐 - 使用 i18n -->
  <h1>{{ t('home.hero.title') }}</h1>
  <p>{{ t('home.hero.subtitle') }}</p>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
</script>
```

---

## 代码审查清单

提交代码前，请确保：

- [ ] 所有 TypeScript 类型定义正确
- [ ] 组件使用 `<script setup>` 语法
- [ ] 变量和函数命名清晰、符合规范
- [ ] 样式使用 `scoped` 属性
- [ ] 添加必要的注释
- [ ] 无 console.log 残留（错误处理中的除外）
- [ ] 响应式设计已测试
- [ ] 国际化文本已配置

---

## 参考资料

- [Vue 3 官方文档](https://cn.vuejs.org/)
- [TypeScript 官方文档](https://www.typescriptlang.org/)
- [Ant Design Vue 文档](https://antdv.com/)
- [Vue 风格指南](https://cn.vuejs.org/style-guide/)

---

**最后更新**: 2026-03-23  
**维护者**: QA Live Healthcare Team
