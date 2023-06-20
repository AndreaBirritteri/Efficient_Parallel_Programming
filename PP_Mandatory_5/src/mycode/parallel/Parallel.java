package mycode.parallel;

import mycode.ConvexHull;
import precode.IntList;

public class Parallel {
    int[] xPoints, yPoints;

    int threadsNumber;
    int maxLevel;

    int INDEX_MIN_X, INDEX_MAX_X;
    int INDEX_MAX_Y;

    IntList pointsIndexes;
    IntList edgeIndexes;

    ConvexHull graph;

    public Parallel(int[] xPoints, int[] yPoints, IntList pointsIndexes, int threadsNumber) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;

        this.threadsNumber = threadsNumber;
        this.maxLevel = (int) (Math.log(threadsNumber) / Math.log(2));

        this.edgeIndexes = new IntList();
        this.graph = new ConvexHull(xPoints, yPoints, edgeIndexes);
        this.pointsIndexes = pointsIndexes;
    }

    public static ConvexHull findConvexHull(int[] x, int[] y, IntList pointsIndexes, int threadsNumber) {
        Parallel parallel = new Parallel(x, y, pointsIndexes, threadsNumber);
        parallel.start();

        parallel.graph.INDEX_MAX_X = parallel.INDEX_MAX_X;
        parallel.graph.INDEX_MAX_Y = parallel.INDEX_MAX_Y;

        return parallel.graph;
    }

    private void start() {
        setMinMaxXY();
        WorkerFindEdge leftWorker = new WorkerFindEdge(pointsIndexes, xPoints, yPoints, INDEX_MIN_X, INDEX_MAX_X,
                maxLevel - 1);
        Thread leftThread = new Thread(leftWorker);
        leftThread.start();
        WorkerFindEdge rightWorker = new WorkerFindEdge(pointsIndexes, xPoints, yPoints, INDEX_MAX_X, INDEX_MIN_X,
                maxLevel - 1);
        Thread rightThread = new Thread(rightWorker);
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
            edgeIndexes.add(INDEX_MAX_X);
            edgeIndexes.append(leftWorker.getEdgeIndexes());
            edgeIndexes.add(INDEX_MIN_X);
            edgeIndexes.append(rightWorker.getEdgeIndexes());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void setMinMaxXY() {
        INDEX_MIN_X = xPoints[0];
        INDEX_MAX_X = xPoints[0];

        for (int i = 0; i < xPoints.length; i++) {
            if (xPoints[INDEX_MIN_X] > xPoints[i])
                INDEX_MIN_X = i;
            else if (xPoints[INDEX_MAX_X] < xPoints[i])
                INDEX_MAX_X = i;
        }

        INDEX_MAX_Y = yPoints[0];
        for (int i = 0; i < yPoints.length; i++) {
            if (i > INDEX_MAX_Y)
                INDEX_MAX_Y = i;
        }
    }
}
