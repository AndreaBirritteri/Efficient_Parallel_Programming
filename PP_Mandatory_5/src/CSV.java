import mycode.ConvexHull;
import mycode.parallel.Parallel;
import mycode.sequential.Sequential;
import precode.IntList;
import precode.NPunkter17;

public class CSV {
    public static void main(String[] args) {
        int totalThreads = Runtime.getRuntime().availableProcessors();
        int seed = 42;

        long startSequential, finishSequential, totalSequential;
        long startParallel, finishParallel, totalParallel;

        System.out.println("n;sequential;parallel;speedup");
        for (int n = 100; n <= 100000000; n *= 10) {
            int[] x = new int[n], y = new int[n];

            NPunkter17 nPunkter17 = new NPunkter17(n, seed);
            IntList intList = nPunkter17.lagIntList();
            nPunkter17.fyllArrayer(x, y);

            for (int times = 0; times < 7; times++) {
                startSequential = System.nanoTime();
                ConvexHull convexHullS = Sequential.findConvexHull(x, y, intList);
                finishSequential = System.nanoTime();

                startParallel = System.nanoTime();
                ConvexHull convexHullP = Parallel.findConvexHull(x, y, intList, totalThreads);
                finishParallel = System.nanoTime();

                totalSequential = finishSequential - startSequential;
                totalParallel = finishParallel - startParallel;

                System.out.print(n + ";");
                System.out.print(totalSequential + ";");
                System.out.print(totalParallel + ";");
                System.out.println((double) totalSequential / totalParallel);
            }
        }
    }
}