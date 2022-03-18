# MemView
A fast, modern, cross platform photo viewer and converter written in Java

## Why MemView?
Many photo viewing applications available across platforms suffer from feature bloat or lack of features. 

MemView was built with the following in mind:
1. A viewer that provides with basic zooming and navigation options+
2. A photo conversion option to get photos into convenient formats - Those webP images that are not valid...
3. A simple, uncluttered UI
4. Performant when flicking from one photo to another in quick succession*
5. Work on any platform
6. Support for a wide range of image types


*This requires loading images into RAM to prevent waiting for disk IO. 
This was a comprimise as RAM is usually abundant but disk IO, particularly with mechanical HDDs is limited

# Acknowledgements
TwelveMonkeys - A library that extends the functionality of the Java Image.IO API.
Many of the available features in MemView would not be possible without it


