# Dispatcher Shell

## 📝 About
This is a simple shell written in Java. It is a project made in an effort to simulate of how processes are executed in a real operating system.
## 💡 Walkthrough

The dispatcher shell takes in a text file as an argument. The text file contains a list of processes to be executed(explained below). The shell then reads the file and creates a process for each line in the file.
It consists of two major process queues:
1.  **Real Time Queue**: This queue contains all real time processes(priority value = 0) and excutes them on the fly with out any interruption. The processes are sorted by their priority and arrival time. This queue executes the processes in a First-Come, First-Served Algorithm(FCFS) fashion.
<br><br>
3. **User Job Queue**: This is a multilevel feedback queue which contains all user jobs(priority value = 1, 2, 3). The processes are sorted by their priority and arrival time. The processes are then executed in a Round-Robin fashion. The time quantum is 1 second.
<br><br>

## 🧩 Diagram
<br>

![A screenshot](./diagram/Blank%20diagram%20(1).png)

<br>

## 🚀 Getting Started
1.  Clone the repository
    <br><br>
    ```
     git clone https://github.com/bedre7/dispatcher-shell.git
    ```
2. Make a list of processes to be executed in a text file with the following format
    <br><br>
   ```
   <arrival time>, <priority[0 - 3]>, <burst time>
   ```
3. Run the program on your terminal
    <br><br>
   ```
   java -jar Program.jar <path to your text file>
   ```