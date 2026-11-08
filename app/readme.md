# Terrible Movie Search App Assignment

where should i put

## Overview

Build a two-screen Android app that searches the OMDb API for a movie
title and shows the poster of the first result. The app must work well
on phones and tablets, in both portrait and landscape, and must not lose
what the user typed when the device rotates.

By the end of this assignment you should be able to:

-   Use Volley to make an HTTP request to a public JSON API from an
    Android app and parse the response.

-   Pass data from one Activity to another using Intent extras.

-   Load an image from a URL into an ImageView with the Picasso library.

-   Build layouts that adapt to different screen sizes and orientations.

-   Preserve UI state across configuration changes such as rotation.

-   Report errors to the user with a Toast.

Write the app in Kotlin. Use Android Studio and target a SDK of API 35.
Use Volley for all network requests and Picasso for image loading.

You may use an AI image generator to create any images or logos the app
needs, such as the launch screen logo, the app icon, and the poster
placeholder.

## Requirements

The app has a launch screen followed by two main screens. Each main
screen is its own Activity.

### Splash screen

1.  When the app starts, show a splash screen with the app\'s name and a
    logo before Screen 1 appears.

### Screen 1: Search

1.  A title at the top of the screen (a TextView), for example \"Movie
    Search\".

2.  A text box (an EditText) where the user types a movie title.

3.  A button labelled \"Search\".

4.  When the button is tapped, the app sends a Volley request to
    omdbapi.com for the text in the box.

5.  If the API returns no results, show a Toast such as \"No movies
    found\".

6.  If the network request fails for any reason (no connection, timeout,
    bad response), show a Toast such as \"Network error, please try
    again\".

7.  If a result is found, start Screen 2 and pass the first result\'s
    title and poster URL to it using Intent extras.

8.  The text in the EditText must still be there after the device
    rotates.

### Screen 2: Result

1.  A TextView showing the movie\'s title.

2.  An ImageView showing the movie\'s poster, loaded with Picasso.

3.  A \"Back\" button that finishes the Activity and returns to Screen
    1.

4.  If the poster URL is missing or the string \"N/A\", show a
    placeholder image instead of a blank space.

### Both screens

-   The layout must look correct in portrait and landscape on a phone,
    and on a tablet-sized screen. Nothing may be cut off, overlap, or
    stretch out of proportion.

-   The app must not crash on rotation, on an empty search, or on a
    search with no results.

## Using the OMDb API

