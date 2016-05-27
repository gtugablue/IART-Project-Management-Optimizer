package optimizer.solver.simulated_annealing;

import optimizer.Problem;
import optimizer.Solution;
import optimizer.domain.Element;
import optimizer.domain.Task;

import java.util.*;

/**
 * Created by duarte on 15-04-2016.
 */
public class Schedule extends Solution{
    protected List<Task> orderedTasks = new ArrayList<>();

    Schedule(Problem problem){
        super(problem);

        orderedTasks.addAll(getGeneratedOrderedList(problem.getTasks()));

        for (Task task : problem.getTasks()){
            taskStartTimes.put(task,0);
            taskCompletionTimes.put(task,0);
        }
    }

    public List<Task> getGeneratedOrderedList(List<Task> tasks){
        ArrayList<Task> orderedList= new ArrayList<>();

        for(Task j : tasks){
            if(j.getPosition() != null)
                continue;
            for(Task precedence : j.getPrecedences()){
                if(orderedList.contains(precedence))
                    continue;
                insertTask(orderedList,precedence);
            }
            insertTask(orderedList,j);
        }
        return orderedList;
    }

    private void assignPositionTask(List<Task> list, Task task, int i){
        list.add(i,task);
        task.setPosition(i);
        for (int j = i+1; j < list.size(); j++) {
            list.get(j).setPosition(j);
        }
    }

    private void removeTaskPosition(List<Task> list, Task task){
        int i = task.getPosition();
        list.remove(i);
        for (int j = i; j < list.size(); j++) {
            list.get(j).setPosition(j);
        }
    }

    private int insertTask(List<Task> list, Task task){
        int position=-1;

        Random random = new Random();
        int min = task.getLastPrecedence();
        int max = task.getFirstSuccessor(list.size());

        position = random.nextInt(max - min + 1) + min;

        assignPositionTask(list,task,position);
        return position;
    }

    public void clearTask(Task... tasks){
        for(Task task :tasks ){
            taskStartTimes.remove(task);
            taskCompletionTimes.remove(task);
        }
    }

    public List<Task> getOrderedTasks() {
        return orderedTasks;
    }

    public void setOrderedTasks(List<Task> orderedTasks) {
        this.orderedTasks = orderedTasks;
    }
}
