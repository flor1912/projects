# SwipeBite – Tinder-style Restaurant Discovery App

SwipeBite is a modern Android application that helps users discover restaurants using a familiar **Tinder-style swipe interaction**.  
Instead of scrolling through long lists, users can **swipe right to save** and **swipe left to skip**, making restaurant discovery fast, fun, and intuitive.

This project showcases **modern Android development with Jetpack Compose**, clean architecture, real API integration, and local persistence using **DataStore + Kotlin Serialization**.

---

## Features

-  Tinder-style swipe cards for restaurant discovery  
-  Swipe right to save restaurants to **Favorites**  
-  Swipe left to dismiss  
-  Location-based search (City & Country)  
-  Dietary filters (Everything / Vegetarian / Vegan)  
-  Distance filter (1–50 km)  
-  Custom keyword filtering  
-  Persistent favorites stored locally with DataStore  
-  State preserved across navigation  
-  Consistent custom UI theme built with Compose Material 3  

---

## 🧠 Technical Highlights

- **Jetpack Compose** UI (Material 3)
- **MVVM architecture**
- **ViewModel + StateFlow** for reactive state
- **DataStore Preferences** for persistence
- **Kotlin Serialization** for storing complex objects
- **Overpass API (OpenStreetMap)** for real restaurant data
- **Swipeable card gesture library**
- Stable item keys to avoid recomposition issues
- Favorites deduplication & legacy data migration
- Navigation with **Navigation Compose**

---

##  Architecture

UI (Compose Screens)
│
├── DiscoverScreen → Swipe cards, loads restaurants
├── FavoritesScreen → Persistent saved restaurants
├── PreferenceScreen → Filters & search settings
│
ViewModel (DiscoverViewModel)
│
Repository (RestaurantRepository)
│
Network (Overpass API)
│
Local Storage (DataStore + Kotlin Serialization)
