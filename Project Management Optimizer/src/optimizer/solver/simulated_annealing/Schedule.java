package optimizer.solver.simulated_annealing;

import optimizer.Optimizer;
import optimizer.Problem;
import optimizer.Solution;
import optimizer.domain.Element;
import optimizer.domain.Task;
import scala.Array;
import scala.xml.Elem;

import java.util.*;

/**
 * Created by duarte on 15-04-2016.
 */
public class Schedule extends Solution{
    protected List<Task> orderedTasks = new ArrayList<>();
    protected Map<Element,Map<Float,Task>> elementsAssignementTimes = new HashMap<>();
    private Map<Task ,Set<Element>> taskAssignedElements = new HashMap<>();


    Schedule(Problem problem){
        super(problem);
        initMaps();
        orderedTasks.addAll(getGeneratedOrderedList(problem.getTasks()));
    }

    private void initMaps() {
        Problem problem = getProblem();
        for(Element element : problem.getElements()){
            elementsAssignementTimes.put(element, new HashMap<>());
        }

        for (Task task : problem.getTasks()){
            taskAssignedElements.put(task, new HashSet<>());
            taskStartTimes.put(task,(float)0);
            taskCompletionTimes.put(task,(float)0);
        }
    }

    public Schedule(Problem problem, List<Task> tasks, Map<Element,Map<Float,Task>> elementsAssignementTimes,Map<Task ,Set<Element>> taskAssignedElements){
        super(problem);
        orderedTasks = tasks;
        this.elementsAssignementTimes = elementsAssignementTimes;
        this.taskAssignedElements = taskAssignedElements;
    }

    public List<Task> getGeneratedOrderedList(List<Task> tasks){
        ArrayList<Task> orderedList= new ArrayList<>();

        for(Task j : tasks){
            if(j.getPosition() != null)
                continue;
            insertTask(orderedList,j);
        }
        return orderedList;
    }

    private static void assignPositionTask(List<Task> list, Task task, int i){
        list.add(i,task);
        task.setPosition(i);
        for (int j = i+1; j < list.size(); j++) {
            list.get(j).setPosition(j);
        }
    }

    public Task removeTaskPosition(int position){
        Task task = getOrderedTasks().get(position);
        removeTaskPosition(getOrderedTasks(), task);
        return task;
    }

    public static void removeTaskPosition(List<Task> list, Task task){
        int i = task.getPosition();
        list.remove(i);
        task.setPosition(null);
        for (int j = i; j < list.size(); j++) {
            list.get(j).setPosition(j);
        }
    }

    public int insertTask(Task task){
        return insertTask(getOrderedTasks(), task);
    }

    public static int insertTask(List<Task> list, Task task){
        for(Task precedence : task.getPrecedences()){
            if(list.contains(precedence))
                continue;
            insertTask(list,precedence);
        }

        int position=-1;

        Random random = new Random();
        int min = task.getLastPrecedence();
        int max = task.getFirstSuccessor(list.size());

        position = random.nextInt(max - min + 1) + min;

        assignPositionTask(list,task,position);
        return position;
    }

    public void clearTasks(List<Task> tasks){
        clearTasks(tasks.toArray(new Task[tasks.size()]));
    }

    public void clearTasks(Task... tasks){
        for(Element element : getProblem().getElements()){
            removeElementFromTaskArray(element,tasks);
        }

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

    public boolean elementCanBeAssigned(float start, float end, Element element){
        Map<Float,Task> assignementTimes = elementsAssignementTimes.get(element);
        Set<Float> keys = assignementTimes.keySet();

        for(Float t_start : keys){
            float t_end = assignementTimes.get(t_start).getDuration()+start;
            if((start > t_start && start < t_end)
                    || (t_start > start && t_start < end)){
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if a client could be assigned to a specific project that starts and finishes at start time
     * @param start
     * @return
     */
    public boolean elementIsFree(Float start, Element element){
        return elementCanBeAssigned(start,start, element);
    }

    /**
     * Verifies if a Element is free to be assigned to a task that starts and finishes at start time and if true assigns it
     * @param start
     * @param task
     * @return
     */
    public boolean assignElement(float start, Task task, Element element){
        Map<Float, Task> assignementTimes = elementsAssignementTimes.get(element);

        if (! element.hasSkill(task.getSkill()) || assignementTimes.containsValue(task))
            return false;
        if(elementIsFree(start, element)){

            assignementTimes.put(start,task);
            taskAssignedElements.get(task).add(element);
            return true;
        }else{
            return false;
        }
    }

    public void removeElementFromTaskArray(Element element,Task... tasks){
        for(Task task : tasks){
            removeElementFromTask(task,element);
        }
    }

    public boolean removeElementFromTask(Task task, Element element){
        Map<Float,Task> assignementTimes = elementsAssignementTimes.get(element);

        if(!assignementTimes.containsValue(task)){
            return false;
        }

        Set<Float> keys = assignementTimes.keySet();
        for (Float startTime : keys){
            Task t_task = assignementTimes.get(startTime);
            if(t_task.equals(task)) {
                assignementTimes.remove(startTime);
                taskRemoveAssignedElement(task,element);
                break;
            }
        }
        return true;
    }

    public boolean taskRemoveAssignedElement(Task task,Set<Element> elements){
        return taskRemoveAssignedElement(task,elements.toArray(new Element[elements.size()]));
    }

    public boolean taskRemoveAssignedElement(Task task,Element... elements){
        Set<Element> assignedElements = taskAssignedElements.get(task);
        return assignedElements.removeAll(Arrays.asList(elements));
    }

    public int taskCalculateEfectiveDuration(Task task){
        Set<Element> assignedElements = taskAssignedElements.get(task);

        float sum = 0;

        for(Element element : assignedElements){
            float tempDuration = task.getDuration()/element.getSkillPerfomance(task.getSkill());
            sum += 1/(tempDuration);
        }
        float duration = 1/sum;
        return (int) Math.ceil(duration);
    }

    public Map<Element, Map<Float, Task>> getElementsAssignementTimes() {
        return elementsAssignementTimes;
    }

    public void setElementsAssignementTimes(Map<Element, Map<Float, Task>> elementsAssignementTimes) {
        this.elementsAssignementTimes = elementsAssignementTimes;
    }

    public Map<Task, Set<Element>> getTaskAssignedElements() {
        return taskAssignedElements;
    }

    public void setTaskAssignedElements(Map<Task, Set<Element>> taskAssignedElements) {
        this.taskAssignedElements = taskAssignedElements;
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule(Optimizer.generateRandomProblem());
        schedule.toString();
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "orderedTasks=" + orderedTasks +
                '}';
    }

    @Override
    public Object clone() {
        return new Schedule(getProblem(), getOrderedTasks(),getElementsAssignementTimes(),getTaskAssignedElements());
    }
}
