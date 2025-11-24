# Lab 09: FIFO Page Replacement Simulator

## Learning Objectives
- Understand the FIFO (First-In-First-Out) page replacement algorithm
- Implement FIFO using Queue and Set data structures
- Observe how frame allocation affects page fault rates
- Witness Belady's Anomaly in action

## Prerequisites
- Basic Java programming knowledge
- Understanding of Queue and Set data structures
- Completion of Memory Management Part 2 lesson

## Introduction

When physical memory fills up, the operating system must decide which pages to remove to make room for new ones. FIFO (First-In-First-Out) is the simplest page replacement algorithm—it always removes the oldest page in memory, regardless of whether that page is still being used.

Think of FIFO like a line at a store: the first person who entered the line is the first to leave. It doesn't matter if they're still shopping—if they were first in, they're first out.

In this lab, you'll implement the core FIFO algorithm and discover something surprising: **Belady's Anomaly**. Sometimes, giving a process MORE memory frames actually causes MORE page faults. This counterintuitive behavior demonstrates why FIFO isn't used alone in modern operating systems.

## What You'll Implement

Complete **1 TODO task** in one file:

- **TODO 1**: Implement the FIFO page replacement algorithm using Queue and Set

Everything else is provided: test data, metrics calculation, output formatting, and the main method.

## Lab File Structure

**FIFOPageReplacementLab.java** - The only file you need

- `simulateFIFO()` method - **TODO 1** (you implement the algorithm)
- `displayResults()` method - Fully provided (formats output)
- `main()` method - Fully provided (runs test cases)

## Project Setup

1. Download `FIFOPageReplacementLab.java`
2. Complete TODO 1
3. Compile: `javac FIFOPageReplacementLab.java`
4. Run: `java FIFOPageReplacementLab`

## Understanding FIFO Page Replacement

### The Algorithm

FIFO maintains a queue of pages currently in physical memory. When a page fault occurs and all frames are occupied, FIFO removes the page at the front of the queue (the oldest) and adds the new page to the back.

### Key Concepts

- **Page**: A block of memory identified by a number (like 1, 2, 3)
- **Frame**: A slot in physical memory that holds one page
- **Page Fault**: Occurs when the requested page isn't in memory
- **Page Hit**: Occurs when the requested page is already in memory
- **Reference String**: Sequence of page accesses (example: `[7, 0, 1, 2, 0, 3]`)

### Algorithm Steps

1. Start with empty frames
2. For each page access:
    - **If page is in memory**: Page HIT (do nothing)
    - **If page is NOT in memory and frames have space**: Page FAULT (add page to queue)
    - **If page is NOT in memory and frames are full**: Page FAULT (remove oldest page from queue, add new page)
3. Count total faults and hits

### Example Walkthrough

**Reference String**: `[1, 2, 3, 4, 1, 2, 5]`  
**Frames**: 3

```
Access | Frames      | Result
-------|-------------|------------------
  1    | [1]         | FAULT (empty frame)
  2    | [1,2]       | FAULT (empty frame)
  3    | [1,2,3]     | FAULT (empty frame)
  4    | [2,3,4]     | FAULT (replaced 1)
  1    | [3,4,1]     | FAULT (replaced 2)
  2    | [4,1,2]     | FAULT (replaced 3)
  5    | [1,2,5]     | FAULT (replaced 4)

Total Faults: 7
Total Hits: 0
```

## Expected Output

```
========================================
FIFO Page Replacement Simulator
========================================

Test Case 1: Random Access Pattern
Reference String: [7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1]

--- Running with 3 frames ---
Page Faults: 15
Page Hits: 5
Total Accesses: 20
Hit Rate: 25.0%
Fault Rate: 75.0%

--- Running with 4 frames ---
Page Faults: 11
Page Hits: 9
Total Accesses: 20
Hit Rate: 45.0%
Fault Rate: 55.0%

========================================

Test Case 2: Belady's Anomaly Example
Reference String: [1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5]

--- Running with 3 frames ---
Page Faults: 9
Page Hits: 3
Total Accesses: 12
Hit Rate: 25.0%
Fault Rate: 75.0%

--- Running with 4 frames ---
Page Faults: 10
Page Hits: 2
Total Accesses: 12
Hit Rate: 16.7%
Fault Rate: 83.3%

⚠️  BELADY'S ANOMALY DETECTED! ⚠️
With 3 frames: 9 page faults
With 4 frames: 10 page faults
Adding more frames INCREASED page faults!

========================================

Test Case 3: Looping Pattern
Reference String: [1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4]

--- Running with 3 frames ---
Page Faults: 12
Page Hits: 0
Total Accesses: 12
Hit Rate: 0.0%
Fault Rate: 100.0%

--- Running with 4 frames ---
Page Faults: 4
Page Hits: 8
Total Accesses: 12
Hit Rate: 66.7%
Fault Rate: 33.3%

========================================
Simulation Complete!
========================================
```

