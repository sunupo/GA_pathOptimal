package pathOptimal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class ProcessData {
    private int[] orderIds;
    private double[] xCoords,yCoords,demands,readyTimes,dueTimes,serviceTimes;
    private double[][] distance=null;

    public ProcessData(int i,int dataSize) {
//        eg:datasize=100,

        orderIds=new int[dataSize];
        xCoords=new double[dataSize];
        yCoords=new double[dataSize];
        demands=new double[dataSize];
        readyTimes=new double[dataSize];
        dueTimes=new double[dataSize];
        serviceTimes=new double[dataSize];

        try{
            String fileName="C:"+File.separator+"Users"+
                    File.separator+"孙敬钦"+File.separator+"Desktop"+File.separator+"data"+i+".txt";
            System.out.println("filename="+fileName);
            File file=new File(fileName);
            BufferedReader in=new BufferedReader(new FileReader(file));
            String tempLine,s2=new String();
            int firstLine=1;
            int index=0;
            while((tempLine=in.readLine())!=null){
                if(firstLine==1) {
                    firstLine++;
                }
                String[] tempLineArray=tempLine.split("\\s+");

                try{
                    if(tempLineArray.length == 8 && index < dataSize){
                        orderIds[index]=Integer.parseInt(tempLineArray[1]);
                        xCoords[index]=Double.parseDouble(tempLineArray[2]);
                        yCoords[index]=Double.parseDouble(tempLineArray[3]);
                        demands[index]=Double.parseDouble(tempLineArray[4]);
                        readyTimes[index]=Double.parseDouble(tempLineArray[5]);
                        dueTimes[index]=Double.parseDouble(tempLineArray[6]);
                        serviceTimes[index]=Double.parseDouble(tempLineArray[7]);
                        index++;
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            System.out.println("last_index="+index);


        }catch(Exception e){
            e.printStackTrace();
        }


    }
//    public static  void main(String[] args){
//        ProcessData data = new ProcessData(1, 100);
//        System.out.println(data.toString());
//    }

    /**
     * 计算任意两点之间的距离，得到距离的二维数组
     * @param xCoords
     * @param yCoords
     */
    public double[][] getDistance(double[] xCoords,double[] yCoords){
        double[][] dis=new double[xCoords.length][yCoords.length];
        for (int i = 0; i < xCoords.length; i++) {
            for (int j = 0; j < yCoords.length; j++) {
                dis[i][j]=Math.sqrt((xCoords[i]-xCoords[j])*(xCoords[i]-xCoords[j])+(yCoords[i]-yCoords[j])*(yCoords[i]-yCoords[j]));
            }
        }
        this.distance=dis;
        return dis;
    }


    public int[] getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(int[] orderIds) {
        this.orderIds = orderIds;
    }

    public double[] getxCoords() {
        return xCoords;
    }

    public void setxCoords(double[] xCoords) {
        this.xCoords = xCoords;
    }

    public double[] getyCoords() {
        return yCoords;
    }

    public void setyCoords(double[] yCoords) {
        this.yCoords = yCoords;
    }

    public double[] getDemands() {
        return demands;
    }

    public void setDemands(double[] demands) {
        this.demands = demands;
    }

    public double[] getReadyTimes() {
        return readyTimes;
    }

    public void setReadyTimes(double[] readyTimes) {
        this.readyTimes = readyTimes;
    }

    public double[] getDueTimes() {
        return dueTimes;
    }

    public void setDueTimes(double[] dueTimes) {
        this.dueTimes = dueTimes;
    }

    public double[] getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(double[] serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    @Override
    public String toString() {
        return "ProcessData{" +
                "orderIds=" + Arrays.toString(orderIds) +
                ", \nxCoords=" + Arrays.toString(xCoords) +
                ", \nyCoords=" + Arrays.toString(yCoords) +
                ", \ndemands=" + Arrays.toString(demands) +
                ", \nreadyTimes=" + Arrays.toString(readyTimes) +
                ", \ndueTimes=" + Arrays.toString(dueTimes) +
                ", \nserviceTimes=" + Arrays.toString(serviceTimes) +
                '}';
    }
}
