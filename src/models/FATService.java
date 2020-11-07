package models;

/**
 * 文件分配表服务
 */

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import models.Disk;
import models.FAT;
import models.File;
import models.Folder;
import models.OpenFile;
import models.OpenFiles;
import util.FileSystemUtil;
import util.MessageUtil;

public class FATService {

    private static FAT[] myFAT;
    //文件分配表数组

    private static OpenFiles openFiles;
    //已打开文件表

    public FATService() {
        openFiles = new OpenFiles();
    }

    public void addOpenFile(FAT fat, int flag) {  //添加已打开文件
        OpenFile openFile = new OpenFile();
        openFile.setFile((File) fat.getObject());
        openFile.setFlag(flag);
        openFiles.addFile(openFile);
    }

    public void removeOpenFile(FAT fat) { //删除已打开文件
        for (int i = 0; i < openFiles.getFiles().size(); i++) {
            if (openFiles.getFiles().get(i).getFile() == (File) fat.getObject()) {
                openFiles.getFiles().remove(i);
            }
        }
    }

    public boolean checkOpenFile(FAT fat) { //检查已打开的文件
        for (int i = 0; i < openFiles.getFiles().size(); i++) {
            if (openFiles.getFiles().get(i).getFile() == (File) fat.getObject()) {
                return true;
            }
        }
        return false;
    }

    public void initFAT() {
        //新建文件分配表
        myFAT = new FAT[128];

        //磁盘第0块表示被系统占用
        myFAT[0] = new FAT(FileSystemUtil.END, FileSystemUtil.DISK, null);

        //myFAT[1]表示为硬盘名 不确定是否为根目录
        myFAT[1] = new FAT(FileSystemUtil.END, FileSystemUtil.DISK, new Disk("C"));

    }

    /**
     * 文件夹创建
     */
    public int createFolder(String path) {
        String folderName = null;
        boolean canName = true;
        int index = 1;

        //得到新建文件夹名字
        do {
            folderName = "新建文件夹";
            canName = true;

            //新建文件夹index 新建文件夹1,2,3
            folderName += index;
            //从0开始搜索
            for (int i = 0; i < myFAT.length; i++) {
                //若文件分配表不为空
                if (myFAT[i] != null) {
                    //若文件分配表的类型为文件夹
                    if (myFAT[i].getType() == FileSystemUtil.FOLDER) {
                        Folder folder = (Folder) myFAT[i].getObject();
                        //????
                        if (path.equals(folder.getLocation())) {
                            //如果存在同名目录
                            if (folderName.equals(folder.getFolderName())) {
                                canName = false;
                            }
                        }
                    }
                }
            }
            index++;
        } while (!canName);
        //在myFAT中添加文件夹
        int index2 = searchEmptyFromMyFAT();
        //找到第一个为空的fat下标 index2为该文件夹的起始盘块号

        //如果找不到
        if (index2 == FileSystemUtil.ERROR) {
            //返回错误提示
            return FileSystemUtil.ERROR;
        } else {
            //新建文件夹
            Folder folder = new Folder(folderName, path, index2);
            //255，文件夹，folder对象
            myFAT[index2] = new FAT(FileSystemUtil.END, FileSystemUtil.FOLDER, folder);
        }
        return index2;
    }

    //创建文件
    public int createFile(String path) {
        String fileName = null;
        boolean canName = true;
        int index = 1;
        //得到新建文件名字
        do {
            fileName = "新建文件";
            canName = true;
            fileName += index;
            for (int i = 0; i < myFAT.length; i++) {
                if (myFAT[i] != null) {
                    if (myFAT[i].getType() == FileSystemUtil.FILE) {
                        File file = (File) myFAT[i].getObject();
                        if (path.equals(file.getLocation())) {
                            if (fileName.equals(file.getFileName())) {
                                canName = false;
                            }
                        }
                    }
                }
            }
            index++;
        } while (!canName);
        //在myFAT中添加文件
        int index2 = searchEmptyFromMyFAT();
        if (index2 == FileSystemUtil.ERROR) {
            return FileSystemUtil.ERROR;
        } else {
            File file = new File(fileName, path, index2);
            myFAT[index2] = new FAT(FileSystemUtil.END, FileSystemUtil.FILE, file);
        }
        return index2;
    }

