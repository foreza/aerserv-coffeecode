
# Introduction

Hi, I am a member of InMobi's PSO (product specialist organization) team.
We're responsible for a variety of things, such as walking new publishers through our latest features, troubleshooting integration issues, and providing overall platform and industry knowledge.
I also hail from the AerServ team (recently acquired by InMobi) and have some knowledge on how the mediation stack will work in the unified SDK.

# aervserv-coffeecode

This is an android project (written in Java) that demonstrates how to integrate Aerserv's Android SDK.
We have a sample application bundled into each of our SDK releases that walks you through the basics.
This app offers another viewpoint and a closer look at the latest and greatest features.


## Application Feature Set

*Note: These are sorted by the core features, then by latest SDK release. We will test and integrate the features offered by the latest SDK.*

### Core Features

**SDK Initialization**: In the MainActivity, we do an init of the SDK and demonstrate which mediated SDKs need this, and which ones do not. 

**Banner Preload + Show**: In the Main Activity, we demonstrate how we can preload, and then show a banner.
Recycler view: In the main activity, we demonstrate how you might put a banner inside of a recycler view. You can consult CustomViewAdapter class to see how this was done. 

**Interstitial Preload + Show**: In the CoffeeIncremented activity, we demonstrate how you might preload and then show an interstitial.

**MREC**: In the SipAndSwipe activity, we demonstrate to you how a MREC (medium rectangle) banner placement can be shown. This same activity also can be used to test any banner placement; it will cycle through a predefined set of PLCs that you define in the global class.

**Singleton "Game State"**: We created a GlobalClass singleton to be used in the app wherever you like. This is used loosely through the whole app to track states, store test cases, etc. This assumes an offline environment with no backend game server, as an example. 

**Virtual Currency**: This relies off of the GlobalClass to store and retrieve some 'currency'. The interstitial (assuming it has VC attached to it) will reward currency. (use placement: 380004)

**SDK Version / Important Info**: We show the SDK version (and GDPR consent!) near the top of the app

### Mediation Adapters Supported**:

* A9 Adapter (DTB)
* MoPub
* AdMob (Google Ads)
* InMobi
* Facebook (Audience Network)


### SDK Features with latest releases

**3.1.3 - A9 (AMAZON ads)**
* If you have a placement that supports A9, it can now be tested in this app. Added an additional method that is used specificially if A9 is enabled. You can enable this / disable this in GlobalClass. A9 requires for the SDK to ask the DTBAdLoader for an ad. Our SDK can take that ad response and send that as a package to our AdServer.


**3.1.2 - Moat, bug fixes, admob mediation**
* Added Admob (via gradle) to support test placements that use the AdMob adapter. 
* AdMob smart banners will now be supported. Made a new view 


x We updated the moat library. However, due to the fact SDK unification is around the corner, I decided to hold off on doing anything. BTW - Moat will cause some issues if you try to package jars. 

**3.1.1 - GDPR**
* Added a dialogue for tracking GDPR consent state as an EXAMPLE. For each of your mediation partners, you (the publisher) are responsible in securing permissions for each platform. This is covered lightly in GDPRConsent activity.





## AerServ Android SDK 

Link for download and documentation: https://support.aerserv.com/hc/en-us/articles/204159160


## Other Notes:

* Features are sometimes done out of order. You can see them in the issues section.
* I do accept issues; I do not accept PRs. This is a small project meant to help myself learn, and also help you benefit from what I've tried.
* I made this as a very junior / beginning Android dev. Please have mercy on my earlier design decisions.

 
