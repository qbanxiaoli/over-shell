package com.window;

import de.felixroske.jfxsupport.GUIState;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * @author Q版小李
 * @description 系统托盘
 * @create 2021/3/10 15:20
 */
public class MinWindow {

    // 最小化到托盘
    @SneakyThrows
    public static void minimizeToTray(Stage stage) {
        // 执行stage.close()方法,窗口不直接退出
        Platform.setImplicitExit(false);
        // 菜单项(打开)中文乱码的问题是编译器的锅,如果使用IDEA,需要在Run-Edit Configuration在LoginApplication中的VM Options中添加-Dfile.encoding=GBK
        // 如果使用Eclipse,需要右键Run as-选择Run Configuration,在第二栏Arguments选项中的VM Options中添加-Dfile.encoding=GBK
        // 菜单项(打开主界面)
        MenuItem showItem = new MenuItem("打开主界面");
        // 菜单项(退出)
        MenuItem exitItem = new MenuItem("退出");
        // 此处不能选择ico格式的图片,要使用其它格式的图片
        URL url = MinWindow.class.getResource("/OverShell.jpg");
        // 系统托盘图标
        TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(url));
        // 设置图标尺寸自动适应
        trayIcon.setImageAutoSize(true);
        // 弹出式菜单组件
        PopupMenu popup = new PopupMenu();
        popup.add(showItem);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);
        // 鼠标移到系统托盘,会显示提示文本
        trayIcon.setToolTip("OverShell");
        // 添加到系统托盘
        if (GUIState.getSystemTray().getTrayIcons().length == 0) {
            GUIState.getSystemTray().add(trayIcon);
        }
        // 添加点击事件监听
        MinWindow.listen(stage, showItem, exitItem, trayIcon);
    }

    // 添加事件监听
    private static void listen(Stage stage, MenuItem showItem, MenuItem exitItem, TrayIcon trayIcon) {
        // 行为事件: 点击"打开主界面"按钮，显示窗口
        ActionListener showListener = e -> Platform.runLater(() -> MinWindow.showStage(stage));
        // 行为事件: 点击"退出"按钮，就退出系统
        ActionListener exitListener = e -> System.exit(0);
        // 鼠标行为事件: 单击显示stage
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // 点击系统托盘，显示界面
                    MinWindow.showStage(stage);
                }
            }
        };
        // 给菜单项添加事件
        showItem.addActionListener(showListener);
        exitItem.addActionListener(exitListener);
        // 给系统托盘添加鼠标响应事件
        trayIcon.addMouseListener(mouseListener);
    }

    // 点击系统托盘,显示界面
    private static void showStage(Stage stage) {
        Platform.runLater(() -> {
            if (stage.isIconified()) {
                // 将最小化的状态设为false
                stage.setIconified(false);
            }
            if (!stage.isShowing()) {
                stage.show();
            }
            // 显示在最前面
            stage.toFront();
        });
    }

}
