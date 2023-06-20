package mycode;

import precode.IntList;

public class ConvexHull {
    public int[] x, y;
    public int n;
    public int INDEX_MAX_X, INDEX_MAX_Y;
    public IntList list;

    public ConvexHull(int[] x, int[] y, IntList list){
        this.x = x;
        this.y = y;

        n = x.length;

        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        for(int i = 0; i < list.size(); i++){
            out.append(list.get(i));
            out.append(" -> ");
        }

        out.append(list.get(0));

        return out.toString();
    }
}
