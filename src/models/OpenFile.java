package models;
/**
 * 模拟已打开文件表
 * 打开单个文件
 */

import java.util.List;

public class OpenFile {

    private int flag;// 操作类型 0表示以读操作方式打开文件，1表示以写操作方式打开文件
    private models.Pointer read;
    private models.Pointer write;
    private File file;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public models.Pointer getRead() {
        return read;
    }

    public void setRead(models.Pointer read) {
        this.read = read;
    }

    public models.Pointer getWrite() {
        return write;
    }

    public void setWrite(models.Pointer write) {
        this.write = write;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
