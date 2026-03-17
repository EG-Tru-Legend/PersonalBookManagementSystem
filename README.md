<div align="center">

# 📚 Personal Book Manager

**A clean, local-first Android app for tracking your reading list and progress.**

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room_DB-FF6F00?style=flat&logo=android&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-24-brightgreen?style=flat)

</div>

---

## Overview

Personal Book Manager is an Android app built with **Kotlin and Jetpack Compose** that lets you manage your reading list entirely offline. Track progress, filter by genre, search by author, and share your list — all without a backend.

---

## Features

- 📖 Add, edit, and delete books
- 📊 Track reading progress by percentage or current page
- 🔍 Search by title or author
- 🏷️ Filter by genre and sort by title
- 📤 Share book details or your full filtered list via Android share sheet
- 💾 Fully offline — local persistence with Room database

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose (Material 3) |
| State management | ViewModel + StateFlow |
| Database | Room (local, offline-first) |
| Navigation | Drawer + screen state pattern |
| Build | Gradle Kotlin DSL |

---

## Project structure

```
├── model/          # Room entities, DAO, database setup
├── viewmodel/      # App state, business logic (search/filter/sort/progress)
├── view/           # UI screens (book list, add book, book detail)
├── navigation/     # Top bar, drawer, screen routing
└── utils/          # Reusable UI components, dialogs, sharing utilities
```

---

## Getting started

**Requirements**
- Android Studio (latest stable)
- compileSdk 35 / minSdk 24 / targetSdk 35
- JDK 11

**Setup**
```bash
git clone https://github.com/EG-Tru-Legend/[repo-name]
```
1. Open in Android Studio
2. Sync Gradle
3. Run on emulator or physical device

---

## Status

MVP is functional with core CRUD, progress tracking, search, filtering, and sharing.

**Roadmap**
- [ ] Reading goals (weekly/monthly targets)
- [ ] Due dates and reminders
- [ ] Cover image support
- [ ] Export/import backup
- [ ] Unit and UI tests
