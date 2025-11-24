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
        
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> pagesInMemory = new HashSet<>();
        
        for(int page: referenceString) {
        	if(pagesInMemory.contains(page)) {
        		pageHits++;
        	}else {
        		pageFaults++;
        		if(queue.size() == numFrames) {
        			int victim = queue.poll();
        			pagesInMemory.remove(victim);
        		}
        		queue.offer(page);
        		pagesInMemory.add(page);
        	}
        }
        

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