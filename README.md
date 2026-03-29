# DayLeaf3 note editor
An Android app to let user jot down daily note.

## Usage
Launch the app and edit note.

In the top bar:
- Date in local timezone is shown,
- Tap ↓ to export notes that have not been exported since edited as a text file,
- Tap &lt; to view and edit previous note,
- Tap &gt; to view and edit next note, and
- Tap &gt;&gt; to view and edit today's note

Tap the space below to start editing the note.

## Privacy policy
This privacy policy states use of information of the user by a software application, DayLeaf3.

The application stores notes created by the user on the device. Exported notes are accessible by other apps on the device.

## License
Copyright 2026 by Green Soybean Technologies &lt;edamametech at gmail.com&gt;, under [MIT License](LICENSE).

## Development
### Releasing the app
1. Remove unreferenced resources and debug logs.
1. Update `android.defaultConfig.versionCode` and `...versionName` in `app/build.gradle.kts`.
1. Test things on debug build, generate screenshots if necessary.
1. Build - Generate Signed App Bundle or APK... and select release build.
1. Rename the signed app bundle `app/release/app-release.aab` with the version, i.e., `X.Y.aab`.
1. Upload the signed app bundle to developer console, wait for quick checks to complete.
1. Upload screenshots if necessary
1. Tag the working copy: `git tag vX.Y`
1. Push changes to origin: `git push; git push origin tag vX.Y`

