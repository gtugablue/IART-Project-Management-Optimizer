package optimizer.solver.simulated_annealing;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.domain.Task;

import java.util.Random;

/**
 * Created by duarte on 28-05-2016.
 */
public class Algorithm implements Runnable{
    protected final double  coolingRate;
    private final int cicles;
    protected double initTemperature;
    protected Problem problem;

    public Algorithm(Problem problem, double coolingRate, double initTemperature, int cicles){
        this.problem = problem;
        this.coolingRate = coolingRate;
        this.initTemperature = initTemperature;
        this.cicles = cicles;
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
        long startTime = System.nanoTime();

        double temperature = initTemperature;
        Random random = new Random();
        Schedule schedule = new Schedule(problem);
        ScheduleGenerationScheme scheme = new ScheduleGenerationScheme(schedule);
        float bestAll = eval(schedule);
        Schedule bestAllSchedule = (Schedule) schedule.clone();
        float score = bestAll;
        for (int i = 0 ; i < cicles; i++){
            System.out.println("T: "+temperature+" bestAll: "+bestAll+" score: "+score);
            schedule.correctPositions();
            scheme.setSchedule((Schedule) schedule.clone());
            scheme.generateNewState();
            float eval = eval(scheme.getSchedule());
            float delta = score - eval;
            if(delta > 0){
                score = eval;
                schedule = scheme.getSchedule();

                if(score < bestAll){
                    bestAll = score;
                    bestAllSchedule = (Schedule) scheme.getSchedule().clone();
                }
            }else{
                double boltzmannFactor = Math.exp(Math.abs(delta/temperature));
                //System.out.println("Boltzmann Factor:"+boltzmannFactor+"");
                if(boltzmannFactor > random.nextFloat()){
                    score = eval;
                    schedule = scheme.getSchedule();
                }
            }
            temperature = coolingRate*temperature;
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime)/1000000;
        System.out.println(bestAllSchedule.toString());
        System.out.println(bestAllSchedule.getElementsAssignementTimes());
        System.out.println(bestAllSchedule.getTaskAssignedElements());
        System.out.println("T: "+temperature+" bestAll: "+bestAll+" score: "+score);
        System.out.println("Duration: "+duration);

    }

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm(Optimizer.loadProblemFromJSON("1.json"), 0.999,1000,50000);
        algorithm.run();
    }
}
