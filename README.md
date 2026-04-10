
## 📌 Overview

This project is a **mini Operating System simulator** that executes programs, manages memory, schedules processes, and controls access to shared resources.

It was developed as part of **CSEN 602 – Operating Systems** at the German University in Cairo.

---

## 🚀 Features

### 🧾 Interpreter
- Parses `.txt` program files
- Executes custom instruction set
- Converts programs into processes dynamically

### 🧠 Memory Management
- Fixed-size memory (40 words)
- Stores:
  - Instructions
  - Variables
  - Process Control Blocks (PCB)
- Supports **process swapping (Disk ↔ Memory)**

### 🔄 Process Management
Each process includes:
- Process ID
- State (Ready, Running, Blocked, Finished)
- Program Counter
- Memory boundaries

### ⏱️ Scheduling Algorithms
- **Round Robin (RR)** → Preemptive (2 instructions per time slice)
- **Highest Response Ratio Next (HRRN)** → Non-preemptive  
- *(Optional)* Multi-Level Feedback Queue (MLFQ)

### 🔐 Synchronization (Mutexes)
Ensures safe access to shared resources:
- 🖥️ User Input  
- 📺 User Output  
- 📁 File Access  
