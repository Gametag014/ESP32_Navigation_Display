# Navigation-Display-on-ESP32

This project is an ESP32-based navigation display and security system designed for motorcycles. It receives navigation data from an Android app via Bluetooth and displays it on an OLED screen. Additionally, it features a kill switch security system that prevents unauthorized engine startup.

⚠️ Project Status: Needs Help!

🚧 This project is currently not fully functional, and we need your help to improve and debug it. If you're experienced with ESP32, Bluetooth communication, or Android development, your contributions would be greatly appreciated!

Features :
    Bluetooth communication between ESP32 and an Android app
    Navigation display showing turn-by-turn directions and speed
    Kill switch security system activated based on Bluetooth pairing
    OLED display for better visibility
    Android app integration for navigation tracking

Hardware Requirements :
    ESP32 Dev Module
    SSD1306 OLED Display (128x64)
    Relay module (for the kill switch)
    Push buttons (optional for control)
    Power source (battery or USB)

Software Requirements :
    Arduino IDE (ESP32 board support installed)
    Android Studio (for the mobile app)

Installation & Setup :
    ESP32 Firmware
    Install Arduino IDE and add ESP32 board support.
    Install the required libraries:
    - BluetoothSerial.h
    - Wire.h
    - EEPROM.h
    - Adafruit_GFX.h
    - Adafruit_SSD1306.h
    Upload the ESP32 firmware using Arduino IDE.

Android App :
    Open the Android Studio project.
    Grant Bluetooth & Location permissions in the app.
    Build and install the app on your phone.

How It Works :
    Pair your phone with the ESP32 via Bluetooth.
    The ESP32 receives navigation data from the app and displays it.
    If an unauthorized device is detected, the kill switch remains active to prevent engine start.
    The system automatically unlocks the kill switch when the paired device is in range.

Contributing 🤝
We welcome contributions from the community! 🚀
    Debug existing issues and fix errors
    Improve Bluetooth stability
    Enhance navigation UI
    Add voice commands or vibration alerts
    Optimize power consumption

How to Contribute:
Fork the repository on GitHub.
        Create a new branch for your feature.
    Submit a pull request with your changes.

Issues & Support
If you encounter any issues, feel free to open an issue on GitHub or start a discussion. We appreciate all feedback and suggestions!

GitHub Repository: Navigation Display on ESP32

Let's build something great together! ✨