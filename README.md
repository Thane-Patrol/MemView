# MemView
A fast, modern, cross platform photo viewer and converter written in Java

## Why MemView?
Many photo viewing applications available across platforms suffer from feature bloat or lack of features. 

MemView was built with the following in mind:
1. A viewer that provides with basic zooming and navigation options+
2. A photo conversion option to get photos into convenient formats - Those webP images that are not valid...
3. A simple, uncluttered UI
4. Performant when flicking from one photo to another in quick succession*
5. Works on most common platforms
6. Support for a wide range of image types


*This requires loading images into RAM to prevent waiting for disk IO. 
This was a comprimise as RAM is usually abundant but disk IO, particularly with mechanical HDDs is limited

## Where to download
Links to download for the following distributions:
- <a href="">Windows x86 </a>
- <a href="">Mac OS x86 </a>
- <a href="">Mac OS arm64</a>
- <a href="">Debian derived x86 .deb</a>
- <a href="">Fedora derived x86 .rpm</a>
- <a href="">Flatpak</a>


Please note all binaries are 64bit

## Writable formats
Input file formats
| --- | BMP | JPG | PNG | TGA | TIFF | WEBP |
| --- | --- | --- | --- | --- | ---- | ---- |
| BMP |  X  |  X  |     |  X  |  X   |  X   |
| JPG |  X  |  X  |     |  X  |      |  X   |
| PNG |  X  |  X  |  X  |  X  |  X   |  X   |
| TGA |  X  |  X  |  X  |  X  |  X   |  X   |
| TIFF|  X  |  X  |  X  |  X  |  X   |  X   |

## Limitations
### Conversion:
- Refer to the table above to see which output file format is avaliable for a given input file format
- If your favourite format isn't visible, open an issue, detailing any specifics and why it is valuable to have
- Please note that while HDR support is technically possible, it does not produce a desirable outcome and will not be considered at this time
- 

# Acknowledgements
TwelveMonkeys - A library that extends the functionality of the Java Image.IO API.
Many of the available features in MemView would not be possible without it


