package mycode.sequential;

import mycode.ConvexHull;
import precode.IntList;
import utils.Utils;

public class Sequential {
    int[] xPoints, yPoints;

    int INDEX_MIN_X, INDEX_MAX_X;
    int INDEX_MAX_Y;

    IntList pointsIndexes;
    IntList edgeIndexes;

    ConvexHull graph;

    public Sequential(int[] xPoints, int[] yPoints, IntList pointsIndexes) {
        this.xPoints = xPoints;
        this.yPoints = yPoints;

        this.edgeIndexes = new IntList();
        this.graph = new ConvexHull(xPoints, yPoints, edgeIndexes);
        this.pointsIndexes = pointsIndexes;
    }

    public static ConvexHull findConvexHull(int[] x, int[] y, IntList pointsIndexes) {
        Sequential sequential = new Sequential(x, y, pointsIndexes);
        sequential.start();

        sequential.graph.INDEX_MAX_X = sequential.INDEX_MAX_X;
        sequential.graph.INDEX_MAX_Y = sequential.INDEX_MAX_Y;

        return sequential.graph;
    }


    private void start() {
        setMinMaxXY();
        edgeIndexes.add(INDEX_MAX_X);
        findEdge(pointsIndexes, INDEX_MIN_X, INDEX_MAX_X);
        edgeIndexes.add(INDEX_MIN_X);
        findEdge(pointsIndexes, INDEX_MAX_X, INDEX_MIN_X);
    }


    private void findEdge(IntList possiblePoints, int p1, int p2) {
        int[] line = Utils.lineEquation(xPoints, yPoints, p1, p2);
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

            x = xPoints[index];
            y = yPoints[index];

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
