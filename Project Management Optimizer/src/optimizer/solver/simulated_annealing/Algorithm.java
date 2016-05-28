package optimizer.solver.simulated_annealing;

import optimizer.domain.Task;

/**
 * Created by duarte on 28-05-2016.
 */
public class Algorithm {
    public float eval(Schedule schedule){
        float bigger = 0;
        for(Task task : schedule.getOrderedTasks()){
            float completionTime = schedule.getTaskCompletionTime(task);
            if(completionTime > bigger){
                bigger = completionTime;
            }
        }

        return bigger;
    }
}
