# GitStat
GitStat is a simple android app designed to aggregate Github profile data into informative cards and graphs.

<img src="doc/logo.png" width="20%"/>

### Screenshots
<p align="center">
<img src="doc/screenshot_login.png" width="30%"/>
<img src="doc/screenshot_repositories_stat.png" width="30%"/>
<img src="doc/screenshot_filters.png" width="30%"/>
</p>


### Technology (some notes)

- Used single activity approach and [Navigation component](https://developer.android.com/guide/navigation) to navigate across fragments.
- [View binding](https://developer.android.com/topic/libraries/view-binding) is used to interact with views within fragments and recyclerview adapters.
- Kotlin coroutines are used for asynchronous operations.
- [Retrofit](https://github.com/square/retrofit) is used to perform [Github API](https://docs.github.com/en/rest) calls to obtain the data. Also use [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor) to log requests.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) database is used for cache implementation.
- [colors.json]() file is borrowed from the [github-colors](https://github.com/ozh/github-colors) repository and parsed with [Gson](https://github.com/google/gson) library in order to obtain and display native github colors for programming languages.
- Google's [FlexboxLayout](https://github.com/google/flexbox-layout) as LayoutManager and custom checkable LinearLayout are 
used to implement languages filter (see third screenshot).
- [CircleImageView](https://github.com/hdodenhof/CircleImageView) and [Picasso](https://github.com/square/picasso) libs are involved to obtain and display user profile image.
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) library is used for plots and diagrams in the application.


### How to authorize
- Download APK from the [releases](https://github.com/alexandr7035/gitstat/releases) page and install it.
- Go to [Personal access tokens](https://github.com/settings/tokens) section in your Github profile settings.
- Create personal access token with ```read:user``` and ```repo``` access scopes. (**Note**: full ```repo``` scope is used only to have access to your private repos data. No malicious write operations are performed by the app).
- Use the obtained token as auth credetial in the application login form.

More user-friendly auth method may be implemented later. Authorization process in the app **needs additional research and refactoring**.
