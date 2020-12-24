# ConnectivityUtil

Simple util to check Internet connection.

JitPack url
[![](https://jitpack.io/v/gbksoft/ConnectivityUtil.svg)](https://jitpack.io/#gbksoft/ConnectivityUtil)

## Installation
Add to the top level gradle file:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add to the app level gradle:
```
dependencies {
    implementation 'com.github.gbksoft:ConnectivityUtil:0.0.7'
}
```

## Capabilities

- Auto-width columns on-the-fly
- You can use custom layouts as cells
- You can setup width and heights of headers, show or not to show dividers, change dividers colors 


## How to use
`Importing class
```
import com.gbk.soft.connectivity_util.ConnectivityManager
```

`Creating instance
```
val connectivityManager = ConnectivityManager(this)
```
`Checking Internet Connection
`returns true if device is connected to Internet, and false if not 
```
connectivityManager.isOnline
```

# Let us know
This scrollable view android adjustment is not our only original decision. Contact us by email [hello@gbksoft.com](hello@gbksoft.com) to find out more about our projects! Share your feedback and tell us about yourself. 