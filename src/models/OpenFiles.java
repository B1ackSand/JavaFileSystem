package models;
/**
 * 模拟已打开文件表
 * 打开多个文件
 */

import java.util.ArrayList;
import java.util.List;

import models.OpenFile;
import util.FileSystemUtil;

public class OpenFiles {

    private List<OpenFile> files;
    private int length; //最多打开的数目

    public OpenFiles() {  //新建可打开的文件，最多为 FileSystemUtil.num
        files = new ArrayList<OpenFile>(FileSystemUtil.num);
        length = 0;
    }

    public void addFile(OpenFile openFile) { //add
        files.add(openFile);
    }

    public void removeFile(OpenFile openFile) { //remove
        files.remove(openFile);
    }

    public List<OpenFile> getFiles() {
        return files;
    }

    public void setFiles(List<OpenFile> files) {
        this.files = files;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
