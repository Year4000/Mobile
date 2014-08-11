//steps to installing, compiling, and running app
1. download and install the android sdk

2. open android virtual device manager(AVD manager)
    and create new device per your desired testing specs
    Note: if you cannot access AVD manager from sdk, you should be able to open it from IDE

3. import android-app into IDE following import steps from your IDE

4. open file pom.xml and change dependency marked "sdk path" to the
    location of android sdk on your machine (be sure to use entire path)

5. change name of device (avd) to the name you gave your newly created avd

6. if your avd is not already running, open it and give it time to completely start up
    Note: do not continue until you are looking at the android home screen

7. Maven Compile and Build with Android
    in order to compile with android you need a few extra terminal commands
    it is not enough to simply run mvn install you must also tell android to deploy the app

    To compile and build use:
        mvn clean install android:deploy

    To compile, build and run use:
        mvn clean install android:deploy android:run