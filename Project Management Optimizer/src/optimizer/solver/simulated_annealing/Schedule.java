package optimizer.solver.simulated_annealing;

import optimizer.domain.Element;
import optimizer.domain.Task;

import java.util.*;

/**
 * Created by duarte on 15-04-2016.
 */
public class Schedule {
    protected List<Task> orderedTasks = new ArrayList<>();
    protected List<Integer> startTimes = new ArrayList<>();
    protected List<Integer> finishTimes = new ArrayList<>();
    protected List<Element> resources = new ArrayList<>();

    Schedule(List<Task> tasks, List<Element> resources){
        orderedTasks.addAll(tasks);

        this.resources.addAll(resources);

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

    // TODO: 19-04-2016  
    private int insertTask(List<Task> list, Task task){
        boolean inserted = false;
        int position=-1;

        Random random = new Random();
        int min = task.getLastPrecedence();
        int max = task.getFirstSuccessor(list.size());

        do {
            position = random.nextInt(max - min + 1) + min;
//            if()
        }while(!inserted);


        return position;
    }
}
