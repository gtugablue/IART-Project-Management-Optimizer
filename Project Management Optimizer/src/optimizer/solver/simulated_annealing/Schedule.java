package optimizer.solver.simulated_annealing;

import optimizer.domain.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by duarte on 15-04-2016.
 */
public class Schedule {
    private List<Task> orderedTasks = new ArrayList<>();
    private List<Integer> startTimes = new ArrayList<>();
    private List<Integer> finishTimes = new ArrayList<>();

    Schedule(List<Task> tasks){
        orderedTasks.addAll(tasks);
        startTimes.addAll(Collections.nCopies(tasks.size(), null));
        finishTimes.addAll(Collections.nCopies(tasks.size(), null));
    }

    public List<Task> getGeneratedOrderedList(List<Task> tasks){
        ArrayList<Task> orderedList= new ArrayList<>();

        for(Task j : tasks){
            if(j.getPosition() != null)
                continue;
            for(Task precedence : j.getPrecedences()){
                if(orderedList.contains(precedence))
                    continue;

            }
        }
        return orderedList;
    }

    private void assignPositionTask(List<Task> list, Task task, int i){
        list.add(i,task);
        task.setPosition(i);
    }

    // TODO: 15-04-2016 Finish
    private int insertTask(List<Task> list, Task task){
        boolean inserted = false;
        int position=-1;
        do {

        }while(!inserted);


        return position;
    }
}