    //得到myFAT中第一个为空的磁盘块索引
    public int searchEmptyFromMyFAT() {
        for (int i = 2; i < myFAT.length; i++) { //从第二块磁盘开始搜索
            if (myFAT[i] == null) {
                return i;
            }
        }
        return FileSystemUtil.ERROR;
    }

    //得到磁盘块的使用  检测此时磁盘已经用了多少块
    public int getNumOfFAT() {
        int n = 0;
        for (int i = 2; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                n++;
            }
        }
        return n;
    }

    //得到空的磁盘块数量
    public int getSpaceOfFAT() {
        int n = 0;
        for (int i = 2; i < myFAT.length; i++) {
            if (myFAT[i] == null) {
                n++;
            }
        }
        return n;
    }

    //保存数据时重新分配磁盘
    public void saveToModifyFATS(int num, FAT fat) {
        //得到起始磁盘号
        int begin = ((File) fat.getObject()).getDiskNum();
        //得到myFAT中第一个为空的磁盘块索引
        int n = this.searchEmptyFromMyFAT();
        myFAT[begin].setIndex(n);
        for (int i = 1; i < num; i++) {
            n = this.searchEmptyFromMyFAT();
            if (i == num - 1) {
                myFAT[n] = new FAT(255, FileSystemUtil.FILE, (File) fat.getObject());
            } else {
                myFAT[n] = new FAT(20, FileSystemUtil.FILE, (File) fat.getObject());
                int n2 = this.searchEmptyFromMyFAT();
                myFAT[n].setIndex(n2);
            }
        }
    }

    //添加数据时，磁盘需分配
    public boolean saveToModifyFATS2(Component parent, int num, FAT fat) {
        //从哪片磁盘开始
        //找到起始盘块号
        int begin = ((File) fat.getObject()).getDiskNum();
        //获得下一个盘块号的位置
        int index = myFAT[begin].getIndex();
        //统计旧的文件所占用的盘块数量
        int oldNum = 1;
        //当下标不为255，说明还未结束
        while (index != FileSystemUtil.END) {
            oldNum++;
            //如果下一个磁盘为255，说明已经结束
            if (myFAT[index].getIndex() == FileSystemUtil.END) {
                begin = index;
            }
            index = myFAT[index].getIndex();
        }

        //
        if (num > oldNum) {
            //需要添加磁盘块
            int n = num - oldNum;
            if (this.getSpaceOfFAT() < n) {
                MessageUtil.showErrorMgs(parent, "保存的内容已经超过磁盘的容量");
                return false;
            }
            //得到myFAT中第一个为空的磁盘块索引
            int space = this.searchEmptyFromMyFAT();
            myFAT[begin].setIndex(space);
            for (int i = 1; i <= n; i++) {
                //得到myFAT中第一个为空的磁盘块索引
                space = this.searchEmptyFromMyFAT();
                if (i == n) {
                    myFAT[space] = new FAT(255, FileSystemUtil.FILE, (File) fat.getObject());
                } else {
                    //暂时存放20号fat
                    myFAT[space] = new FAT(20, FileSystemUtil.FILE, (File) fat.getObject());
                    int space2 = this.searchEmptyFromMyFAT();
                    myFAT[space].setIndex(space2);
                }
            }
            return true;
        } else {
            //不需要添加磁盘块
            return true;
            //return false;
        }
    }

    //删除数据时，磁盘的分配
    public boolean saveToModifyFATS3(Component parent, int num, FAT fat) {
        //从哪片磁盘开始
        //找到起始盘块号
        int begin = ((File) fat.getObject()).getDiskNum();
        //获得下一个盘块号的位置
        int index = myFAT[begin].getIndex();
        if (index != FileSystemUtil.END) {
            for (int i = 1; i < num - 1; i++) {
                index = myFAT[index].getIndex();
            }
        }
        //找到起始盘块号
        int begin1 = ((File) fat.getObject()).getDiskNum();
        //获得下一个盘块号的位置
        int index3 = myFAT[begin1].getIndex();
        //统计旧的文件所占用的盘块数量
        int oldNum = 1;
        //当下标不为255，说明还未结束
        while (index3 != FileSystemUtil.END) {
            oldNum++;
            index3 = myFAT[index3].getIndex();
        }
        if (oldNum > 1) {
            int index1 = myFAT[index].getIndex();
            int index2;
            while (index1 != FileSystemUtil.END) {
                index2 = index1;
                index1 = myFAT[index1].getIndex();
                myFAT[index2] = null;
            }
        }
        if (num > 1) {
            myFAT[index] = new FAT(255, FileSystemUtil.FILE, (File) fat.getObject());
        } else {
            if (oldNum > 1) {
                myFAT[index] = null;
                myFAT[begin] = new FAT(255, FileSystemUtil.FILE, (File) fat.getObject());
            } else {
                myFAT[begin] = new FAT(255, FileSystemUtil.FILE, (File) fat.getObject());
            }
        }
        return true;
    }

    //遍历folder
    public List<Folder> getFolders(String path) {
        List<Folder> list = new ArrayList<Folder>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof Folder) {
                    if (((Folder) (myFAT[i].getObject())).getLocation().equals(path)) {
                        list.add((Folder) myFAT[i].getObject());
                    }
                }
            }
        }
        return list;
    }

    //遍历文件
    public List<File> getFiles(String path) {
        List<File> list = new ArrayList<File>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof File) {
                    if (((File) (myFAT[i].getObject())).getLocation().equals(path)) {
                        list.add((File) myFAT[i].getObject());
                    }
                }
            }
        }
        return list;
    }

    //遍历该路径下的文件夹和文件
    public List<FAT> getFATs(String path) {
        List<FAT> fats = new ArrayList<FAT>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof Folder) {
                    if (((Folder) (myFAT[i].getObject())).getLocation().equals(path)) {
                        fats.add(myFAT[i]);
                    }
                }
            }
        }
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof File) {
                    if (((File) (myFAT[i].getObject())).getLocation().equals(path)) {
                        fats.add(myFAT[i]);
                    }
                }
            }
        }
        return fats;

    }

    public List<FAT> getFolders1(String path) {
        List<FAT> list = new ArrayList<FAT>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof Folder) {
                    if (((Folder) (myFAT[i].getObject())).getLocation().equals(path)) {
                        list.add(myFAT[i]);
                    }
                }
            }
        }
        return list;
    }

    //查找某个特定的文件夹
    public List<FAT> getFolders1(String path, String name) {
        List<FAT> list = new ArrayList<FAT>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof Folder) {
                    if (((Folder) (myFAT[i].getObject())).getLocation().equals(path)) {
                        if ((((Folder) (myFAT[i].getObject())).getFolderName().equals(name))) {
                            list.add(myFAT[i]);
                        }
                    }
                }
            }
        }
        return list;
    }


    public List<FAT> getFiles1(String path) {
        List<FAT> list = new ArrayList<FAT>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof File) {
                    if (((File) (myFAT[i].getObject())).getLocation().equals(path)) {
                        list.add(myFAT[i]);
                    }
                }
            }
        }
        return list;
    }

    //查找某个特定的文件
    public List<FAT> getFiles1(String path, String name) {
        List<FAT> list = new ArrayList<FAT>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof File) {
                    if (((File) (myFAT[i].getObject())).getLocation().equals(path)) {
                        if ((((File) (myFAT[i].getObject())).getFileName().equals(name))) {
                            list.add(myFAT[i]);
                        }

                    }
                }
            }
        }
        return list;
    }

    //取得具有相同路径的文件夹表和文件列表
    public List<FAT> getFATs1(String path) {
        List<FAT> fats = new ArrayList<FAT>();
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof Folder) {
                    if (((Folder) (myFAT[i].getObject())).getLocation().equals(path)) {
                        fats.add(myFAT[i]);
                    }
                }
            }
        }
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getObject() instanceof File) {
                    if (((File) (myFAT[i].getObject())).getLocation().equals(path)) {
                        fats.add(myFAT[i]);
                    }
                }
            }
        }
        return fats;

    }

    //修改路径
    public void modifyLocation(String oldPath, String newPath) {
        for (int i = 0; i < myFAT.length; i++) {
            if (myFAT[i] != null) {
                if (myFAT[i].getType() == FileSystemUtil.FILE) {
                    if (((File) myFAT[i].getObject()).getLocation().contains(oldPath)) {
                        ((File) myFAT[i].getObject()).setLocation(((File) myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
                    }
                } else if (myFAT[i].getType() == FileSystemUtil.FOLDER) {
                    if (((Folder) myFAT[i].getObject()).getLocation().contains(oldPath)) {
                        ((Folder) myFAT[i].getObject()).setLocation(((Folder) myFAT[i].getObject()).getLocation().replace(oldPath, newPath));
                    }
                }
            }
        }

    }

    /**
     * 删除
     *
     * @param jp1
     * @param fat
     * @param map
     */
    public void delete(JPanel jp1, FAT fat, Map<String, DefaultMutableTreeNode> map) {
        if (fat.getType() == FileSystemUtil.FILE) {
            //---------------->文件
            //判断是否文件正在打开，如果打开则不能删除
            for (int i = 0; i < openFiles.getFiles().size(); i++) {
                if (openFiles.getFiles().get(i).getFile().equals(fat.getObject())) {
                    MessageUtil.showErrorMgs(jp1, "文件正打开着，不能删除");
                    return;
                }
            }

            for (int i = 0; i < myFAT.length; i++) {
                if (myFAT[i] != null && myFAT[i].getType() == FileSystemUtil.FILE) {
                    if (((File) myFAT[i].getObject()).equals((File) fat.getObject())) {
                        myFAT[i] = null;
                        System.out.println("----------------------->删除");
                    }
                }
            }

        } else {
            //---------------->文件夹
            String path = ((Folder) fat.getObject()).getLocation();
            String folderPath = ((Folder) fat.getObject()).getLocation() + "\\" + ((Folder) fat.getObject()).getFolderName();
            System.out.println("路径：" + folderPath);
            int index = 0;
            for (int i = 2; i < myFAT.length; i++) {
                if (myFAT[i] != null) {
                    Object obj = myFAT[i].getObject();
                    if (myFAT[i].getType() == FileSystemUtil.FOLDER) {
                        if (((Folder) obj).getLocation().equals(folderPath)) {
                            MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
                            return;
                        }
                    } else {
                        if (((File) obj).getLocation().equals(folderPath)) {
                            MessageUtil.showErrorMgs(jp1, "文件夹不为空，不能删除");
                            return;
                        }
                    }
                    if (myFAT[i].getType() == FileSystemUtil.FOLDER) {
                        if (((Folder) myFAT[i].getObject()).equals((Folder) fat.getObject())) {
                            index = i;
                        }
                    }
                }
            }

            myFAT[index] = null;
            DefaultMutableTreeNode parentNode = map.get(path);
            parentNode.remove(map.get(folderPath));
            map.remove(folderPath);
        }
    }

    public static FAT[] getMyFAT() {
        return myFAT;
    }

    public static void setMyFAT(FAT[] myFAT) {
        FATService.myFAT = myFAT;
    }

    public static FAT getFAT(int index) {
        return myFAT[index];
    }

    public static OpenFiles getOpenFiles() {
        return openFiles;
    }

    public static void setOpenFiles(OpenFiles openFiles) {
        FATService.openFiles = openFiles;
    }

}
