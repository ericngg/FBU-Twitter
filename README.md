# Project 3 - SimpleTweet

**SimpleTweet** is an android app that allows a user to view their Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **18** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X]	User can **sign in to Twitter** using OAuth login
* [X]	User can **view tweets from their home timeline**
  * [X] User is displayed the username, name, and body for each tweet
  * [X] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
* [X] User can **compose and post a new tweet**
  * [X] User can click a “Compose” icon in the Action Bar on the top right
  * [X] User can then enter a new tweet and post this to Twitter
  * [X] User is taken back to home timeline with **new tweet visible** in timeline
  * [X] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh
* [X] User can **see a counter with total number of characters left for tweet** on compose tweet page
* [X] User can **pull down to refresh tweets timeline**
* [X] User can **see embedded image media within a tweet** on list or detail view.

Time spent: **6.5** hours spent in total

The following **stretch** features are implemented:

* [X] User is using **"Twitter branded" colors and styles**
* [X] User sees an **indeterminate progress indicator** when any background or network task is happening
* [X] User can **select "reply" from home timeline to respond to a tweet** (I changed it to the detail tweet because I thought it looked nicer)
  * [X] User that wrote the original tweet is **automatically "@" replied in compose**
* [X] User can tap a tweet to **open a detailed tweet view**
  * [X] User can **take favorite (and unfavorite) or retweet** actions on a tweet
* [X] User can view more tweets as they scroll with infinite pagination
* [X] Compose tweet functionality is built using modal overlay
* [X] User can **click a link within a tweet body** on tweet details view. The click will launch the web browser with relevant page opened.
* [X] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.org/android/Drawables#vector-drawables) where appropriate.
* [X] User can view following / followers list through any profile they view.
* [X] Use the View Binding library to reduce view boilerplate.
* [X] On the Twitter timeline, apply scrolling effects such as [hiding/showing the toolbar](http://guides.codepath.org/android/Using-the-App-ToolBar#reacting-to-scroll) by implementing [CoordinatorLayout](http://guides.codepath.org/android/Handling-Scrolls-with-CoordinatorLayout#responding-to-scroll-events).
* [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.

**I implemented this last bonus and it sort of worked. However, I did something else and it ended up breaking my code. So I reverted to before implementing this bonus and decided that my final product will be without the last bonus feature.**

Time spent: **10.5** hours spent in total

The following **additional** features are implemented:


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/ericngg/FBU-Twitter/blob/main/twitterdemo.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIFs created with [screen2gif](https://www.screentogif.com/).

## Notes

Describe any challenges encountered while building the app.

Noted earlier: **I implemented this last bonus and it sort of worked. However, I did something else and it ended up breaking my code. So I reverted to before implementing this bonus and decided that my final product will be without the last bonus feature.**

I also think there are way too many bonuses that we are asked to implement in this assignment. I feel like at a certain point, my code is pretty cluttered with some useless code.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2021] [Eric Ng]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
