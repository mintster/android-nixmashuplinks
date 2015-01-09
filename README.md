#NixMashup Links for Android#

The Android Version of NixMashup Links built in Android Studio.

Released Versions are found in their respective branch. v1.0.1, v1.1, etc. Master branch contains ongoing development.

##Application Features##

##Version 1.2##

- Redesigned for Material Design and Android 5.0
- ActionBar replaced by Toolbar
- Material Design Navigation Drawer with RecyclerView replacing ListView
- Android 5.x Styling

##Version 1.1##

- Navigation Drawer
- Menu added to individual Link Activity display
- Tablets now display individual Link Activity page

##Version 1.0.1##

- Optional Polling Service which notifies user of new NixMashup Links
- Dynamically generated Urls supporting all links, retrieval by tag, and search
- Records retrieved asynchronously on background thread
- Side-to-side paging of Individual Links
- Integrated Web View of source content

##About NixMashup Links##

NixMashup Links begin as a collection of links published periodically at [NixMash.com.](http://nixmash.com/nixmashup)

They are then processed and converted into individual objects at [NixMashupLinks.com](http://nixmashuplinks.com)

As individual data objects with tagging and searchable engine the links are now available to serve devices like Android. The Server API is not currently available but sample XML and Urls are included in the /docs folder.

###Sample XML###

Sample data format is found in _docs/sample-data.xml_

###Sample Urls###

Urls used include retrieving ALL links, links by TAG and SEARCH. Url structures are found in _docs/sample-urls.txt_

###Installation###

This repository does not contain production NixMashup Links API Urls and Keys, so uncomment the _api_xml_url, api_images_url, and api_key_ items in *values/strings.xml* for error-free compile.

Because no XML data is being retrieved the v1.0.1 app will intially launch to a blank screen.


