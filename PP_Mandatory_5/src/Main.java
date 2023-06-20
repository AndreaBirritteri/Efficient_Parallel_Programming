import mycode.ConvexHull;
import mycode.parallel.Parallel;
import mycode.sequential.Sequential;
import precode.IntList;
import precode.NPunkter17;
import precode.Oblig5Precode;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        int totalThreads = Runtime.getRuntime().availableProcessors();
        int seed = 42;
        int n;

        if (args.length == 1 && Objects.equals(args[0], "-h")) {
            System.out.println("use [n] (seed random number) (threadNumber)");
        } else if (args.length != 3 && args.length != 2 && args.length != 1) {
            System.out.println("Wrong or no arguments. Use -h to get help");
        } else {
            n = Integer.parseInt(args[0]);
            if(args.length >= 2)
                seed = Integer.parseInt(args[1]);
            if(args.length >= 3)
                totalThreads = Integer.parseInt(args[2]);
            System.out.printf("Using %d point, %d threads, %d as seed\n", n, totalThreads, seed);

            long startSequential, finishSequential, totalSequential;
            long startParallel, finishParallel, totalParallel;
            int[] x = new int[n], y = new int[n];

            System.out.println("Generating the points...");
            NPunkter17 nPunkter17 = new NPunkter17(n, seed);
            IntList intList = nPunkter17.lagIntList();
            nPunkter17.fyllArrayer(x, y);
            System.out.println("Points generated!");

            System.out.println("Calculating convex hull sequentially...");
            startSequential = System.nanoTime();
            ConvexHull convexHullS = Sequential.findConvexHull(x, y, intList);
            finishSequential = System.nanoTime();
            System.out.println("Sequential finished!");

            System.out.println("Calculating convex hull sequentially...");
            startParallel = System.nanoTime();
            ConvexHull convexHullP = Parallel.findConvexHull(x, y, intList, totalThreads);
            finishParallel = System.nanoTime();
            System.out.println("Parallel finished!");

            totalSequential = finishSequential - startSequential;
            totalParallel = finishParallel - startParallel;
            System.out.println("\nResults:");
            System.out.println("Sequential time -> " + totalSequential/1000000 + "ms");
            System.out.println("Parallel time -> " + totalParallel/1000000 + "ms");
            System.out.println("Speedup -> " + (double) totalSequential / totalParallel);

            if(n < 10000){
                Oblig5Precode oblig5Precode = new Oblig5Precode(convexHullP, convexHullP.list);
                oblig5Precode.drawGraph();
                oblig5Precode.writeHullPoints();
                System.out.println("Parallel points:");
                System.out.println(convexHullP);
            }
        }
    }
}
