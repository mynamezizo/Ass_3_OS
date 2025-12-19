import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class PreemptiveSJFScheduler extends SJFScheduler {
    
    public PreemptiveSJFScheduler(List<Process> processes, int contextSwitchTime) {
        super(processes, contextSwitchTime);
    }
    
    @Override
    public void schedule() {
        List<Process> processList = new ArrayList<>();
        for (Process p : processes) {
            Process copy = new Process(p.name, p.arrivalTime, p.burstTime, p.priority, p.quantum);
            processList.add(copy);
        }
        
        processList.sort(Comparator.comparingInt(p -> p.arrivalTime));
        
        int completed = 0;
        int n = processList.size();
        Process lastProcess = null;
        
        while (completed < n) {
            List<Process> readyQueue = new ArrayList<>();
            for (Process p : processList) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    readyQueue.add(p);
                }
            }
            
            if (readyQueue.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : processList) {
                    if (p.remainingTime > 0 && p.arrivalTime > currentTime) {
                        nextArrival = Math.min(nextArrival, p.arrivalTime);
                    }
                }
                currentTime = nextArrival;
                continue;
            }
            
            Process currentProcess = readyQueue.stream()
                .min(Comparator.comparingInt(p -> p.remainingTime))
                .orElse(null);
            
            if (currentProcess == null) break;
            
            if (lastProcess != null && !lastProcess.name.equals(currentProcess.name)) {
                currentTime += contextSwitchTime;
                executionOrder.add("Context Switch");
            }
            
            executionOrder.add(currentProcess.name + " (Time: " + currentTime + ")");
            
            currentProcess.remainingTime--;
            currentTime++;
            
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                completed++;
            }
            
            lastProcess = currentProcess;
        }
        
        for (int i = 0; i < processes.size(); i++) {
            processes.get(i).completionTime = processList.get(i).completionTime;
            processes.get(i).remainingTime = processList.get(i).remainingTime;
        }
        
        calculateTimes();
    }
