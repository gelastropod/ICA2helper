# CS4131 ICA 2 Helper Code, AY2025
## Usage:
Clone this repository and use it as a starting template!
## Documentation:
### Important functional APIs
#### Notifications
- ```NotificationComposables.sendNotification(context: Context, title: String, message: String)```: send a single notification
#### PiP
- ```PictureInPictureComposables.VideoScreen(videoID: Int)```: show a video frame and a button to switch into PiP mode, ```videoID``` refers to the ```R.raw.<video_name>``` value
#### **Speech** to Text
- ```SpeechToTextComposables.minimalSTT(viewModel: SpeechToTextViewModel)```: creates a ```Text``` which will show the transcribed text and a button that starts/stops the transcription, the ```SpeechToTextViewModel``` can be initiated in ```MainActivity.kt```
#### **Text** to Speech
- ```TextToSpeechComposables.speakText(viewModel: TextToSpeechViewModel, text : String)```: says the string ```text```
- ```TextToSpeechComposables.minimalTTS(viewModel: TextToSpeechViewModel, initialString : String)```: shows a ```TextField``` which the user can enter the string to be spoken and a button that plays the speech
#### Read in text file
- ```readRawTextFile(context: Context, resourceId: Int)```: reads a text file in ```res/Raw``` as supplied by ```R.raw.<textFileName>```
### Troubleshooting
- Make sure all dependencies are loaded correctly
- Make sure relevant permissions are authorised on your android device
