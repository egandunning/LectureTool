LectureTool version 1
Egan Dunning 2017
---------------------------
Contents
1. About
2. Running the program
3. Using the program
---------------------------
1. About 
The LectureTool program plays videos and allows note taking in a simple text editor. The 
user can create Courses that are linked to a Youtube playlist url. The user can create 
notes that are associated with a Course.

The purpose of this program is to facilitate learning. Youtube hosts many educational 
videos, LectureTool provides users with a convenient way to store playlists and notes 
for educational videos.

2. Running the program 
Double-click LectureTool.jar to start LectureTool. LectureTool will generate files called 
"options", "log.txt", and a folder called "notes" inside LectureTool.jar's containing 
folder.

3. Using the program 
Upon starting the program, you can pick a course to open from a list of all the courses 
you've made. This will load the video playlist, and any notes for that course. To take 
notes, press the "Open Note" button to select a note from a list of all the notes you've 
created for that course. To save a note, press the "Save Note" button or press Ctrl-S 
(Command-S) on Mac. A message at the bottom of the window will be displayed if the note 
was saved.

Press the "New Course" button to create a new course. This will display a dialog where 
prompting the course name, and the Youtube playlist URL. Find the course you want to 
watch on Youtube, then just copy and paste the URL into the text field in the dialog box. 

Press the "New Note" button to create a new note. After naming the note, a file is created 
at notes/(course name)/(note name).

The "Options" button brings up a dialog with a few settings: 
Change course URL - Pick a course and change the video URL 
Set note storage location - Change the location to store courses and notes. (Browse button 
not implemented)
Set log storage location - Change the location to store the log file. (Browse button not 
implemented)
Reset Defaults - reset default settings.

Delete the currently selected course by pressing the "Delete Course" button. Delete the 
currently selected note by pressing the "Delete Note" button. A confirmation dialog 
prevents accidental deletions.