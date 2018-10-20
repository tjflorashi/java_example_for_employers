package edu.toronto.csc301.grid.impl;


import edu.toronto.csc301.grid.IGrid;
import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGridSerializerDeserializer;

import java.io.*;
import java.util.*;
import java.util.function.Function;

public class FlexGridSerializerDeserializer implements IGridSerializerDeserializer {
    @Override
    public <T> IGrid<T> deserialize(InputStream input,
                                    Function<byte[], T> byteArray2item) throws IOException {

        ArrayList<GridCell> cells = new ArrayList<>();
        HashMap<GridCell, T> cellNames = new HashMap<>();

        BufferedReader reader =  new BufferedReader(new InputStreamReader(input));

        String line;

        while ((line = reader.readLine()) != null){
            String[] coords = line.split(" ")[0].split(":");//-30:1
            GridCell cell = GridCell.at(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]));
            cells.add(cell);

            if (line.split(" ").length > 1){
                cellNames.put(cell, byteArray2item.apply(line.split(" ")[1].getBytes())); //0:0 R:3
            }
        }

        FlexGrid<T> grid =  new FlexGrid<>(cells);

        for (GridCell cell: cellNames.keySet()){
            grid.addCell(cell, cellNames.get(cell));
        }

        input.close();
        reader.close();

        return grid;

    }

    @Override
    public <T> void serialize(IGrid<T> grid,
                              OutputStream output,
                              Function<T, byte[]> item2byteArray)
            throws IOException {

        Iterator<GridCell> itr = grid.getGridCells();

        String cell_string = "";
        while (itr.hasNext()){
            GridCell cell = itr.next();
            cell_string = cell.x + ":" + cell.y;
            if (grid.getItem(cell) != null){
                cell_string += " " + new String(item2byteArray.apply(grid.getItem(cell))) + "\n";
            } else {
                cell_string += "\n";
            }

            output.write(cell_string.getBytes());
        }
        output.close();
    }
}