## Code Template

```java
import java.util.*;

public class FIFOPageReplacementLab {

    /**
     * TODO 1: Implement FIFO Page Replacement Algorithm
     * 
     * Simulates FIFO page replacement for a given reference string and number of frames.
     * Returns an array: [pageFaults, pageHits]
     * 
     * Algorithm:
     *   1. Create a Queue<Integer> to represent frames in memory (use LinkedList)
     *   2. Create a Set<Integer> to quickly check if a page is in memory (use HashSet)
     *   3. Initialize pageFaults = 0 and pageHits = 0
     *   4. For each page in the referenceString:
     *      a. If page is in the Set (memory):
     *         - Increment pageHits
     *      b. If page is NOT in the Set:
     *         - Increment pageFaults
     *         - If queue size equals numFrames (frames are full):
     *           * Remove the oldest page: int victim = queue.poll()
     *           * Remove victim from the Set
     *         - Add the new page to the queue
     *         - Add the new page to the Set
     *   5. Return new int[]{pageFaults, pageHits}
     * 
     * Example:
     *   referenceString = [1, 2, 3, 1]
     *   numFrames = 2
     *   
     *   Access 1: queue=[1], set={1}, FAULT
     *   Access 2: queue=[1,2], set={1,2}, FAULT  
     *   Access 3: queue=[2,3], set={2,3}, FAULT (removed 1)
     *   Access 1: queue=[3,1], set={3,1}, FAULT (removed 2)
     *   
     *   Result: [4 faults, 0 hits]
     */
    public static int[] simulateFIFO(int[] referenceString, int numFrames) {
        int pageFaults = 0;
        int pageHits = 0;

        // TODO 1: Implement FIFO algorithm here
        // Queue<Integer> queue = new LinkedList<>();
        // Set<Integer> pagesInMemory = new HashSet<>();
        
        // for (int page : referenceString) {
        //     if (pagesInMemory.contains(page)) {
        //         // Page HIT
        //     } else {
        //         // Page FAULT
        //         if (queue.size() == numFrames) {
        //             // Remove oldest page
        //         }
        //         // Add new page
        //     }
        // }

        return new int[]{pageFaults, pageHits};
    }

    /**
     * Display results for a test case (FULLY PROVIDED)
     */
    public static void displayResults(String testName, int[] referenceString, 
                                     int frames1, int frames2) {
        System.out.println("\nTest Case: " + testName);
        System.out.print("Reference String: [");
        for (int i = 0; i < referenceString.length; i++) {
            System.out.print(referenceString[i]);
            if (i < referenceString.length - 1) System.out.print(", ");
        }
        System.out.println("]\n");

        // Test with first frame count
        int[] result1 = simulateFIFO(referenceString, frames1);
        System.out.println("--- Running with " + frames1 + " frames ---");
        System.out.println("Page Faults: " + result1[0]);
        System.out.println("Page Hits: " + result1[1]);
        System.out.println("Total Accesses: " + referenceString.length);
        System.out.printf("Hit Rate: %.1f%%\n", 
            (result1[1] * 100.0) / referenceString.length);
        System.out.printf("Fault Rate: %.1f%%\n\n", 
            (result1[0] * 100.0) / referenceString.length);

        // Test with second frame count
        int[] result2 = simulateFIFO(referenceString, frames2);
        System.out.println("--- Running with " + frames2 + " frames ---");
        System.out.println("Page Faults: " + result2[0]);
        System.out.println("Page Hits: " + result2[1]);
        System.out.println("Total Accesses: " + referenceString.length);
        System.out.printf("Hit Rate: %.1f%%\n", 
            (result2[1] * 100.0) / referenceString.length);
        System.out.printf("Fault Rate: %.1f%%\n\n", 
            (result2[0] * 100.0) / referenceString.length);

        // Check for Belady's Anomaly
        if (result2[0] > result1[0]) {
            System.out.println("⚠️  BELADY'S ANOMALY DETECTED! ⚠️");
            System.out.println("With " + frames1 + " frames: " + result1[0] + " page faults");
            System.out.println("With " + frames2 + " frames: " + result2[0] + " page faults");
            System.out.println("Adding more frames INCREASED page faults!\n");
        }

        System.out.println("========================================");
    }

    /**
     * Main method with test cases (FULLY PROVIDED)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("FIFO Page Replacement Simulator");
        System.out.println("========================================");

        // Test Case 1: Random access pattern
        int[] test1 = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
        displayResults("Random Access Pattern", test1, 3, 4);

        // Test Case 2: Belady's Anomaly example
        int[] test2 = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};
        displayResults("Belady's Anomaly Example", test2, 3, 4);

        // Test Case 3: Looping pattern
        int[] test3 = {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};
        displayResults("Looping Pattern", test3, 3, 4);

        System.out.println("\nSimulation Complete!");
        System.out.println("========================================");
    }
}
```

