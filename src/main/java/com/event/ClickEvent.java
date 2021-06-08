package com.event;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/18 18:43
 */
@Component
public class ClickEvent {

    private TextArea console = new TextArea();

    public EventHandler<? super Event> eventHandler(BorderPane borderPane) {
        Font font = new Font("Consolas", 15);
        console.setFont(font);
        console.setStyle("-fx-control-inner-background:black");
        return event -> {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                    String text = String.valueOf((char) b);
                    Platform.runLater(() -> {
                        console.appendText(text);
                    });
                }

                @Override
                public void write(byte[] b, int off, int len) {
                    String s = new String(b, off, len);
                    Platform.runLater(() -> console.appendText(s));
                }
            }, true));
            try {
                Process process = Runtime.getRuntime().exec("cmd");
                Charset charset = Charset.forName("gbk");
                new Thread(() -> {
                    try (InputStreamReader reader = new InputStreamReader(process.getInputStream(), charset)) {
                        int read;
                        while ((read = reader.read()) != -1) {
                            System.out.print((char) read);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            borderPane.setCenter(console);
        };
    }

}
