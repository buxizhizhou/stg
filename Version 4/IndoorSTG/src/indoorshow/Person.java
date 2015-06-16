package indoorshow;

public class Person {

    private int id;
    private int firstid;
    private int secondid;
    private int type;
    private int inId;
    Location loc[];
    private boolean isArrive;

    public Person(int id, int firstid, int secondid, int type, int inId) {
        this.id = id;
        this.firstid = firstid;
        this.secondid = secondid;
        this.type = type;
        this.inId = inId;
        this.loc = new Location[3];
    }

    public int getInId() {
        return inId;
    }

    public void setInId(int inId) {
        this.inId = inId;
    }

    public int getFirstid() {
        return firstid;
    }

    public void setFirstid(int firstid) {
        this.firstid = firstid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsArrive() {
        return isArrive;
    }

    public void setIsArrive(boolean isArrive) {
        this.isArrive = isArrive;
    }

    public int getSecondid() {
        return secondid;
    }

    public void setSecondid(int secondid) {
        this.secondid = secondid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLocInfor(double priProb, double secProb) {//仅仅设置有目的性的对象的对应位置的信息
        if (type == 1) {//有目的性的对象
            loc[0] = new Location(0, firstid, priProb);//首要位置
            loc[1] = new Location(1, secondid, (1 - loc[0].getProb()) * secProb);//次要位置
            loc[2] = new Location(2, -1, (1 - loc[0].getProb()) * (1 - secProb));//其他房间位置
        }
    }
}

class Location {//每个人存储一个对应的位置信息

    int type;//0/1/2/分别代表首要位置，次要位置，其他位置
    int rid;
    double pro;

    public Location(int type, int rid, double p) {
        this.type = type;
        this.rid = rid;
        this.pro = p;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public void setProb(double p) {
        this.pro = p;
    }

    public int getType() {
        return type;
    }

    public int getRid() {
        return rid;
    }

    public double getProb() {
        return pro;
    }
}