package com.esezak.client.UI.Elements.Panels;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;

import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class WatchlistPanel extends SimplePanel{
    class WatchilstTableModel extends AbstractTableModel {
        private String[] columnNames = {"Movie Name", "Date Added", "Rating","Status"};
        private ArrayList<Object[]> data = new ArrayList<>();
        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex)[columnIndex];

        }
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
        public void addRow(Object[] rowData) {
            data.add(rowData);
            //fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
        public void removeSelectedRow(int row) {
            if (row >= 0 && row < data.size()) {
                data.remove(row);
                fireTableRowsDeleted(row, row);
                fireTableDataChanged();
            }
        }



    }

    private JTable table;
    private SimpleButton saveButton;
    public WatchlistPanel(){
        super();
        int rowCount = 50;
        WatchilstTableModel tableModel = new WatchilstTableModel();
        Object[] rowData = {"Movie 1", "12.12.12", 10, "Watched"};
        for(int i = 0; i < rowCount; i++){
            tableModel.addRow(rowData);
        }
        table = new JTable(tableModel);
        table.setRowHeight(20);

        saveButton = new SimpleButton("Save Watchlist");
        // Initializing the JTable
        table.setBackground(Color.gray);
        table.setFont(new Font(GLOBAL_FONT.getFontName(), Font.PLAIN, 23));
        panel.add(table, BorderLayout.NORTH);
        panel.add(saveButton.getButton(), BorderLayout.SOUTH);
    }
}
