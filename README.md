# Sleep Guardian

## Authors
- Gia Huy Phạm  
- *(Add partner name here if applicable)*

---

# Project Overview

**Sleep Guardian** is a context-aware mobile application that helps users reduce nighttime distractions by detecting when they are likely going to sleep.

Instead of requiring the user to manually enable a sleep mode, the application uses **environmental context from sensors** to determine when the user is preparing for sleep. When certain conditions are detected, the application automatically activates **Sleep Mode** and records the event.

The goal of this project is to demonstrate how **mobile devices can intelligently react to their environment** using sensor fusion while respecting **user privacy and hardware limitations**.

---

# How the App Assists the User

Sleep Guardian attempts to detect when the user is preparing to sleep by observing the phone's environment.

The application evaluates three contextual signals:

1. **Light Sensor (Hardware Sensor)**
2. **Proximity Sensor (Hardware Sensor)**
3. **Battery Charging State (Virtual Sensor)**

These signals are combined to determine whether the user may be going to sleep.

## Context Logic

Sleep Mode activates when the following conditions are met:

- The surrounding environment is **dark** (light sensor value below a threshold)
- The phone is **face down or close to a surface** (proximity sensor reports near)
- The phone is **charging**

When all conditions are satisfied, the system assumes the user has placed their phone down for the night and activates **Sleep Mode**.

When Sleep Mode activates, the app:

- Records a **Sleep Event** in the local database
- Updates the UI to reflect the current sleep state
- Logs the contextual sensor values used to make the decision

This allows the application to provide **automatic assistance without requiring manual interaction**.

---

# Sensors Used

Sleep Guardian uses **two hardware sensors** and **one virtual sensor**.

## Hardware Sensors

### Light Sensor
- Measures ambient light level (lux)
- Used to determine whether the environment is dark

### Proximity Sensor
- Detects whether an object is close to the phone
- Used to approximate whether the phone is placed face-down or near a surface

---

## Virtual Sensor

### Battery Charging State
Obtained from the device battery manager.

Used to determine whether the phone is currently charging.

Charging is used as a signal that the user may be preparing for extended phone inactivity (such as sleep).

---

# Background Processing

Sleep Guardian uses **WorkManager** to periodically evaluate the sleep detection logic.

The background worker:

1. Retrieves the most recent sensor values
2. Applies the context rule
3. Logs an event if Sleep Mode becomes active

This allows the application to **continue monitoring context even when the application is not open**.

---

# Data Persistence

The application stores data locally using two Android persistence systems.

## Room Database

Room stores **Sleep Events**, including:

- Timestamp
- Light sensor value
- Proximity sensor state
- Charging state
- Whether Sleep Mode was activated

This allows the application to maintain a **history of detected sleep events**.

---

## DataStore

DataStore is used to store **persistent application settings**, including:

- Light/Dark theme preference
- Whether Sleep Guardian is enabled
- Sensor threshold values

---

# Application Architecture

The application follows the **Model–View–ViewModel (MVVM)** architecture pattern.

This pattern separates:

- **Data management (Model)**
- **Application logic (ViewModel)**
- **User interface rendering (View)**

---

# Application Screens

The application includes a **Bottom Navigation Bar** with four destinations.

## Dashboard

The Dashboard is the primary interface for the user.

It displays:

- Live sensor readings
- Current Sleep Mode status
- Recent sleep detection events

---

## Context Logic

This screen explains the **technical operation of the application**, including:

- Which sensors are used
- How sensor values are interpreted
- The rule used to activate Sleep Mode

---

## Privacy by Design

This screen explains:

- What permissions are used
- What data is collected
- How the data is stored
- How user privacy is protected

All collected data is **stored locally and never transmitted externally**.

---

## Settings

The Settings screen allows users to configure application behavior.

Users can:

- Enable or disable Sleep Guardian
- Switch between **Light and Dark mode**
- Adjust detection preferences

Settings are stored using **DataStore**.

---

# Privacy Considerations

Sleep Guardian is designed with **privacy-first principles**.

Key protections include:

- No data is transmitted to external servers
- All sensor data is stored locally
- No location information is collected
- Users can delete stored data at any time

Sensor data is only used to determine **device context**, not personal identity.

---

# Epics + User Stories (Implementation Plan)

---

## Epic A — Project Setup & Baseline Navigation  
*(Rubric: Navigation, MVVM foundation)*

### A1
**User Story:**  
As a user, I can navigate between Dashboard, Context Logic, Privacy, and Settings using the bottom navigation bar.

**Acceptance Criteria**
- The application contains 4 navigation destinations
- Navigation state is preserved on screen rotation

### A2
**User Story:**  
As a developer, I maintain a clean MVVM package structure.

**Acceptance Criteria**
- Project uses the following structure:

```
data/
domain/
ui/
viewmodel/
```

- Dependency injection module included (optional if using Hilt)

---

## Epic B — Theme, Adaptive Layout, and Light/Dark Toggle  
*(Rubric: 15 pts)*

### B1
**User Story:**  
As a user, I can switch between Light and Dark mode in Settings.

**Acceptance Criteria**
- Theme updates immediately
- Preference persists using DataStore

### B2
**User Story:**  
As a user, the Dashboard adapts to different device orientations and screen sizes.

**Acceptance Criteria**
- Responsive layout used
- Example: single column on compact screens and grid layout on larger screens

