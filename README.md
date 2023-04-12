# Tugas Kecil 3 - Strategi Algoritma 
Tugas Kecil 3 IF2211 – Strategi Algoritma Tahun 2022/2023

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Algrithm Explanation](#algorithm-explanation)
* [Features](#features)
* [Screenshots](#screenshots)
* [How to Run](#how-to-run)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Authors](#authors)


## General Information
Route planning is the process of determining the best route or route from one location to another. In route planning, there are two main types of algorithms used, namely uninformed algorithms and informed algorithms. There is UCS (Uniform Cost Search) as an uninformed search algorithm and A* as an informed search algorithm.

The UCS (Uniform cost search) and A* (or A star) algorithms can be used to determine the shortest path from one point to another. In this small task 3, the shortest path was determined based on the Google Map of the streets in the city of Bandung. From the road segments on the map a graph is formed. The node represents a crossroads (intersection 3, 4 or 5) or the end of the road.


## Technologies Used
- kotlin
- java
- gradle
- Android Studio
- Google Map API
- xml

## Algorithm Explanation
UCF is a pathfinding algorithm that calculates the cost to reach each vertex in a graph. This algorithm chooses the node with the smallest cost as the node to be explored next. UCF continues to search until it reaches the destination node or there are no more reachable nodes. UCF is appropriate when no heuristic information is available. The function calculation for UCF is as follows: f(n) = g(n) where g(n) is the cost of reaching node n.

A* is an algorithm that combines techniques from BFS and Greedy Best First Search. This algorithm looks for the shortest path from the initial node to the destination node by considering the cost to reach a certain node and the estimated cost required to reach the destination node. This algorithm is very effective in finding the shortest path and is often used in navigation applications. A* is also a pathfinding algorithm that combines ideas from UCF and heuristics. This algorithm looks for the shortest path from the initial node to the destination node by considering the cost to reach a certain node and the estimated cost required to reach the destination node. A* uses a heuristic function to estimate the remaining distance until it reaches the destination node. This algorithm is very effective in finding the shortest path and is often used in navigation applications. The function calculation for A* is as follows: f(n) = g(n) + h(n) where g(n) is the cost to reach node n and h(n) is the estimated cost to reach the destination node from node n.

## Features
- Route planning
- UCS (Uniform Cost Search)
- A* (A star)
- Google Map Visualization
- Distance Calculation


## Screenshots
<!-- atur size dan center -->
<img src="./doc/app.jpg" alt="Example screenshot" width="40%"/>


## How to Run
Android APK
1. Download the app from [here](https://github.com/NerbFox/Tucil3_Stima/blob/main/src/Release)
2. Install the application on your android device
3. Enable necessary permissions (Install from unknown sources, file permission, etc)
4. Open the application, you can choose Maps if you want to see the visualization of the route on maps
5. input matrix file and coordinate file, you can use the example files in [bandung.txt](./tests/bandung.txt) for matrix file and [bandungcoor.txt](./tests/bandungcoor.txt) for coordinate file
6. Choose start and goal node
7. Choose the algorithm (UCS or A*)
8. Click RUN
9. Enjoy


## Project Status
Project is: _complete_


## Room for Improvement

Room for improvement:
- Speed up algorithm
- Algorithm optimization
- UI improvement
- More features


## Authors
1. Nigel Sahl (13521043)         
2. Muhamad Aji Wibisono (13521095)   
