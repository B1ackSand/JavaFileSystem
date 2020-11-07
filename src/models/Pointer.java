package models;
/**
 * 已打开文件表中，读、写指针结构
 */

import util.FileSystemUtil;

public class Pointer {

    private int dnum; //磁盘盘块号
    private int bnum; //磁盘盘块第几个字节

    public int getDnum() {
        return dnum;
    }

    public void setDnum(int dnum) {
        this.dnum = dnum;
    }

    public int getBnum() {
        return bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

}