## Common Mistakes to Avoid

### 1. Forgetting to Remove from Both Data Structures

```java
// WRONG - Only removes from queue
int victim = queue.poll();

// CORRECT - Remove from both
int victim = queue.poll();
pagesInMemory.remove(victim);
```

### 2. Not Checking if Frames are Full

```java
// WRONG - Always removes
int victim = queue.poll();

// CORRECT - Check first
if (queue.size() == numFrames) {
    int victim = queue.poll();
    pagesInMemory.remove(victim);
}
```

### 3. Adding Before Removing

```java
// WRONG - Can exceed frame limit
queue.offer(page);
if (queue.size() > numFrames) {
    queue.poll();
}

// CORRECT - Remove first if needed
if (queue.size() == numFrames) {
    int victim = queue.poll();
    pagesInMemory.remove(victim);
}
queue.offer(page);
```

## Analysis Questions

After completing the lab, answer these questions:

1. **Belady's Anomaly**: In Test Case 2, why does adding a 4th frame cause MORE page faults than having only 3 frames? This seems counterintuitive.

2. **Data Structures**: Why do we need both a Queue AND a Set? What would happen if we only used a Queue?

3. **Performance**: If memory access takes 200 nanoseconds and a page fault takes 8 milliseconds, calculate the effective access time for Test Case 1 with 3 frames. (Use the formula from the lesson)

4. **Pattern Analysis**: Which test case had the worst performance with 3 frames? Why did that particular access pattern cause so many page faults?

5. **Real World**: Based on your observations, why do you think modern operating systems don't use pure FIFO for page replacement?

## What You're Learning

This lab isn't just about implementing FIFO—you're learning:

- **Data Structure Synergy**: How Queue and Set work together for efficiency
- **Algorithm Limitations**: Why simple solutions can have unexpected behaviors
- **Performance Impact**: How page replacement directly affects system performance
- **Trade-offs**: Understanding when simplicity isn't worth the cost

The key insight: **FIFO is easy to implement but performs poorly**. Belady's Anomaly proves that FIFO doesn't consider actual page usage patterns. This is why modern systems use more sophisticated algorithms like LRU.

## Compilation and Execution

```bash
javac FIFOPageReplacementLab.java
java FIFOPageReplacementLab
```

## Submission Requirements

After completing your work:

```bash
git add .
git commit -m "completed lab 08 - FIFO page replacement"
git push origin main
```

Include:
- Completed `FIFOPageReplacementLab.java` with TODO 1 implemented
- Screenshot showing successful execution with all three test cases
- Answers to the 5 analysis questions
- Verification that Belady's Anomaly is detected in Test Case 2

---

**Good luck! Understanding why FIFO fails is just as important as making it work.**