OMDb is a free JSON API for movie data. Request a free API key at
[omdbapi.com/apikey.aspx](https://www.omdbapi.com/apikey.aspx). The key
arrives by email and must be activated from the link in that email
before it works.

Search by title with the s parameter. Replace YOUR_KEY with your own key
and URL-encode the search text:

https://www.omdbapi.com/?apikey=YOUR_KEY&s=inception

A successful response looks like this. The first result is Search\[0\]:

{\
\"Search\": \[\
{\
\"Title\": \"Inception\",\
\"Year\": \"2010\",\
\"imdbID\": \"tt1375666\",\
\"Type\": \"movie\",\
\"Poster\":
\"https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@.\_V1_SX300.jpg\"\
}\
\],\
\"totalResults\": \"1\",\
\"Response\": \"True\"\
}

When nothing matches, the API still returns HTTP 200, so you must check
the Response field, not just the status code:

{\
\"Response\": \"False\",\
\"Error\": \"Movie not found!\"\
}

Things to watch for:

-   Add \<uses-permission android:name=\"android.permission.INTERNET\"
    /\> to your manifest or every request will fail.

-   Use https, not http. Android blocks cleartext traffic by default.

-   Some results have \"Poster\": \"N/A\" instead of a URL. Handle that
    case on Screen 2.

-   Do not commit your API key to a public repository. Put it in
    local.properties

-   Volley runs the request on a background thread and delivers the
    response and error listeners on the main thread, so you can show a
    Toast or start an Activity directly inside them.

### Making the request with Volley

Add Volley to your module-level build.gradle.kts:

dependencies {\
implementation(\"com.android.volley:volley:1.2.1\")\
}

## Passing data to the second screen with extras

You must use Intent extras to send the first result\'s data from Screen
1 to Screen 2. Do not use a static field, a singleton, or a global
variable. Extras survive process death and are the standard way to hand
data between Activities.

## Loading the poster with Picasso

[Picasso](https://square.github.io/picasso/) is a small image-loading
library from Square. It downloads the image on a background thread,
caches it, and sets it on the ImageView when it arrives. You must use it
for the poster on Screen 2.

Add the dependency to your module-level build.gradle (or
build.gradle.kts) and sync:

dependencies {\
implementation(\"com.squareup.picasso:picasso:2.8\")\
}

The simplest call loads a URL straight into an ImageView:

Picasso.get()\
.load(posterUrl)\
.into(posterImageView)

For this assignment use the fuller form. It shows a placeholder while
downloading, an error image if the download fails, and sizes the bitmap
to the view so large posters do not waste memory:

val posterUrl = intent.getStringExtra(Extras.POSTER_URL)\
\
if (posterUrl.isNullOrBlank() \|\| posterUrl == \"N/A\") {\
posterImageView.setImageResource(R.drawable.ic_no_poster)\
} else {\
Picasso.get()\
.load(posterUrl)\
.placeholder(R.drawable.ic_loading)\
.error(R.drawable.ic_no_poster)\
.fit()\
.centerInside()\
.into(posterImageView)\
}

Notes:

-   Picasso throws an IllegalArgumentException if you call load() with
    an empty string, so check for empty and \"N/A\" first, as above.

-   fit() waits until the ImageView has been measured, so the ImageView
    must have a real size in the layout. Avoid wrap_content on both
    dimensions.

-   centerInside() keeps the poster\'s aspect ratio. Posters are taller
    than they are wide, so do not stretch them.

-   If you want to know when loading finished, pass a Callback to
    into():

Picasso.get().load(posterUrl).into(posterImageView, object : Callback {\
override fun onSuccess() { /\* hide a progress bar, for example \*/ }\
override fun onError(e: Exception) {\
Toast.makeText(this@ResultActivity, \"Could not load poster\",
Toast.LENGTH_SHORT).show()\
}\
})

The two drawables (ic_loading and ic_no_poster) can be any vector or PNG
you add under res/drawable. A simple grey rectangle is fine, or you may
generate them with an AI image tool.

## Layout, screen sizes, and rotation

A large part of the grade is for layout. Your screens will be checked on
a small phone, a large phone, and a 10-inch tablet, each in portrait and
landscape. Test all six before you submit. Android Studio\'s Layout
Validation tool (View → Tool Windows → Layout Validation) shows all of
them at once.

### Layout rules

-   Use ConstraintLayout as the root of each screen. Avoid nesting
    several LinearLayouts.

-   Never use fixed pixel sizes. Use dp for sizes and margins and sp for
    text.

-   Give the EditText and Button a width of 0dp with start and end
    constraints so they stretch with the screen, rather than a
    hard-coded width.

-   Wrap Screen 2 in a ScrollView (or use NestedScrollView) so a tall
    poster plus the title and button still fit in landscape on a small
    phone.

-   Constrain the poster ImageView with a ratio, for example
    app:layout_constraintDimensionRatio=\"2:3\", so it keeps a poster
    shape at any width.

-   Keep text in res/values/strings.xml, not hard-coded in XML or
    Kotlin.

-   If a screen genuinely needs a different arrangement in landscape,
    add a second layout file under res/layout-land/ with the same file
    name and the same view ids. You may also add res/layout-sw600dp/ for
    tablets.

-   Do not lock the orientation with android:screenOrientation, and do
    not add android:configChanges to avoid handling rotation. Both will
    be marked as not meeting the requirement.

### Keeping the text box across rotation

When the device rotates, Android destroys and recreates the Activity.
The text the user typed disappears unless it is saved.

Android saves and restores the contents of any EditText automatically,
but only if the view has an android:id and the Activity calls
super.onSaveInstanceState and super.onCreate(savedInstanceState) as
normal. Make sure your EditText has an id and check that rotation keeps
the text. If it does, you have met the requirement.

If you keep the query anywhere else, or you want to be explicit, save it
yourself:

companion object {\
private const val KEY_QUERY = \"query\"\
}\
\
override fun onSaveInstanceState(outState: Bundle) {\
super.onSaveInstanceState(outState)\
outState.putString(KEY_QUERY, searchEditText.text.toString())\
}\
\
override fun onCreate(savedInstanceState: Bundle?) {\
super.onCreate(savedInstanceState)\
setContentView(R.layout.activity_search)\
savedInstanceState?.getString(KEY_QUERY)?.let {
searchEditText.setText(it) }\
}

A search that is still in flight during rotation will deliver its
response to the old, destroyed Activity. To avoid that, tag the request
and cancel it in onStop:

request.tag = \"search\"\
queue.add(request)\
\
override fun onStop() {\
super.onStop()\
queue.cancelAll(\"search\")\
}

Screen 2 does not need any special handling. Its data arrives in the
Intent extras, which are delivered again to the recreated Activity, and
Picasso serves the poster from its cache.

## Submission and grading

Submit a link to a Git repository containing the whole Android Studio
project. Your API key must not be in the repository. Put it in
local.properties (which is git-ignored by default).

The README must include:

-   [ ] How to add an OMDb API key and build the app.

-   [ ] Six screenshots: Screen 1 and Screen 2, each on a phone in
    portrait, a phone in landscape, and a tablet.

-   [ ] A list of any images or logos that were AI-generated.

-   [ ] Anything that does not work, stated honestly.

Marks are out of 100:

  -----------------------------------------------------------------------
Criterion                                                    Marks
  ------------------------------------------------------------ ----------
Launch screen shows the app name and logo and opens Screen 1 5

Screen 1 has a title, text box, and button, and searches     15
OMDb with Volley

No-result and network-error cases each show a Toast and do   10
not crash

First result\'s title and poster URL are passed to Screen 2  10
with Intent extras

Screen 2 shows the title, the poster loaded with Picasso,    15
and a working Back button

Missing or \"N/A\" poster shows a placeholder                5

Layouts are correct on phone and tablet, portrait and        20
landscape, with no overlap, cut-off, or stretched views

Text box content survives rotation                           10

Code quality: Kotlin idioms, no hard-coded strings, API key  10
not committed, README complete
  -----------------------------------------------------------------------
