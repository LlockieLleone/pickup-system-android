# Smart Childcare Management System - Teacher Android App

# 托管班管理系统 - 教师端安卓应用

Android-based teacher-side mobile application for childcare centers and after-school pickup management.

基于 Android 的托管班教师端移动应用，面向托管班与课后接送管理场景。

---

# 📌 Overview ｜ 项目简介

This repository contains the Android teacher-side mobile application of the Smart Childcare Management System.

The app is designed for teachers and pickup staff to manage student pickup tasks, attendance workflows, and real-time task confirmation.

该仓库包含智能托管班管理系统的教师端安卓应用。

当前应用主要用于教师与接送人员处理学生接送任务、考勤流程与实时任务确认。

---

# ✅ Current Status ｜ 当前开发状态

## Completed ｜ 已完成

### Android Architecture

* Kotlin Android project structure
* Jetpack Compose UI architecture
* MVVM architecture
* Navigation Compose integration
* Retrofit API encapsulation
* StateFlow state management

### Business Features

* Teacher login page
* JWT login integration
* Daily pickup task display
* Student pickup confirmation workflow
* Backend API communication
* Local token persistence

### Engineering

* Release APK generation
* Android signing configuration
* Environment separation
* Production API integration
* Git version control integration

已完成内容：

### 安卓基础架构

* Kotlin 安卓项目结构
* Jetpack Compose UI 架构
* MVVM 架构
* Navigation Compose 路由集成
* Retrofit 网络请求封装
* StateFlow 状态管理

### 业务功能

* 教师登录页面
* JWT 登录接入
* 今日接送任务展示
* 学生接送确认流程
* 后端 API 通信
* Token 本地持久化

### 工程化

* Release APK 生成
* Android 应用签名配置
* 环境隔离
* 生产 API 接入
* Git 版本管理接入

---

# 🛠 Tech Stack ｜ 技术栈

## Android

* Kotlin
* Jetpack Compose
* Navigation Compose
* Retrofit2
* Gson
* StateFlow
* MVVM Architecture

## Communication

* RESTful API
* JWT Authentication

---

# 🏗 System Architecture ｜ 系统架构

```text
Teacher Android App
          ↓
      RESTful API
          ↓
 Spring Boot Backend
          ↓
        MySQL
```

Current repository scope:

* Android teacher-side app only

Not included in this repository:

* Spring Boot backend service
* Vue admin panel

当前仓库仅包含：

* 教师端安卓应用

暂未包含：

* Spring Boot 后端服务
* Vue 管理后台

---

# ✨ Main Features ｜ 当前功能

## Authentication ｜ 用户认证

* JWT login integration

* Login state persistence

* Token local storage

* JWT 登录接入

* 登录状态持久化

* Token 本地存储

---

## Daily Pickup Tasks ｜ 今日接送任务

* Daily pickup task display

* Student task information

* Real-time task updates

* 今日接送任务展示

* 学生任务信息展示

* 实时任务状态更新

---

## Pickup Confirmation ｜ 接送确认

* Student pickup confirmation

* Arrival confirmation

* Pickup completion confirmation

* 学生接到确认

* 到达确认

* 接送完成确认

---

## Backend Communication ｜ 后端通信

* Retrofit API integration

* Token request injection

* Error handling

* Production environment API support

* Retrofit 接口通信

* Token 自动注入

* 错误处理

* 生产环境 API 支持

---

# 🔒 Current Security Measures ｜ 当前安全措施

The current Android app includes:

* JWT token authentication
* Local token persistence
* Release signing support
* Production environment isolation
* API authorization support

当前安卓应用已包含：

* JWT Token 鉴权
* Token 本地持久化
* Release 正式签名
* 生产环境隔离
* API 权限验证支持

---

# 🚧 Future Improvements ｜ 后续开发方向

The project is still under active development.

Planned improvements include:

* Push notification support
* QR code verification
* Real-time location updates
* Offline cache support
* Dark mode support
* Attendance analytics
* Crash monitoring
* HTTPS-only communication
* CI/CD Android pipeline

项目目前仍在持续开发中。

后续计划包括：

* 推送通知支持
* 二维码接送验证
* 实时位置更新
* 离线缓存支持
* 深色模式支持
* 考勤数据分析
* 崩溃监控
* HTTPS 安全通信
* Android 自动化部署流程

---

# 🚀 Build ｜ 构建方式

## Debug Build ｜ Debug 构建

```bash
./gradlew assembleDebug
```

---

## Release Build ｜ Release 构建

```bash
./gradlew assembleRelease
```

---

## Release APK Output ｜ Release APK 输出路径

```text
app/build/outputs/apk/release/
```

---

# ⚙ Environment Configuration ｜ 环境配置

## Debug Environment

```text
http://10.0.2.2:8080/
```

---

## Production Environment

```text
http://8.134.184.238/pickup/service/
```

备案与 HTTPS 完成后将切换为正式域名。

---

# 🔑 Release Signing ｜ Release 签名

The release APK is signed using a private JKS keystore.

Important:

* JKS files are NOT committed to GitHub
* Signing credentials are stored locally only
* Release APK is generated manually

---

# 📂 Repository Scope ｜ 仓库范围说明

This repository currently contains only the Android teacher application module.

Backend services and admin web applications are maintained separately.

当前仓库仅包含教师端安卓应用模块。

后端服务 https://github.com/LlockieLleone/pickup-system-service
管理后台 https://github.com/LlockieLleone/pickup-system-admin

---

# 👨‍💻 Author ｜ 作者

Lingjun Liao
University of Ottawa
Computer Engineering
