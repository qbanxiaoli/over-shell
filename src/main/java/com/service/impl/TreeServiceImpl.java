package com.service.impl;

import com.service.TreeService;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Service;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/18 18:28
 */
@Service
public class TreeServiceImpl implements TreeService {

    @Override
    public TreeView<String> getDictionaryTree() {
        Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/folder.png")));
        TreeItem<String> rootItem = new TreeItem<>("服务器地址", rootIcon);
        rootItem.setExpanded(true);
        for (int i = 1; i < 2; i++) {
            TreeItem<String> item = new TreeItem<>("47.245.11.204");
            rootItem.getChildren().add(item);
        }
        return new TreeView<>(rootItem);
    }

}
