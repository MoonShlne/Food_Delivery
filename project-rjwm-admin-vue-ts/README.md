## 3工程结构解析

```
├── public                     # 静态资源 (会被直接复制)
│   │── favicon.ico            # favicon图标
│   │── manifest.json          # PWA 配置文件
│   │── img                    # 静态图片存放
│   └── index.html             # html模板
├── src                        # 源代码
│   ├── api                    # 所有请求
│   ├── assets                 # 主题 字体等静态资源 (由 webpack 处理加载)
│   ├── components             # 全局组件
│   ├── icons                  # svg 图标
│   ├── layout                 # 全局布局
│   ├── router                 # 路由
│   ├── store                  # 全局 vuex store
│   ├── styles                 # 全局样式
│   ├── utils                  # 全局方法
│   ├── views                  # 所有页面
│       ├── category           # 分类管理
│       ├── employee           # 员工管理
│       ├── dish               # 菜品管理、添加菜品
│       ├── login              # 登录
│       ├── package            # 套餐管理、添加套餐
│       ├── orders             # 订单
│       ├── orderDetail        # 订单明细
│       ├── chart              # 表单
│       └── 404.vue            # 404报错页面
│   ├── App.vue                # 入口页面
│   ├── main.js                # 入口文件 加载组件 初始化等
│   ├── permission.ts          # 权限管理
│   └── shims-vue.d.ts         # 模块注入
├── dist                       # 打包文件夹（可删除重新打包）
├── tests                      # 测试
├── .circleci/                 # 自动化 CI 配置
├── .browserslistrc            # browserslistrc 配置文件 (用于支持 Autoprefixer)
├── .editorconfig              # 编辑相关配置
├── .env.xxx                   # 环境变量配置
├── .eslintrc.js               # eslint 配置
├── babel.config.js            # babel-loader 配置
├── cypress.json               # e2e 测试配置
├── jest.config.js             # jest 单元测试配置
├── package.json               # package.json 依赖
├── postcss.config.js          # postcss 配置
├── tsconfig.json              # typescript 配置
└── vue.config.js              # vue-cli 配置

```

### 文档参考

- vue[https://cn.vuejs.org/v2/guide/]

## 4 部署运行指南

#### 图片预览地址服务器部署需要修改 baseUrl src/config.json

# 例如

```
{
  "baseUrl": "http://172.17.0.60:8200" 此地址修改为服务器部署地址
}
```

##### 安装依赖

```bash
yarn install/npm install
```

##### 启动本地开发环境（自带热启动）

```bash
yarn serve/npm run serve
```

##### 压缩打包 <打包生成文件为 dist 文件夹>

##### 线上 去掉删除功能

```bash
yarn build/npm run build
```

##### 测试 保留删除功能

```bash
yarn build:uat/npm run build:uat
```