### B3
**User Story:**  
As a user, I see a custom theme applied to the app.

**Acceptance Criteria**
- Non-default color palette
- Custom typography applied throughout the app

---

## Epic C — Sensor Capture  
*(Rubric: Context Sensors)*

### C1
**User Story:**  
As a user, I can see the live **light sensor** lux value on the Dashboard.

**Acceptance Criteria**
- Updates while the screen is open
- Displays **Unavailable** if the device does not support the sensor

### C2
**User Story:**  
As a user, I can see the **proximity sensor** state.

**Acceptance Criteria**
- Displays **Near** or **Far**
- Updates in real time

### C3
**User Story:**  
As a user, I can see the **battery charging state and battery percentage**.

**Acceptance Criteria**
- Updates when battery state changes
- Works correctly on the Android emulator

### C4
**User Story:**  
As a developer, I cache the last known sensor states for background processing.

**Acceptance Criteria**
DataStore stores:

```
lastLux
lastProximityNear
lastCharging
lastUpdated
```

---

## Epic D — Context Logic & State Evaluation  
*(Rubric: ViewModel logic + Context Logic screen)*

### D1
**User Story:**  
As a user, the app determines Sleep Mode automatically using contextual sensor data.

**Acceptance Criteria**
- Sleep state changes when conditions are satisfied
- Status visible on the Dashboard

### D2
**User Story:**  
As a user, I can see how the detection rule is evaluated.

**Acceptance Criteria**
The Context Logic screen displays:

- `isDark`
- `isNear`
- `isCharging`
- Overall rule result

### D3
**User Story:**  
As a developer, application logic is separated from the UI.

**Acceptance Criteria**
- Logic resides in ViewModel or UseCase
- UI only renders state
- Rule evaluator can be unit tested (optional)

---

## Epic E — Persistence with Room Database  
*(Rubric: Persistence)*

### E1
**User Story:**  
As a user, when Sleep Mode activates or deactivates, an event is stored in the database.

**Acceptance Criteria**
- New entries appear in the history list

### E2
**User Story:**  
As a user, I can view recent sleep detection events.

**Acceptance Criteria**
History list displays:

- Timestamp
- Reason for trigger
- Sensor snapshot

### E3
**User Story:**  
As a user, I can clear stored history.

**Acceptance Criteria**
- Button in Settings clears the Room table
- UI updates immediately

---

## Epic F — Background Task Processing  
*(Rubric: WorkManager / ForegroundService)*

### F1
**User Story:**  
As a user, Sleep Guardian continues checking conditions even when the app is closed.

**Acceptance Criteria**
- Periodic WorkManager runs
- Rule evaluation logs events to Room

### F2
**User Story:**  
As a user, I receive a notification when Sleep Mode activates.

**Acceptance Criteria**
- Notification triggered on activation
- Uses `POST_NOTIFICATIONS` permission (Android 13+)

### F3
**User Story:**  
As a developer, background checks use cached sensor data.

**Acceptance Criteria**
- Worker reads DataStore cached values
- Worker evaluates detection rule without direct sensor listeners

---

## Epic G — Privacy by Design & Documentation  
*(Rubric: README + Privacy screen)*

### G1
**User Story:**  
As a user, I can understand what data the app collects.

**Acceptance Criteria**
Privacy screen explains:

- Sensor usage
- Data storage (Room + DataStore)
- No network data transmission

### G2
**User Story:**  
As a user, I can see all permissions requested and why.

**Acceptance Criteria**
- Lists `POST_NOTIFICATIONS` permission
- Explains that sensors do not require runtime permission

### G3
**User Story:**  
As a developer, I provide clear project documentation.

**Acceptance Criteria**
README includes:

- Author names
- Sensor fusion explanation
- Emulator testing instructions
- References

---

# Suggested Package Structure

```
data/
 ├── db/          (Room entities, DAO, database)
 ├── prefs/       (DataStore settings)
 ├── sensors/     (SensorManager + battery wrapper)
 └── repo/        (Repositories)

domain/
 ├── SleepDetectionUseCase
 └── models/

ui/
 ├── navigation/
 ├── screens/
 │   ├── dashboard
 │   ├── context
 │   ├── privacy
 │   └── settings
 └── theme/

viewmodel/
 ├── DashboardViewModel
 ├── SettingsViewModel
 └── ContextLogicViewModel

worker/
 └── SleepGuardianWorker
```

---

# Minimum Viable Implementation Order

1. Bottom navigation with 4 empty screens *(Navigation points)*  
2. Theme system with DataStore dark mode toggle *(Theme points)*  
3. Live light sensor, proximity sensor, and battery status *(Sensor points)*  
4. Sleep rule evaluation + Context Logic screen *(MVVM logic)*  
5. Room database logging + history list *(Persistence)*  
6. WorkManager background evaluation + notification *(Background task)*  
7. Privacy screen + README documentation *(Documentation points)*

---

# References / Resources

Android Developers Documentation  
https://developer.android.com

Room Persistence Library  
https://developer.android.com/training/data-storage/room

Jetpack DataStore  
https://developer.android.com/topic/libraries/architecture/datastore

WorkManager  
https://developer.android.com/topic/libraries/architecture/workmanager

Android Sensors Overview  
https://developer.android.com/guide/topics/sensors/sensors_overview

