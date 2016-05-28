package optimizer.solver.simulated_annealing;

import optimizer.Problem;
import optimizer.domain.Task;

/**
 * Created by duarte on 28-05-2016.
 */
public class Algorithm implements Runnable{
    protected final float rate;
    protected float initTemperature;
    protected Problem problem;

    public Algorithm(Problem problem, float rate, float initTemperature){
        this.problem = problem;
        this.rate = rate;
        this.initTemperature = initTemperature;
    }

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


    @Override
    public void run() {
        float temperature = initTemperature;
        Schedule schedule = new Schedule(problem);
        ScheduleGenerationScheme scheme = new ScheduleGenerationScheme(schedule);
        float bestAll = eval(schedule);

        while(temperature > 0){



            temperature *= rate;
        }
    }
}
