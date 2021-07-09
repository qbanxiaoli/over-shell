package com.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/18 18:43
 */
@Component
public class ClickEvent {

    private TextArea console = new TextArea();

    public EventHandler<? super Event> eventHandler(BorderPane borderPane) {
        console.setFont(Font.getDefault());
        console.setStyle("-fx-control-inner-background:black");
        return event -> {
            // 输出信息到控制台
            borderPane.setCenter(console);
        };
    }

}
