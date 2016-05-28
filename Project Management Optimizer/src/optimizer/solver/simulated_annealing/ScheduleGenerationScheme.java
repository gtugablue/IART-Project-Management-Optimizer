package optimizer.solver.simulated_annealing;

import optimizer.Optimizer;
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
        calculateTimes();
    }

    public ScheduleGenerationScheme(Problem problem){
        Schedule schedule = new Schedule(problem);
        this.schedule = schedule;
        calculateTimes();
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

        for (int i = start; i < taskList.size(); i++){
            Task task = taskList.get(i);
            boolean assigned = false;
            currStartTime-=1;
            while (!assigned) {
                schedule.clearTasks(task);
                currStartTime+=1;
                boolean precedenceNotFinished = false;
                for (Task precedence : task.getPrecedences()){
                    try {
                        if (schedule.getTaskCompletionTime(precedence) > currStartTime) {
                            //System.err.println(precedence.getName() + " "+schedule.getTaskCompletionTime(precedence));
                            precedenceNotFinished = true;
                            break;
                        }
                    }catch (Exception e){
                        System.err.println(task.getName());
                        System.err.println(precedence.getName() + " - "+ schedule.getTaskCompletionTime(precedence));
                    }
                }

                if(precedenceNotFinished){
                    continue;
                }

                for (Element element : schedule.getProblem().getElements()) {
                    if(!schedule.assignElement(currStartTime,task, element)){
                        /*if(!element.hasSkill(task.getSkill())){
                            System.err.println(element.getName() + " skill doesn't match task:" +task.getName());
                            continue;
                        }

                        if(!schedule.elementIsFree(currStartTime,element)){
                            System.out.println(currStartTime+ " element "+element.getName() +"is not free");
                            continue;
                        }*/
                    }
                }

                float duration = 0;
                try {
                    duration = (float) schedule.taskCalculateEfectiveDuration(task);
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    continue;
                }
                if(duration <= 0){
                    //System.err.println("Falhou a dar assign à task:" + task.getName()+" duration:"+duration);
                    continue;
                }
                float end = currStartTime+duration;

                if(!schedule.assignTask(task,currStartTime,end)){
                    //System.err.println("Falhou a dar assign à task:" + task.getName());
                    continue;
                }
                //System.out.println("Assigned task: "+task.getName()+" start: "+currStartTime + " end "+end);
                assigned = true;
            }
        }
    }

    public void generateNewState(){
        Random random = new Random();
        int i = random.nextInt(schedule.getOrderedTasks().size());
        Task removedTask = schedule.removeTaskPosition(i);
        int newPosition = schedule.insertTask(removedTask);
        //System.out.println("Changing task: "+removedTask.getName() + " from position "+i+" to position "+newPosition);

        int timesCalculationPosition = (newPosition < i)? newPosition : i;
        calculateTimes(timesCalculationPosition);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule(Optimizer.generateRandomProblem());
        ScheduleGenerationScheme scheme = new ScheduleGenerationScheme(schedule);
        System.out.println(schedule.toString());
        scheme.generateNewState();
        System.out.println(schedule.toString());

    }
}
