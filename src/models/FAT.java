package models;

/**
 * 文件分配表对象
 * 磁盘中的每一块指针部分提取出来，组成文件分配表
 *
 * @author BlackSand
 */

public class FAT {

    private int index;
    //下标里面的内容，255结束，0空闲等等

    private int type;
    //类型 硬盘，文件，文件夹

    private Object object;
    //说明是目录还是文件

    public FAT(int index, int type, Object object) {
        super();
        this.index = index;
        this.type = type;
        this.object = object;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
