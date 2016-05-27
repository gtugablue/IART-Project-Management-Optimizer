package optimizer.solver.simulated_annealing;

import optimizer.Problem;
import optimizer.domain.Element;
import optimizer.domain.Task;

import java.util.Arrays;
import java.util.List;

/**
 * Created by duarte on 15-04-2016.
 */
public class ScheduleGenerationScheme {
    private Schedule schedule;

    public ScheduleGenerationScheme(Schedule schedule){
        this.schedule = schedule;
    }

    public ScheduleGenerationScheme(Problem problem){
        Schedule schedule = new Schedule(problem);
        this.schedule = schedule;
    }

    public void calculateTimes(){
        calculateTimes(0);
    }
    public void calculateTimes(int startPosition){
        List<Task> taskList = schedule.getOrderedTasks();
        List<Task> sublist = taskList.subList(startPosition,taskList.size()-1);
        clearTasks(sublist);

        assignElementsToTasks(taskList,startPosition);
    }

    private void assignElementsToTasks(List<Task> taskList, int start){
        int currStartTime = (start > 0) ? schedule.getTaskStartTime(taskList.get(start-1)) : 0;

        for (int i = start; i < taskList.size(); i++){
            Task task = taskList.get(i);
            boolean assigned = false;
            currStartTime--;
            while (!assigned) {
                currStartTime++;
                for (Element element : schedule.getProblem().getElements()) {
                    element.assign(currStartTime,task);
                }
                /*int previousNumberElements;
                do{
                    previousNumberElements = task.getAssignedElements().size();
                    //calcular duração
                    //verificar se não choca
                }while (previousNumberElements !=task.getAssignedElements().size() && task.getAssignedElements().size() > 0);*/

                int duration = task.calculateEfectiveDuration();

                if(duration != 0){
                    assigned= true;
                }
            }
        }
    }

    private void clearTasks(List<Task> tasks){
        clearTasks(tasks.toArray(new Task[tasks.size()]));
    }

    /**
     * Removes from all the assignements and deletes assign times and completion times
     * @param tasks
     */
    private void clearTasks(Task... tasks){
        schedule.clearTask(tasks);
        for(Element element : schedule.getProblem().getElements()){
            element.removeFromTaskArray(tasks);
        }
    }
}
