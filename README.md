# QRIS Scanner Example

## Architecture

This repo uses the Model-View-ViewModel architecture in the App layer with the Model being handled by the Domain and Data layers.
The Data layer handles the fetching and inserting of the data into the Room database (it would also be used for API calls in other cases).
The Domain layer preprocesses the data to be inserted in the Data layer and aggregates the data fetched from the Data layer and changes said data into a format displayable by the App layer
The App layer's main responsibility is displaying the data to the user using Jetpack Compose for UI and the ViewModel to manage the composables' states.

The diagram is as follows:
![General Architecture Diagram](https://github.com/user-attachments/assets/1695e4bb-38d6-42de-a756-4650e38d0689)

Note that the right path is disconnected because this project isn't using any online API. A ViewModel can use multiple repositories and a Repository can aggregate data from, or send data into, multiple DataSources.

## Libraries used

### Jetpack Compose
It provides a simpler way to build the UI than the XML or Programmatic View, and may provide some interoperability with other devices using Compose Multiplatform.

### CameraX Compose and MLKit Analyzer
CameraX for Jetpack Compose has a quick integration with an Image Analyzer from Google's MLKit with CameraX's companion library, `camera-mlkit-vision`.
However, ideally I would use a library that isn't tied to Google's Play Services (MLKit, if not using a bundled model, downloads the model from there).
Also, an alternative is ZXImg, but it's noted that it's in Maintenance Mode and may provide bad results when the target is under improper lighting.

### Room Database with KSP
Android's native persistent database based on SQLite.

### Koin
The industry standard is Dagger/Hilt, but I feel that Koin is better for smaller projects like this because it's lightweight.

## Video evidence

Positive Camera permission flow:
https://github.com/user-attachments/assets/98d6043c-0ce9-4a9c-a7ef-8bc5a099eefd

Negative Camera permission flow:
https://github.com/user-attachments/assets/b9d6b6c5-e266-4419-9709-a0b9a006e991

Photo Picker permission flow (there isn't any because I'm using the Android-provided Photo Picker):
https://github.com/user-attachments/assets/60703461-3423-4fd7-af34-fcea3d28b80d

Scenario 1:
https://github.com/user-attachments/assets/65325524-9970-49e3-b2db-364d4d145eeb

Scenario 2:
https://github.com/user-attachments/assets/80fcc6e2-4b86-4777-9e8c-3599c0325f9d


