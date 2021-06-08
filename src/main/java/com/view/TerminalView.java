package com.view;

import com.event.ClickEvent;
import com.service.TreeService;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * @author qbanxiaoli
 * @description
 * @create 2020-06-26 12:48
 */
@FXMLView
public class TerminalView extends AbstractFxmlView {

    private BorderPane borderPane;

    public TerminalView(TreeService treeService, ClickEvent clickEvent) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0, 0, 0, 0));
        TreeView<String> treeView = treeService.getDictionaryTree();
        // 为treeView注册鼠标点击事件
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent.eventHandler(root));
        root.setLeft(treeView);
        borderPane = root;
    }

    @Override
    public Parent getView() {
        return borderPane;
    }

}