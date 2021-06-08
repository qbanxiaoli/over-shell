package com;

import com.splash.ShellSplash;
import com.view.TerminalView;
import com.window.MinWindow;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@SpringBootApplication
public class OverShellApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(OverShellApplication.class, TerminalView.class, new ShellSplash(), args);
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        // 设置标题信息
        stage.setTitle("OverShell");
        // 允许拉动界面大小
        stage.setResizable(true);
        // 设置宽度
        stage.setWidth(1000);
        // 设置高度
        stage.setHeight(800);
    }

    @Override
    public Collection<Image> loadDefaultIcons() {
        // 设置左上角小图标
        return Collections.singletonList(new Image(this.getClass().getResource("/OverShell.jpg").toExternalForm()));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(event -> {
            if (stage.isIconified()) {
                stage.setIconified(false);
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if (SystemTray.isSupported()) {
                alert.getButtonTypes().add(new ButtonType("最小化到托盘"));
            }
            // 设置对话框的标题
            alert.setTitle("OverShell—退出");
            // 设置对话框的icon图标
            alert.initOwner(stage);
            alert.setHeaderText("是否确认退出OverShell？");
            Optional result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.CANCEL)) {
                event.consume();
            } else if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                System.exit(0);
            } else {
                // 最小化到托盘
                MinWindow.minimizeToTray(stage);
            }
        });
        super.start(stage);
    }

}
