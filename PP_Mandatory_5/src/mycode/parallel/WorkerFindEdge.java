package mycode.parallel;

import precode.IntList;
import utils.Utils;

public class WorkerFindEdge implements Runnable {

    private final IntList edgeIndexes;
    private final IntList possiblePoints;
    private final int p1, p2;
    private final int[] xPoints, yPoints;
    private final int maxLevel;

    WorkerFindEdge(IntList possiblePoints, int[] xPoints, int[] yPoints, int p1, int p2, int maxLevel) {
        this.edgeIndexes = new IntList();
        this.possiblePoints = possiblePoints;
        this.p1 = p1;
        this.p2 = p2;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.maxLevel = maxLevel;
    }

    public IntList getEdgeIndexes() {
        return edgeIndexes;
    }

    private void findEdge(IntList possiblePoints, int p1, int p2) {
        int[] line = Utils.lineEquation(this.xPoints, this.yPoints, p1, p2);
        int a = line[0];
        int b = line[1];
        int c = line[2];

        int mostLeftPoint = -1;
        int dist = 0;

        int index;
        int r, x, y;

        IntList onEdgePointsIndexes = new IntList();
        IntList leftSidePointsIndexes = new IntList();

        for (int i = 0; i < possiblePoints.size(); i++) {
            index = possiblePoints.get(i);

            x = this.xPoints[index];
            y = this.yPoints[index];

            r = a * x + b * y + c;

            if (r == 0 && index != p1 && index != p2)
                onEdgePointsIndexes.add(index);

            else if (r > 0) {
                leftSidePointsIndexes.add(index);
                if (r > dist) {
                    dist = r;
                    mostLeftPoint = index;
                }
            }
        }

        if (mostLeftPoint != -1) {
            findEdge(leftSidePointsIndexes, mostLeftPoint, p2);
            edgeIndexes.add(mostLeftPoint);
            findEdge(leftSidePointsIndexes, p1, mostLeftPoint);
        } else {
            Utils.sortLines(this.xPoints, this.yPoints, onEdgePointsIndexes, p2);
            edgeIndexes.append(onEdgePointsIndexes);
        }

    }

    @Override
    public void run() {
        int[] line = Utils.lineEquation(this.xPoints, this.yPoints, p1, p2);
        int a = line[0];
        int b = line[1];
        int c = line[2];

        int mostLeftPoint = -1;
        int dist = 0;

        int index;
        int r, x, y;

        IntList onEdgePointsIndexes = new IntList();
        IntList leftSidePointsIndexes = new IntList();

        for (int i = 0; i < possiblePoints.size(); i++) {
            index = possiblePoints.get(i);

            x = this.xPoints[index];
            y = this.yPoints[index];

            r = a * x + b * y + c;

            if (r == 0 && index != p1 && index != p2)
                onEdgePointsIndexes.add(index);

            else if (r > 0) {
                leftSidePointsIndexes.add(index);
                if (r > dist) {
                    dist = r;
                    mostLeftPoint = index;
                }
            }
        }

        if (mostLeftPoint != -1) {
            if (maxLevel >= 0) {
                WorkerFindEdge leftWorker = new WorkerFindEdge(leftSidePointsIndexes, this.xPoints, this.yPoints, mostLeftPoint, p2,
                        maxLevel - 1);
                Thread leftThread = new Thread(leftWorker);
                leftThread.start();
                WorkerFindEdge rightWorker = new WorkerFindEdge(leftSidePointsIndexes, this.xPoints, this.yPoints, p1, mostLeftPoint,
                        maxLevel - 1);
                Thread rightThread = new Thread(rightWorker);
                rightThread.start();
                try {
                    leftThread.join();
                    rightThread.join();
                    edgeIndexes.append(leftWorker.getEdgeIndexes());
                    edgeIndexes.add(mostLeftPoint);
                    edgeIndexes.append(rightWorker.getEdgeIndexes());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                findEdge(leftSidePointsIndexes, mostLeftPoint, p2);
                edgeIndexes.add(mostLeftPoint);
                findEdge(leftSidePointsIndexes, p1, mostLeftPoint);
            }
        } else {
            Utils.sortLines(this.xPoints, this.yPoints, onEdgePointsIndexes, p2);
            edgeIndexes.append(onEdgePointsIndexes);
        }
    }
}
