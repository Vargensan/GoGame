package GUIGo;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class DrawMathObject implements GUIMathClass{
    protected static final double SCALE = 4.0/5.0;
    int height;
    int startpointX;
    int startpointY;

    @Override
    public int calculateSizeOfCircle(int distance) {
        double k = (double) distance;
        double i = 1.0;
        while((i/k) < (SCALE)) {
            i+=1.0;
        }
        return (int) i;

    }

    @Override
    public int calculateDistance(int height, int boardSize) {

        return (((int) Math.round((double)height/(double)boardSize)) * 11/12);
    }

    @Override
    public int[][][] calculateTableIntersection(int[] startPoint, int distance, int boardSize,int criclesize) {
        int tempTempIntersection[][][] = new int[boardSize][boardSize][2];
        for(int i =0 ; i< boardSize ; i++){
            for(int j = 0 ; j < boardSize ; j++){
                tempTempIntersection[i][j][0]  = startPoint[0] + distance*i -(criclesize/2);
                tempTempIntersection[i][j][1]  = startPoint[1] + distance*j -(criclesize/2);
            }
        }
        return tempTempIntersection;
    }

    @Override
    public int[] calculateIntersection(int[] coordinates, int size, int[] startPoint, int distance) {
        boolean nope=false;
        if(coordinates[0]<=startPoint[0] - distance/2 || coordinates[0] >= height - startPoint[0]+distance/2)
            nope = true;
        if(coordinates[1]<=startPoint[1] - distance/2 || coordinates[1] >= height - startPoint[1]+distance/2)
            nope = true;
        int[] tempCor = new int[2];
        if(nope == false){
            tempCor[0] = (int) Math.round((((double) coordinates[0]- (double) startPoint[0])/((double) distance)));
            tempCor[1] = (int) Math.round((((double) coordinates[1]- (double) startPoint[1])/((double) distance)));
        } else {
            tempCor[0] = -1;
            tempCor[1] = -1;
        }
        return tempCor;
    }

    @Override
    public int[] calculateStartPoint(int height, int size, int distance){
        this.height = height;
        int[] X = new int[2];
        X[0] = (int) (height - ((size-1) * distance))/2;
        X[1] = (int) (height - ((size-1) * distance))/2;
        this.startpointX = X[0];
        this.startpointY = X[1];
        return X;
    }

    @Override
    public int calculateHeightandWidth(int width, int lenght) {

        return 0;
    }
}
