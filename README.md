### Qonversion Android SDK + WorkManager issue sample project

This sample project demonstrates how `Qonversion.shared.offerings` will not invoke any of the callbacks while the app is not actively in foregorund (see [Qonversion Android SDK issue #549](https://github.com/qonversion/android-sdk/issues/549)).

1. Open Application.kt and set a proper `QONVERSION_PROJECT_KEY`.
2. Filter Logcat for `tag:astuetz`.
3. Run the app and tap "Schedule Worker", this will enqueue a Worker to run in ~15 seconds (it usually takes a bit longer).
4. Without doing anything, wait for the Worker to run and you'll see that `onSuccess` is invoked and the main offering is printed.
5. Tap the button again and kick the app from recents right afterwards.
6. Wait for the Worker to be invoked, this time it will get stuck after calling `Qonversion.shared.offerings`. :x:
7. As soon as the app is opened again, the onSuccess callback will be invoked.
