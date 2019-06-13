package pathOptimal;

public class Order {
    private int orderId;
    private float xCoord;
    private float yCoord;
    private float demand;
    private float readyTime;
    private float dueTime;
    private float serviceTime;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public float getxCoord() {
        return xCoord;
    }

    public void setxCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }

    public float getDemand() {
        return demand;
    }

    public void setDemand(float demand) {
        this.demand = demand;
    }

    public float getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(float readyTime) {
        this.readyTime = readyTime;
    }

    public float getDueTime() {
        return dueTime;
    }

    public void setDueTime(float dueTime) {
        this.dueTime = dueTime;
    }

    public float getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(float serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                ", demand=" + demand +
                ", readyTime=" + readyTime +
                ", dueTime=" + dueTime +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
