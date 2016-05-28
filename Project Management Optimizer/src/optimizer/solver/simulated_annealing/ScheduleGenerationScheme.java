package optimizer.solver.simulated_annealing;

import optimizer.Problem;
import optimizer.domain.Element;
import optimizer.domain.Task;

import java.util.List;
import java.util.Random;

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
        schedule.clearTasks(sublist);

        assignElementsToTasks(taskList,startPosition);
    }

    private void assignElementsToTasks(List<Task> taskList, int start){
        float currStartTime = (start > 0) ? schedule.getTaskStartTime(taskList.get(start-1)) : (float)0;

        for(Element  element : schedule.getProblem().getElements()){

        }

        for (int i = start; i < taskList.size(); i++){
            Task task = taskList.get(i);
            boolean assigned = false;
            currStartTime--;
            while (!assigned) {
                currStartTime++;
                boolean precedenceNotFinished = false;
                for (Task precedence : task.getPrecedences()){
                    if(schedule.getTaskCompletionTime(precedence) < currStartTime){
                        precedenceNotFinished = true;
                        break;
                    }
                }

                if(precedenceNotFinished){
                    break;
                }

                for (Element element : schedule.getProblem().getElements()) {
                    schedule.assignElement(currStartTime,task, element);
                }
                /*int previousNumberElements;
                do{
                    previousNumberElements = task.getAssignedElements().size();
                    //calcular duração
                    //verificar se não choca
                }while (previousNumberElements !=task.getAssignedElements().size() && task.getAssignedElements().size() > 0);*/

                int duration = schedule.taskCalculateEfectiveDuration(task);

                if(duration != 0){
                    assigned= true;
                }
            }
        }
    }

    public void generateNewState(){
        Random random = new Random();
        int i = random.nextInt(schedule.getOrderedTasks().size());
        Task removedTask = schedule.removeTaskPosition(i);
        int newPosition = schedule.insertTask(removedTask);
        int timesCalculationPosition = (newPosition < i)? newPosition : i;
        calculateTimes();
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
