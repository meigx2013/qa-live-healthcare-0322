import { createI18n } from 'vue-i18n';
import zhCn from './locales/zh-cn.json';
import enUs from './locales/en-us.json';

const messages = {
  'zh-cn': zhCn,
  'en-us': enUs
};

const savedLocale = localStorage.getItem('locale') || 'zh-cn';

const i18n = createI18n({
  legacy: false,
  locale: savedLocale,
  fallbackLocale: 'zh-cn',
  messages
});

export default i18n;
