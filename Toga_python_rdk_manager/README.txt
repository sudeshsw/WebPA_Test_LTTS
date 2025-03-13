###########################################################
# File: README.txt
# Purpose: RDKB-Manager main control.
# Owner: SYS
# Copy rights: (c) Comcast
###########################################################

>>> Installation procedure

>>> Install briefcase
python3 -m pip install briefcase


>>> Install toga
python3 -m pip install toga-demo
python3 -m pip install toga


>>> Develop APP and run below command to run the app in dev mode
briefcase dev


>>> Create build for Android 
briefcase create android

>>> Update if app is modified 
briefcase update android

>>> Build android app (debug apk file) 
briefcase build android

>>> Run android app on device connected via USB or on Virtual Android device 
briefcase run android


# The debug apk file is stored at below location
./build/rdk_manager/android/gradle/app/build/outputs/apk/debug/

# debug apk file
app-debug.apk

