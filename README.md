# Weather Forecast app readme

## Software development principles, patterns & practices
### For app architecture, I applied Clean Architecture + MVVM:
- Reference: https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html

#### Reason: design a banking architecture can expose business and repository to:
- Reuse usecases among viewmodels
- Long live on technology change
- Reuse among apps in android (app banking for consumer, app banking for sme, app for sold trader)
- Reuse business (maybe repository) among platforms (android and iOS, just have prototype)

#### Layers:
- business layer: to present business, should depend on Kotlin only, not android 
- repository layer: to present how to get data, should depend on business
- presentation layer:  to present how to show data, should depend on business and repository

### For source code, I applied Clean code guideline such as:
- Variable name should be meaningful
- Function just do one thing
- Class just serve for one actor
- Modules should call each other via interfaces
- Source code is arranged by features not by function
- Code must be testable
- Shouldn't have a large source code file
- Format code, optimize import, check error and warning to fix (optional)

### Follow SOLID principles to design classes:
- SRP – Single Responsibility Principle.
- OCP – Open/Closed Principle.
- LSP – Liskov Substitution Principle.
- ISP – Interface Segregation Principle.
- DIP – Dependency Inversion Principle.

### Using network-bound-resource pattern to cache data:
- Reference: https://developer.android.com/topic/libraries/architecture/images/network-bound-resource.png

### Apply security practises:
- Checking root
- Prevent revert engineering
- Prevent take screen-shot and record video
- Don't allow backup
- Using SSL pinning
- Encrypt database(for sensitive data)

### Using Observer pattern for data flow:
- Using livedata between modelview and fragment
- Using callabck between usecase and modelview

### Using Dependency injection and scope:
- Using Dependency injection
- Using scope for object, global scope and view-scope

### Configure multi environments:
- SIT: testing environment with configuration
- PRO: production environment with configuration

### Testing practises:
- Unit test: viewmodel test, usecase test, repository test
- UI test: fragment test
- Check unit test code coverage(jacoco)

## Code structure
### app module: presentation layer which included UI code
- /src/main -> for UI features
- /src/pro --> for config production environment
- /src/sit --> for config test environment
- /src/../core -> common things which used in application.
- /src/../weather/di -> for dependency injection
- /src/../weather/view -> for view item such as fragment, adapter
- /src/../weather/viewmodel -> for viewmodel

### business module: business layer, include business code
- /src/../weather/usecase --> include business usecase and implementation
- /src/../weather/repository --> include interface to get data for usecase
- /src/../weather/info --> include business entities
- /src/../weather/callback --> provide interface to notify result to viewmodel.

### repository module: data layer, include data code
- /src/../weather/remote/model -> include model to retrieve back-end data (Object from Gson)
- /src/../weather/remote/repository -> include a implementation of repository in usecase(network)
- /src/../weather/remote/service -> include retrofit service
- /src/../weather/local/model -> include model to retrieve database
- /src/../weather/local/repository -> include a implementation of repository in usecase (database)
- /src/../weather/local/service -> include database service
- /src/../weather/cache/repository -> include a implementation of repository in usecase (combine)

## Java/Kotlin libraries and frameworks:
- Architecture: Clean + MVVM
- Dependency Injection: Koin
- Network call: Retrofit + OkHttp
- Json parser: Gson
- Cache: Using SqlCipher
- Multi-thread: Kotlin Coroutine
- Testing: Mockito for Unit Test, espresso and mockK for UI test
- CodeCoverage: jacoco
- DAO: Room
- Observer data: LiveData + callback
- Decompile apk and rooted detection: using code and proguard

## How to run?
### Please run below commands:
- Using below command to create a apk test file
```
./gradlew clean assembleSitDebug
```
- Or creating apk test file and install it
```
./gradlew clean installSitDebug
```
### In order to verify UT, please run below command:
```
   ./gradlew :app:testSitDebugUnitTest
   ./gradlew :business:testDebugUnitTest
   ./gradlew :repository:testDebugUnitTest
```

