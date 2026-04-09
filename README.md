[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23479672)

## Author:
### Zachary Amith
### 04/08/2026 --> mm/dd/yyyy
### Merced College

Learning Objectives
By completing this assignment, you will:

work with a custom linked list implementation
use ArrayList to manage data (inventory)
read and understand a multi-class Java program
load and use data from a CSV file
apply object-oriented programming principles
build a basic GUI using Swing
connect backend logic to a visual interface
Part 1: Run the Starter Code (Console Version)
Clone the GitHub Classroom repository
Open the project in Eclipse
Run the program using the Main class
Requirements:
The program should run without crashing
You should be able to:
move between rooms
collect items
view inventory
reach the final room
What to do:
Read through the code and understand how the following classes work:
Scene
Choice
SceneLinkedList
Player
AdventureGame
You are not required to rewrite this code, but you should understand how it works before moving on.

Part 2: Convert the Game to a GUI
You will now convert the console-based game into a graphical user interface using Java Swing.

Tools:
Eclipse WindowBuilder (recommended)
Swing components (JFrame, JButton, JLabel, JTextArea, etc.)
Your GUI must include:
A main window (JFrame)
A display area for:
room title
room description
Buttons for player choices (at least 2)
A way to display the player’s inventory
A way to interact with the game without using the console
Required behavior:
When the player enters a room:
update the title and description on screen
When a button is clicked:
move to the next scene using the linked list
If a room contains an item:
allow the player to pick it up
The final room must still check if the player has the correct items
Important:
You should reuse the existing classes (do NOT rewrite the data structures)
You are replacing the input/output layer, not the core logic
Part 3: Add One New Room
Add at least one new room to the game.

This includes:
Adding a new row to the CSV file
Connecting it properly to existing rooms
(Optional) Adding an item in that room
Your new room should:

have a meaningful description
connect logically to the game
not break the game flow
Program Requirements
Your final program must:

use the provided linked list structure
use an ArrayList for inventory
load data from the CSV file
run fully in a GUI (no console interaction required)
allow the player to:
move through rooms
collect items
attempt to win the game
Submission Instructions
Submit:

push your code to your GitHub repo 
ensure your code compiles and runs, and is nice and neat.