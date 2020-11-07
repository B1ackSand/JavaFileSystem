package util;
/**
 * 工具包
 * @author BlackSand
 */
public class FileSystemUtil {

    public static int num = 5;  //文件可以打开的个数
    public static String folderPath = "images/folder.jpg";
    public static String folder1Path = "images/folder1.jpg";
    public static String filePath = "images/file.jpg";
    public static String file1Path = "images/file1.jpg";
    public static String diskPath = "images/disk.jpg";
    public static String imgPath = "images/img1.jpg";

    public static int END = 255;  //结束符
    public static int DISK = 0;   //表示是硬盘
    public static int FOLDER = 1; //表示是文件夹
    public static int FILE = 2; //表示是文件

    public static int ERROR = -1; //错误

    public static int flagRead = 0; //只读
    public static int flagWrite = 1;//只写

    /*
     * 动态地根据JLabel来设置JPanel的height
     */
    public static int getHeight(int n) {
        int a = 0;
        a = n / 4;
        if (n % 4 > 0) {
            a++;
        }
        return a * 120;
    }

    /**
     * 每一次保存时都算出
     * 关于向文件中写内容的操作，计算出该文件占多长的长度
     * @return
     */
    public static int getNumOfFAT(int length) {
        if (length <= 64) {
            return 1;
        } else {
            int n = 0;
            if (length % 64 == 0) {
                n = length / 64;
            } else {
                n = length / 64;
                n++;
            }
            return n;
        }
    }
}