### In order to verify UT with code coverage, please run below command:
```
   ./gradlew testSitDebugUnitTestCoverage
   ./gradlew :business:testDebugUnitTestCoverage
   ./gradlew :repository:testDebugUnitTestCoverage
```
Example as below will be log in console:
```
> Task :repository:testDebugUnitTestCoverage
- instruction coverage rate is: 70.23%, minimum is 70%
- branch coverage rate is: 70.23%, minimum is 70%
- line coverage rate is: 70.23%, minimum is 70%
- complexity coverage rate is: 70.23%, minimum is 70%
- method coverage rate is: 70.23%, minimum is 70%
- class coverage rate is: 70.23%, minimum is 70%
```
### In order to verify UI test, please run below command, note: android OS >= android P:
```
   ./gradlew connectedAndroidTest
```

### In order to only verify decompike apk or rooted device or emulator, please run below command:
- Using below command to create a apk test file
```
./gradlew clean assembleProRelease
```
- Or creating apk test file and install it
```
./gradlew clean installProRelease
```
## What I done so far?
### Business checklist: all
1. The application is a simple Android application which is written by Java/Kotlin.
2. The application is able to retrieve the weather information from OpenWeatherMaps
API.
3. The application is able to allow user to input the searching term.
4. The application is able to proceed searching with a condition of the search term length
must be from 3 characters or above.
5. The application is able to render the searched results as a list of weather items.
6. The application is able to support caching mechanism so as to prevent the app from
generating a bunch of API requests.
7. The application is able to manage caching mechanism & lifecycle.
8. The application is able to handle failures.
9. The application is able to support the disability to scale large text for who can't see the
text clearly.
10. The application is able to support the disability to read out the text using VoiceOver
controls.

### Technical checklist:
Programming language:
1. Kotlin is required, Java is optional.
- Status: Done
2. Design app's architecture (suggest MVVM)
- Status: Done
- Note: this app apply Clean + MVVM
3. Apply LiveData mechanism
- Status: Done
- Note: Live Data is used to communicate between viewmodel and fragment
4. UI should be looks like in attachment.
- Status: Done
- Note: Look like but not the same, a search view is added with "X" icon on the right and "search"
   icon on the left, it's more friendly with end-users.
5. Write Unit Tests
- Status: Done
- Note: using below command to run unit test for each of module: app, business, repository
```
   ./gradlew :app:testSitDebugUnitTest
   ./gradlew :business:testDebugUnitTest
   ./gradlew :repository:testDebugUnitTest
```
6. AcceptanceTests
- Status: done
- Note: app is tested and cover errors.
7. Exceptionhandling
- Status: done
- Note: there are 3 common message in app as below:
       "Something went wrong" is a kind of unknown error
       "Something went wrong please check IO connection" is a kind of http error such as BAD GATEWAY
       "Fail by load data, Error:..., reason:..." is kind of fail from Open Weather
8. Cachinghandling
- Status: done
- Note: app use sql to cache data in local, data will be search from local before it will be found
    via network.
9. SecureAndroidappfrom:
##### DecompileAPK
- Status: done
   In other to create a protected apk, using this command:
```
   ./gradlew clean assembleProRelease
```
##### Rooteddevice
- Status: done
- Note: Using below command to create release apk
```
    ./gradlew clean assembleProRelease
```
   Then install to a rooted device, it will show "You may run app in unsafe device, please change
   device or use internet version"
##### Data transmission via network
- Status: done
- Note: I used SSL pinning to trust open weather website.
In the source code, we can find at 
```
"com.architecture.cleanmvvm.core.di.RepositoryModule"
```
##### Encryption for sensitive information
- Status: done
- Note: app used sqlcipher to encrypt database, we can save sensitive data with low security require.
In the source code you can find at 
```"package com.architecture.repository.weather.local.service.WeatherDatabase"```
10. Accessibility for Disability Supports:
##### Talkback:Useascreenreader.
- Status: done
- Note: all of views content description are added text to support for Accessibility
##### Scaling Text: Display size and font size: To change the size of items on your screen, adjust the display size or font size.
- Status: done
- Note: add configuration to activity to apply font-change in the system.
11. Please refer documents for database and clean architecture at path/weatherdemo/diagram

## Need to improve
- Modularization: separate features in modules in order to keep it clean, decoupling
- Add BBD to test usecase and repository by using Cucumber.
- Build a base class for Viewmodel, in base we will handle common cases such common failures cases.
- Maybe we try to apply navigation among fragments.
- Add mock modules for development, let say we test with mock when we wait for real api.
- Add feature toggle to turn on/off new feature(maybe local)
- Add Loggging in whole app