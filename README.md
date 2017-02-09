# IART-Project-Management-Optimizer

Project management optimizer that allocates members to a project in an efficient way taking in account the duration of each task and the skills of each member for each task.

The problem data is imported from a JSON file which contains a list of tasks, team members and their skills. Each member has a list of skills rated according to his capacity. For example, one team member may have a skill of 0.6 in computer engineering and a skill of 1 in economics.

The objective of the optimization is to minimize the time it takes to finish the whole project, or, in other words, the time it takes for the last task to be completed.

For that, two different meta-heuristics have been applied: Genetic Algorithms and Simulated Annealing. Experiments have been made with each heuristic and different configurations. The results are available in the project's final report.

A graphical user interface has been developed to allow the user to choose the algorithm and configurations, as well as being able to see it run in real-time with statistics and the possibility of analysing the best solution.

![Simulated Annealing](https://raw.githubusercontent.com/gtugablue/Project-Management-Optimizer/master/docs/final_report/images/sa_result_exp1.png)
