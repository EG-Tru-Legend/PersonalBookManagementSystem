# Personal Book Management System

An Android app built with Kotlin and Jetpack Compose to help track your reading list, reading progress, and book details in a clean local-first experience.

## Features

- Add, edit, and delete books
- Track reading progress by percentage and current page
- Search books by title or author
- Filter by genre and sort by title
- View book details on a dedicated screen
- Share individual book details or your filtered book list via Android share sheet (email/messages/etc.)
- Local persistence with Room database (offline support)

## Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- Android Architecture Components (ViewModel, StateFlow)
- Room (local database)
- Navigation pattern with drawer + screen state
- Gradle Kotlin DSL

## Project Structure

- model: Room entities, DAO, database setup
- viewmodel: app state and business logic (search/filter/sort/progress)
- view: main UI screens (book list, add book, book detail)
- navigation: top bar, drawer, and screen routing/state
- utils: reusable UI components, dialogs, sharing utilities

## Requirements

- Android Studio (latest stable recommended)
- Android SDK:
  - compileSdk: 35
  - minSdk: 24
  - targetSdk: 35
- JDK 11

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or physical Android device

## How It Works

- Books are stored in a local Room database
- The ViewModel loads and updates books through DAO operations
- UI observes StateFlow values to update automatically
- Progress can be updated either by percentage or current page
- Filtering, sorting, and searching are applied in ViewModel state combination

## Current Status

MVP is functional with core CRUD, progress tracking, and sharing.

## Roadmap Ideas

- Add reading goals (weekly/monthly)
- Add due dates and reminders
- Add cover image support
- Add export/import backup
- Add unit/UI tests for key flows
