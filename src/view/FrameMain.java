package view;

import javax.swing.*;
import java.awt.*;


public class FrameMain extends JFrame {
    private JPanel jp1;
    private JMenu jm;
    private JMenuItem jmi1;
    private JMenuBar jmb;

    public FrameMain(){
        //初始化使用界面
        initFrameMain();

        //窗口信息设置 面板放置
        this.setTitle("模拟磁盘文件程序");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocation(500,200);
        this.setSize(1000,700);
        this.setVisible(true);

        //设置菜单和下拉菜单项目
        jm.add(jmi1);
        jmb.add(jm);

        this.add(jp1,BorderLayout.CENTER);
        this.add(jmb,BorderLayout.NORTH);

    }

    /**
    初始化界面项目
    **/
    public void initFrameMain(){
        jp1 = new JPanel();
        jm = new JMenu("文件");
        jmi1 = new JMenuItem("新建");
        jmb = new JMenuBar();
    }

}
