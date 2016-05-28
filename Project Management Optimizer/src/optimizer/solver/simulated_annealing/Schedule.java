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
    protected Map<Element,Set<Task>> elementsAssignementTimes = new HashMap<>();
    private Map<Task ,Set<Element>> taskAssignedElements = new HashMap<>();


    Schedule(Problem problem){
        super(problem);
        initMaps();
        orderedTasks.addAll(getGeneratedOrderedList(problem.getTasks()));
    }

    private void initMaps() {
        Problem problem = getProblem();
        for(Element element : problem.getElements()){
            elementsAssignementTimes.put(element, new HashSet<>());
        }

        for (Task task : problem.getTasks()){
            taskAssignedElements.put(task, new HashSet<>());
            taskStartTimes.put(task,(float)0);
            taskCompletionTimes.put(task,Float.MAX_VALUE);
        }
    }

    public Schedule(Problem problem, List<Task> tasks, Map<Element,Set<Task>> elementsAssignementTimes,Map<Task ,Set<Element>> taskAssignedElements){
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
            putTaskInBuildingList(orderedList,j);
        }
        return orderedList;
    }

    private static void assignPositionTask(List<Task> list, Task task, int i){
        if(i >= list.size()){
            i = list.size();
            list.add(task);
        }else{
            list.add(i,task);
        }

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

    public static void putTaskInBuildingList(List<Task> list, Task task){
        int addedPrecedencies = 0;
        for(Task precedence : task.getPrecedences()){
            if(list.contains(precedence))
                continue;
            putTaskInBuildingList(list,precedence);
            addedPrecedencies++;
        }
        //System.out.println(task.getPrecedences().size()+" "+addedPrecedencies);

        assignPositionTask(list,task,list.size());
    }

    public static int insertTask(List<Task> list, Task task){
        int position=-1;

        Random random = new Random();
        int min = task.getLastPrecedence()+1;
        int max = task.getFirstSuccessor(list.size());
        //System.out.println("max "+max +" min "+min+" list_size "+list.size());;
        position = random.nextInt(max-min+1) + min;

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
            taskStartTimes.put(task,null);
            taskCompletionTimes.put(task,null);
        }
    }

    public List<Task> getOrderedTasks() {
        return orderedTasks;
    }

    public void setOrderedTasks(List<Task> orderedTasks) {
        this.orderedTasks = orderedTasks;
    }

    public boolean elementCanBeAssigned(float start, float end, Element element){
        Set<Task> assignementTimes = elementsAssignementTimes.get(element);

        for(Task task : assignementTimes){
            float t_start = getTaskStartTime(task);
            float t_end = getTaskCompletionTime(task);
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
        Set<Task> assignementTimes = elementsAssignementTimes.get(element);

        if (! element.hasSkill(task.getSkill()) || assignementTimes.contains(task))
            return false;
        if(elementIsFree(start, element)){
            assignementTimes.add(task);
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
        Set<Task> assignementTimes = elementsAssignementTimes.get(element);

        if(!assignementTimes.contains(task)){
            return false;
        }

        for (Task t_task : assignementTimes){
            if(t_task.equals(task)) {
                assignementTimes.remove(task);
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

    public double taskCalculateEfectiveDuration(Task task) throws Exception {
        Set<Element> assignedElements = taskAssignedElements.get(task);
        if (assignedElements.size() == 0)
            throw new Exception("Não há elementos adicionados na task: "+task.getName());
        float sum = 0;

        for(Element element : assignedElements){
            float tempDuration = task.getDuration()/element.getSkillPerfomance(task.getSkill());
            sum += 1/(tempDuration);
        }

        float duration = 1/sum;
        return Math.ceil(duration);
    }

    public Map<Element, Set<Task>> getElementsAssignementTimes() {
        return elementsAssignementTimes;
    }

    public void setElementsAssignementTimes(Map<Element, Set<Task>> elementsAssignementTimes) {
        this.elementsAssignementTimes = elementsAssignementTimes;
    }

    public Map<Task, Set<Element>> getTaskAssignedElements() {
        return taskAssignedElements;
    }

    public void setTaskAssignedElements(Map<Task, Set<Element>> taskAssignedElements) {
        this.taskAssignedElements = taskAssignedElements;
    }

    public boolean assignTask(Task task, float start, float end){
        if(start > end){
            return false;
        }

        for(Task precedence : task.getPrecedences()){
            float t_start = taskStartTimes.get(precedence);
            float t_end = taskCompletionTimes.get(precedence);

            if(start < t_start || start < t_end){
                //System.err.println("task "+task.getName()+" start "+ start + " t_end "+t_end+ " t_start "+t_start);
                return false;
            }

        }

        this.taskStartTimes.put(task, start);
        this.taskCompletionTimes.put(task, end);

        return true;
    }

    public static void main(String[] args) {
        Schedule schedule = new Schedule(Optimizer.generateRandomProblem());
        System.out.println(schedule.toString());
    }

    public void correctPositions(){
        for (int i = 0; i < orderedTasks.size(); i++) {
            orderedTasks.get(i).setPosition(i);
        }
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "orderedTasks=" + orderedTasks +
                '}';
    }

    @Override
    public Object clone() {
        super.toString();
        ArrayList<Task> orderedTasks = new ArrayList<>(getOrderedTasks());

        HashMap<Element, Set<Task>> elementsAssignementTimes = new HashMap<>(getElementsAssignementTimes());

        for(Element key : elementsAssignementTimes.keySet()){
            Set<Task> newSet = new HashSet<>(getElementsAssignementTimes().get(key));
            elementsAssignementTimes.put(key,newSet);
        }

        HashMap<Task, Set<Element>> taskAssignedElements = new HashMap<>(getTaskAssignedElements());
        for(Task key : taskAssignedElements.keySet()){
            Set<Element> newSet = new HashSet<>(getTaskAssignedElements().get(key));
            taskAssignedElements.put(key, newSet);
        }
        Schedule newSchedule = new Schedule(getProblem(), orderedTasks, elementsAssignementTimes, taskAssignedElements);
        newSchedule.setTaskCompletionTimes(taskCompletionTimes);
        newSchedule.setTaskStartTimes(taskStartTimes);
        newSchedule.setTaskOrder(taskOrder);
        newSchedule.setTotalTime(totalTime);

        return newSchedule;
    }
}
