When the app first starts up, the invoked callbacks are

    onCreate()
    onStart()
    onResume()


When the "phone" is rotated, the invoked callbacks are

    onPause()
    onStop()
    onDestroy()
    onCreate()
    onStart()
    onResume()

And when the "phone" is turned off, the callbacks are

    onPause()
    onStop()