<h1> Evolution2 </h1>

Version 1.11

this is my second neural network project. In this project, I want to make a structure that is more optimized and open to new features than the last. <br>

I made two models of neral network (see package brain), one learning algorithm and two selection algorithm. I want to do more.

I just do that for fun, so feel free to use this however you like.

<h2> version history </h2>

<h5> 1.11 </h5>

Refactor of the project :   
- extraction of a MutationManagement class from the ReproductionAlgorithm class
- changement in packages names from NEAT to neat
- changement of the management of the mutations

/!\ The changes made will require an update if you had already used a previous version of this project

<h4> 1.10.1 </h4>

Renaming of the Controller class in View and deletion of unnecessary methods

<h4> 1.10 </h4>

transformation of the project into a gradle project

<h4> 1.9 </h4>

- addition of a possibility of exportation using json format
- creation of a package tools
- refactor of the thread management in LearningAlgorithm
/!\ The changes made in LearningAlgorithm and Controller will break a program made before this version.

<h4> 1.8 </h4>

We can now control the learing algorithm directly from the code.

<h4> 1.7 </h4>

We can now controll the number of cores used in the computing process of the brain.  
The file "Simulation1" has been transformed into a proper tutorial file.

<h4> 1.6 </h4>

Files allowing to track down who is parent of who are now created.

<h4> 1.5.2 </h4>

Correction of a bug : when the window was closed, the simulation continued running. This is not the case anymore.

<h4> 1.5.1 </h4>

Implementation of multithreading in the computing process of the FlexibleBrain

<h4> 1.5 </h4>

Implementation of multithreading in the computing process of the LayeredBrain

<h4> 1.4 </h4>

Addition of a new selection algorithm : the roulette weel selection algorithm. This algorithm uses a bit of random, but still favors the bests.

<h4> 1.3 </h4>

Addition of a system to regularely save automatically.

<h4> 1.2.1 </h4>

Usage of the brain recombinaison in the Elitism algorithm. <br>
/!\ the saves from before this update will be unusable

<h4> 1.2 </h4>

Creation of a static function in Brain (combine(Brain parent1, Brain parent2)) allowing to fuse two brains of the same type.

<h4> 1.1 </h4>
Developpement of two systems that allow to run the simulation for a set amount of time or a set amount of iterations.

<h4> 1.0 </h4>
First version of the program : <br>
Developpement of two kind of brain : FlexibleBrain and LayeredBrain. <br>
Developpement of a way to track mutations <br>
Developpement of the NEAT algorithm with one selection algorithm <br>
Developpement of a graphic window to control the simulation <br>
