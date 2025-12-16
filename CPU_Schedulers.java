import java.util.*;


class Process {

    public String name;
    public int arrivalTime;
    public int burstTime;
    public int remainingTime;
    public int priority;
    public int quantum;

    // Results
    public int completionTime;
    public int waitingTime;
    public int turnaroundTime;

    // For AG Scheduler
    public List<Integer> quantumHistory;

    public Process(String name,
            int arrivalTime,
            int burstTime,
            int priority,
            int quantum) {

        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;

        this.quantumHistory = new ArrayList<>();
    }
}


 //Scheduler Base Class
 
abstract class Scheduler {

    protected List<Process> processes;
    protected List<String> executionOrder;

    protected int contextSwitchTime;
    protected int currentTime;

    public Scheduler(List<Process> processes, int contextSwitchTime) {
        this.processes = processes;
        this.contextSwitchTime = contextSwitchTime;
        this.executionOrder = new ArrayList<>();
        this.currentTime = 0;
    }

    /* ===== MUST OVERRIDE ===== */
    public abstract void schedule();

    /* ===== Shared Utilities ===== */
    protected void calculateTimes() {
        for (Process p : processes) {
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
    }

    public List<String> getExecutionOrder() {
        return executionOrder;
    }

    public List<Process> getProcesses() {
        return processes;
    }
}


 //Round Robin Scheduler


abstract class RoundRobinScheduler extends Scheduler {

    protected int timeQuantum;

    public RoundRobinScheduler(List<Process> processes,
            int contextSwitchTime,
            int timeQuantum) {
        super(processes, contextSwitchTime);
        this.timeQuantum = timeQuantum;
    }
}


 // Shortest Job First (Preemptive)
 
 
abstract class SJFScheduler extends Scheduler {

    public SJFScheduler(List<Process> processes,
            int contextSwitchTime) {
        super(processes, contextSwitchTime);
    }
}


// Priority Scheduler (Preemptive + Aging)
 
 
abstract class PriorityScheduler extends Scheduler {

    protected int agingInterval;

    public PriorityScheduler(List<Process> processes,
            int contextSwitchTime,
            int agingInterval) {
        super(processes, contextSwitchTime);
        this.agingInterval = agingInterval;
    }
}


 //AG Scheduler

 
 abstract class AGScheduler extends Scheduler {

     public AGScheduler(List<Process> processes,
             int contextSwitchTime) {
         super(processes, contextSwitchTime);
     }

     /* Optional helper hooks */
     protected abstract void updateQuantum(Process p);
 }

