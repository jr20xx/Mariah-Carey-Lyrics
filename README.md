# Mariah-Carey-Lyrics
An Android app to reunite the lyrics of Mariah Carey's songs per album.

## First things first...
This project was made with the primary purpose of making an Android application to collect the lyrics of the songs contained in the studio albums released by one of the most successful artists of all times, inducted by the Songwriters Hall of Fame in the year 2022, Mariah Carey. There is no copyright infringement intended with the realization of this project and that is why the names of the recording studios in charge of the production of every album and the names of the authors of the songs (according to Wikipedia and/or Google) can be found in the information menu of the albums or the songs inside the resultant application.

The links provided by the app to the songs listed in there were found during a quick search on YouTube and not all of them were extracted from the official channel of Mariah Carey. Use them at your own risk.

The application and the project come with no warranty and they are distributed "as is". You are the only responsible of the use you make of this project and/or the resultant application and I won't take any responsibilities for any kind of damage caused by the usage of any. I have no relation with any of the video publishers, the authors of the songs or any of the recording labels that may appear inside the application or the project files. All the information and logos found inside this project are properties of their respective owners. The resultant application does not collect any kind of personal information of its users. Feel free to collaborate with this project. Help would be really appreciated ;)!

**This is currently a work in progress and the resultant application is not ready for a day by day use or to be published in any store.**

## How to build this project?
This project was built using Android Studio Flamingo under Debian 12.

To build this project locally and get an APK file by yourself, first make sure that you have at least the version 11 of the Java Development Kit installed on your computer by executing the command:
```
java -version
```

The resultant output should be something like:<br>
```
openjdk version "17.0.6" 2023-01-17
OpenJDK Runtime Environment (build 17.0.6+10-Debian-1)
OpenJDK 64-Bit Server VM (build 17.0.6+10-Debian-1, mixed mode, sharing)
```

Next, clone this repo by executing:
```
git clone https://github.com/jr20xx/Mariah-Carey-Lyrics
```

After that, you can just open the project on Android Studio and you'll be then ready to build the application by yourself with it.

## How to contribute to this project?
This project uses an embeded SQLite3 database to store the data showed by the application. That database is located in the `assets` folder inside of the application sources and contains two tables to hold the information: the `ALBUMS_TABLE` and the `SONGS_TABLE`. The `ALBUMS_TABLE` contains the next columns:<br>
**ID**: to identify every stored album;<br>
**ALBUM_TITLE**: to store the title of an album;<br>
**YEAR**: to store the year of the launch of the album;<br>
**RECORDING_LABEL**: to store the name of the recording studio behind the production and release of an album; and<br>
**WIKIPEDIA_LINK**: to store the link of the Wikipedia article about an album.

The `SONGS_TABLE`, in the other hand, contains the following columns:<br>
**ID**: to identify every stored song;<br>
**ALBUM_ID**: to establish the link between a song and an album;<br>
**TITLE**: to store the title of a song;<br>
**LYRICS**: to store the lyrics of a song;<br>
**WRITERS**: to store the names of the writers of a song; and<br>
**YT_LINK**: to store the link of a YouTube video where you can play a song.

In the future, the final structure of the database may contain an extra table with information about its current version that will help to improve the control when the application is executed for the first time after installing or after an update. The current structure of the tables inside of the database is the aforementioned. To add data to it and if you use Linux, you can use the `sqlitebrowser` package tool on Debian or its derivates. You can install that package by executing:<br>
```
sudo apt install sqlite3 sqlitebrowser
```

To contribute with the development of this project, just create a pull request and make sure that you have followed the aforementioned instructions to understand how the database must be filled. Please also make sure to provide good descriptions of the jobs you have done.

Thanks for your visit and, if you think that this project worth it, please spread its link and star it!

## Example screenshots

##### Main activity
![main-activity](/screenshots/main.png?raw=true)

##### About app menu
![about-app](/screenshots/main_info.png?raw=true)

##### Album activity example
![mc-activity](/screenshots/mc.png?raw=true)

##### Album information menu example
![album-info](/screenshots/mc_album_info.png?raw=true)

##### Lyrics activity example
![lyrics-activity](/screenshots/mc_lyrics.png?raw=true)

##### Song info example
![song-info](/screenshots/mc_song_info.png?raw=true)