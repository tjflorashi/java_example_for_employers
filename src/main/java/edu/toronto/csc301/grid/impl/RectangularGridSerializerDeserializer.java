package edu.toronto.csc301.grid.impl;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;
import edu.toronto.csc301.grid.IGridSerializerDeserializer;
import edu.toronto.csc301.warehouse.Rack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.function.Function;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class RectangularGridSerializerDeserializer implements IGridSerializerDeserializer{

    public <T> void serialize(IGrid<T> grid, OutputStream output,
                              Function<T, byte[]> item2byteArray) throws IOException{

        ArrayList<GridCell> cells = new ArrayList<>();
        Iterator<GridCell> itr = grid.getGridCells();
        HashMap<GridCell, T> cellNames = new HashMap<>();

        int max_width = -Integer.MAX_VALUE;
        int max_height = -Integer.MAX_VALUE;
        int corner_x = Integer.MAX_VALUE;
        int corner_y = Integer.MAX_VALUE;

        while (itr.hasNext()){
            GridCell cell = itr.next();

            cells.add(cell);

            if (cell.x > max_width){
                max_width = cell.x;
            }

            if (cell.y > max_height){
                max_height = cell.y;
            }

            if (cell.y < corner_y){
                corner_y = cell.y;
            }

            if (cell.x < corner_x){
                corner_x = cell.x;
            }
            if (grid.getItem(cell) != null){
                cellNames.put(cell, grid.getItem(cell));
                if(!cells.contains(cell)){
                    throw new IllegalArgumentException();
                }
            }
        }

        GridCell corner = GridCell.at(corner_x, corner_y); // dummy copy

        for (int x = corner.x; x < corner.x + max_width; x++){ // are you rectangle
            for (int y = corner.y; y < corner.y + max_height; y++){
                if (!cells.contains(GridCell.at(x, y))){
                    throw new IllegalArgumentException();
                }
            }
        }

        int actual_width = max_width - corner.x + 1;
        int actual_height = max_height - corner.y + 1;


        StringBuilder s = new StringBuilder()
                .append(String.format("width: %d\n", actual_width))
                .append(String.format("height: %d\n", actual_height))
                .append(String.format("south-west: %d:%d\n", corner.x, corner.y));

        for (GridCell cell : cellNames.keySet()) {
            s.append(cell.x + ":" + cell.y + " " + "R:" + ((Rack) cellNames.get(cell)).getCapacity());
            s.append("\n");
        }

        output.write(s.toString().getBytes());
        output.close();
    }




    public <T> IGrid<T> deserialize(InputStream input,
                                    Function<byte[],T> byteArray2item) throws IOException{

        BufferedReader reader =  new BufferedReader(new InputStreamReader(input));

        int width = 0;
        int height = 0;
        if(reader.ready()){
            width = Integer.parseInt(reader.readLine().replaceAll("[^0-9]", ""));

            height = Integer.parseInt(reader.readLine().replaceAll("[^0-9]", "")); // all that are not numbers
        }

        String[] corner_coords = reader.readLine().split(" ")[1].split(":");

        GridCell corner = GridCell.at(Integer.parseInt(corner_coords[0]),
                Integer.parseInt(corner_coords[1]));
        RectangularGrid<T> grid = new RectangularGrid<T>(width, height, corner);

        String line;

        while ((line = reader.readLine()) != null){
            String[] grid_coords = line.split(" ")[0].split(":");
            grid.addCell(GridCell.at(Integer.parseInt(grid_coords[0])
                    , Integer.parseInt(grid_coords[1])), byteArray2item.apply(line.split(" ")[1].getBytes()));
        }

        input.close();
        reader.close();
        return grid;
    }


}
