package GUIGo;

/**
 * Created by Bartłomiej on 2016-12-04.
 */
public class DrawMathObject implements GUIMathClass{
    @Override
    public int calculateSizeOfCircle(int distance) {
        double k = (double) distance;
        double i = 1.0;
        while((i/k) < (2.0/3.0)) {
            System.out.println("i: "+i + "  distance: "+ distance);
            i+=1.0;
        }
        return (int) i;

    }

    @Override
    public int calculateDistance(int height, int boardSize) {

        return (int) Math.round((double)height/(double)boardSize);
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
        if(coordinates[0]<=0 )
            coordinates[0] = 0;
        if(coordinates[1]<=0)
            coordinates[1] = 0;
        int[] tempCor = new int[2];
        tempCor[0] = (int) Math.round((((double) coordinates[0]- (double) startPoint[0])/((double) distance)));
        //System.out.print("X: "+ tempCor[0]);
        tempCor[1] = (int) Math.round((((double) coordinates[1]- (double)startPoint[1])/((double) distance)));
        //System.out.print("    Y: "+ tempCor[1]);
        //System.out.println(" distance: "+distance);
        //System.out.println("realX: "+ cordinats[0] + "  realY: " + cordinats[1] + "  realSPX: "+ (cordinats[0]- startPoint[0])
        //+ " realSPY: "+ (cordinats[1]- startPoint[1]));
        return tempCor;
    }

    @Override
    public int[] calculateStartPoint(int height, int size, int distance){
        int[] X = new int[2];
        X[0] = (int) (height - ((size-1) * distance))/2;
        X[1] = (int) (height - ((size-1) * distance))/2;
        return X;
    }

    @Override
    public int calculateHeightandWidth(int width, int lenght) {

        return 0;
    }
}
