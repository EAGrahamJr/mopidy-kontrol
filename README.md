# mopidy-kontrol

A **simple** client for controlling [mopidy](https://mopidy.com/). This uses the [Web Socket](https://docs.mopidy.com/en/latest/api/http/#websocket-api) interface.

[Java Docs](https://eagrahamjr.github.io/mopidy-kontrol/)

![Just Build](https://github.com/EAGrahamJr/HAssK/actions/workflows/build.yaml/badge.svg) ![Kotlin](https://badgen.net/badge/Kotlin/1.9.10/purple)  ![Java](https://badgen.net/badge/Java/17/orange) ![Apache License](https://badgen.net/github/license/EAGrahamJr/HAssK)

## Again, **WHY**??

Yet another small concoction to integrate with my "Kobots" robotic/home-automation/hardware-ish hacking projects. The idea is to _not_ build a fully functional, do-everything client: that's been done (as usual with OSS, to overkill :grinning:).

The idea is to just stop/start/play/pause/volume/mute and **that's it**. It's to be used in the ecosystem to just _barely_ manage playback via external "events" (e.g. a button on my mad [BBoard](https://github.com/EAGrahamJr/kobots-buttonboard) project). This is being built out as a library so I can change my mind about where it's used later...

JSON serialization is done via the "reference" implementation from [org.json](https://github.com/stleary/JSON-java)

## TODO

Stuff maybe...

- get/set mixer values
- parse events

## Building

This project uses [Gradle](https://gradle.org), so the only thing you need is a compatible JDK<sup>**1**</sup>. Additionally, because the project is [Kotlin](https://kotlinlang.org) and uses the _Kotlin Gradle plugin_, a Kotlin installation is also not necessary.

A default build will use the [gradle-plugins](https://github.com/EAGrahamJr/gradle-scripts) to publish to the "local" Maven repository.

[Documentation](docs) is created via the `dokka` plugin: Javadocs **are** created on build (but not published, yet).

---

<sup>**1**</sup>Java 17 is the current build target.
