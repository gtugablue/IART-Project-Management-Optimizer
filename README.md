# IART-Project-Management-Optimizer

Project management optimizer that allocates members to a project in an efficient way taking in account the duration of each task and the skills of each member for each task.

# Project Description

The problem data is imported from a JSON file which contains a list of tasks, team members and their skills. Each member has a list of skills rated according to his capacity. For example, one team member may have a skill of 0.6 in computer engineering and a skill of 1 in economics.

The objective of the optimization is to minimize the time it takes to finish the whole project, or, in other words, the time it takes for the last task to be completed.

For that, two different meta-heuristics have been applied: Genetic Algorithms and Simulated Annealing. Experiments have been made with each heuristic and different configurations. The results are available in the project's final report.

A graphical user interface has been developed to allow the user to choose the algorithm and configurations, as well as being able to see it run in real-time with statistics and the possibility of analysing the best solution.

## Getting Started

To get started on the project, it is required to install some software and to follow the installation procedures described below.

### Prerequisites

In order to run the project it is required to have a Java SDK and an IDE. The chosen software for the development phase were the ones present below.

```
Java SDK version 8.0
Eclipse
```

### Installing

The steps to follow in order to get the project running are described below.

```
1. Open the IDE and import the project.
2. Add the libraries to the build path (present on the directory called libs).
3. Compile and run the Launcher.java file.
```

## Built With

* [GraphStream](http://graphstream-project.org/) - Library used for the graph display
* [Eclipse](https://eclipse.org/) - Development IDE

## Demo

![Simulated Annealing](https://raw.githubusercontent.com/gtugablue/Project-Management-Optimizer/master/final%20report/images/sa_result_exp1.png)

## Authors

* **Duarte Pinto** - *Initial work* - [444Duarte](https://github.com/444Duarte)
* **Filipa Ramos** - *Initial work* - [FilipaRamos](https://github.com/FilipaRamos)
* **Gustavo Silva** - *Initial work* - [gtugablue](https://github.com/gtugablue)
