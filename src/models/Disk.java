package models;

/**
 * 磁盘对象
 *
 * @author BlackSand
 */
public class Disk {


    private String diskName;
    //磁盘名

    public Disk(String diskName) {
        super();
        this.diskName = diskName;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    @Override
    public String toString() {
        return diskName;
    }

}
