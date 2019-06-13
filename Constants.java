package pathOptimal;

public class Constants {

//    车初始的位置
    static final double SOURCE_X=40.00;
    static final double SOURCE_Y=50.00;

    static final double AVG_SPEED=1;//方便计算，取1

    static final int VEHICLE_NUM=200;
    static final double[] capacitys=new double[VEHICLE_NUM];
    static{
        for(int i=0;i<VEHICLE_NUM;i++){
//            capacitys[i]=(int)Math.random()*2000+5000;
            capacitys[i]=200;
        }
    }
    public static double calDis(double x1,double y1,double x2,double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
}
