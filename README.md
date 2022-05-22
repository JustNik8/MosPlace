# MosPlace
## Light theme
![Light theme](./assets/screens_light_theme.jpg)

## Dark theme
![Dark theme](./assets/screens_dark_theme.jpg)

# About
An application was created for users, that likes achievements. The application contains various places, like landmarks, restaurants, parks, that are grouped by districts of Moscow. The user can interact with a place. For example, he can mark the place as visited, rate. In the future, the ability to leave a review and attach a photo will be added.
Each user has a profile. In the plans: in the profile, user can see achievements, progress indicator of visited places, etc.

# Technolohies
* [Kotlin](https://kotlinlang.org/) - Modern, concise and safe programming language. Recommended by Google for building Android apps.
* [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - For asynchronous work and more.
* [Retrofit2](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
* [Hilt](https://dagger.dev/hilt/) - Dependency injection library. Hilt is built on top of [Dagger](https://dagger.dev/dev-guide/).
* [Android Architecture Components](https://developer.android.com/topic/architecture) - Is a suite of libraries to help developers follow best practices, reduce boilerplate code.
  * [View Binding](https://developer.android.com/topic/libraries/view-binding) - Is a feature that allows you to more easily write code that interacts with views.
  * [Navigation Component](https://developer.android.com/guide/navigation) - Allows you to simplify the implementation of navigation between screens in the application.
  * [Flow](https://kotlinlang.org/docs/flow.html) - A flow is an asynchronous version of a Sequence, a type of collection whose values are lazily produced.
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Class is designed to store and manage UI-related data in a lifecycle conscious way.
  * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Is an Android Jetpack library that makes it easy to schedule deferrable, asynchronous tasks that must be run reliably.
* [Glide](https://github.com/bumptech/glide) - Is a fast and efficient open source media management and image loading framework for Android.
* [Yandex MapKit](https://yandex.ru/dev/maps/mapkit/?from=mapsapi) - A software library that allows you to use Yandex cartographic data and technologies in mobile applications.
* [ViewBindingPropertyDelegate](https://github.com/androidbroadcast/ViewBindingPropertyDelegate) - A library, that makes work with Android View Binding simpler.

# Architecture
This app uses [MVVM](https://developer.android.com/jetpack/guide#recommended-app-arch) architecture.
![MVVM](https://miro.medium.com/max/960/0*-ZJZfLhup-7rg0cy.png)

# How to build
1. Create free api key for Mapkit - Mobile SDK: https://developer.tech.yandex.ru/
2. Create a file apikey.properties in root project directory
3. Add following line to file apikey.properties: ```MAPKIT_API_KEY="your_api_key"```
4. Build project

# License
```
Copyright 2022 Nikita Leontev 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
