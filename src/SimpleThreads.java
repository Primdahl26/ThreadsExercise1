public class SimpleThreads {

    // Display a message, preceded by
    // the name of the current thread

    //Here we declare what happens when we call the threadMessage method - formatting
    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    //This class implements the runnable interface that gives us the tools to work with threads
    private static class MessageLoop
            implements Runnable {

        //Here we declare what happens when we use the .start() method of the threads.
        public void run() {

            //An array is made with 4 importantInfo elements
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };

            //Loops throug the important info, then sleeps the thread for 4 seconds, and prints out the important info
            //on the loop placement - then does it again for every element in the array
            try {
                for (int i = 0;
                     i < importantInfo.length;
                     i++) {
                    // Pause for 4 seconds
                    Thread.sleep(3000);
                    // Print a message
                    threadMessage(importantInfo[i]);
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }

    public static void main(String args[])
            throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).

        //This is one hour
        long patience = 1000 * 60 * 60;

        // If command line argument
        // present, gives patience
        // in seconds.

        //If the program is the terminal and arguments are added
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        //Starts the thread, and creates a thread object
        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        Thread u = new Thread(new MessageLoop());
        t.start();
        u.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive() &&  u.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            u.join(1000);

            //If it takes too long time... (1 hour) - and both threads a alive
            if (((System.currentTimeMillis() - startTime) > patience)
                    && t.isAlive() && u.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                u.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
                u.join();
            }
        }
        threadMessage("Finally!");
    }
}