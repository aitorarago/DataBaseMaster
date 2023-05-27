package com.example.projectofinal.Clases.Personalizacion;

import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

public class ItemList extends ListCell<String> {
    /**
     * Función que permite actualizar os items, en este caso los hace un poco mas grandes
     * @param item texto
     * @param empty para saber si un item esta vacio o no
     */
    @Override
    protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item);
                setFont(new Font(18)); // Establece el tamaño de fuente en 16
            }
    }
}

