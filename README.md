# godot-android-firebase-message
Godot Android plugin for the Firebase Messaging


## Usage

### method
* getToken()   
When this is called, _did_receive_token will be called later

### signal
* _did_receive_token(token: String)
* _did_receive_message(message: Dictionary)

```gdscript
if Engine.has_singleton("GodotAndroidFirebaseMessage"):
  var singleton = Engine.get_singleton("GodotAndroidFirebaseMessage")
  singleton.connect("_did_receive_token", self, "_did_receive_token")
  singleton.connect("_did_receive_message", self, "_did_receive_message")
  singleton.getToken()

func _did_receive_token(token: String):
  print(token)
 
func _did_receive_message(message: Dictionary):
  print(message)
```


## Compiling

Steps to build:

1. Clone this Git repository
2. Run `./gradlew build` in the cloned repository
3. copy *.aar and gdap files
```bash
cp app/build/outputs/aar/app-release.aar ${YOUR_PROJECT}/android/plugin/GodotAndroidFirebaseMessage.release.aar
cp GodotAndroidFirebaseMessage.gdap ${YOUR_PROJECT}/android/plugin
```
