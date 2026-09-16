/*
 * Decompiled with CFR 0.152.
 */
package me.replydev.qubo.gui;

import javax.swing.table.DefaultTableModel;

class MyTableModel
extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public MyTableModel() {
        this.addColumn("N");
        this.addColumn("Ip");
        this.addColumn("Port");
        this.addColumn("Players");
        this.addColumn("Version");
        this.addColumn("MOTD");
    }
}

