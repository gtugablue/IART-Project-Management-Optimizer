package optimizer.solver.simulated_annealing;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.domain.Task;
import java.rmi.ConnectIOException;
import java.util.Random;

/**
 * Created by duarte on 28-05-2016.
 */
public class Algorithm implements Runnable{
    protected final double  coolingRate;
    private final int cicles;
    protected int i;
    protected double initTemperature;
    protected Problem problem;
    protected Schedule schedule;
    protected float bestAll;
    protected Schedule bestAllSchedule;
    protected ScheduleGenerationScheme scheme;
    protected double temperature;
    protected float score;
    private Random random;


    public Algorithm(Problem problem, double coolingRate, double initTemperature, int cicles){
        this.problem = problem;
        this.coolingRate = coolingRate;
        this.initTemperature = initTemperature;
        this.cicles = cicles;
        this.schedule = new Schedule(problem);

        this.random = new Random();
        this.temperature = initTemperature;
        this.scheme = new ScheduleGenerationScheme(schedule);
        this.bestAll = eval(schedule);
        this.bestAllSchedule = (Schedule) schedule.clone();
        this.score = bestAll;
        this.i = 0;

    }

    public Algorithm(Problem problem, Config config){
        this(problem,config.coolingRate,config.initTemperature, config.cicles);
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public double getCoolingRate() {
        return coolingRate;
    }

    public int getCicles() {
        return cicles;
    }

    public double getInitTemperature() {
        return initTemperature;
    }

    public Problem getProblem() {
        return problem;
    }

    public float getBestAll() {
        return bestAll;
    }

    public Schedule getBestAllSchedule() {
        return bestAllSchedule;
    }

    public ScheduleGenerationScheme getScheme() {
        return scheme;
    }

    public double getTemperature() {
        return temperature;
    }

    public float getScore() {
        return score;
    }

    public int getI(){
        return i;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();

        for (int i = 0 ; i < cicles; i++){
            newIteration();
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime)/1000000;
        bestAllSchedule.correctPositions();
        /*System.out.println(bestAllSchedule.toString());
        System.out.println(bestAllSchedule.getElementsAssignementTimes());
        System.out.println(bestAllSchedule.getTaskAssignedElements());
        System.out.println("T: "+temperature+" bestAll: "+bestAll+" score: "+score);
        System.out.println("Duration: "+duration);*/

    }

    public void newIteration() {
        i++;
        //System.out.println("T: "+temperature+" bestAll: "+bestAll+" score: "+score );
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
            double boltzmannFactor = Math.exp(-Math.abs(delta/temperature));
            //System.out.println("Boltzmann Factor:"+boltzmannFactor+"");
            if(boltzmannFactor > random.nextFloat()){
                score = eval;
                schedule = scheme.getSchedule();
            }
        }
        temperature = coolingRate*temperature;
    }

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm(Optimizer.generateRandomProblem(), 0.9999,1000,50000);
        algorithm.run();
    }
}
