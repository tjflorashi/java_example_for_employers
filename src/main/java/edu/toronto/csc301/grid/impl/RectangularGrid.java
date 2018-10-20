package edu.toronto.csc301.grid.impl;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import edu.toronto.csc301.grid.IGrid;
import edu.toronto.csc301.grid.GridCell;

public class RectangularGrid<T> implements IGrid<T>{
    private int width;
    private int height;
    private GridCell sw;
    private HashSet<GridCell> cells = new HashSet<>();
    private Map<String, T> cellsNames = new HashMap<>(); // string to cell


    public RectangularGrid(int width, int height,GridCell sw){
        if(sw==null){
            throw new NullPointerException();
        }
        if(width<=0 || height <= 0){
            throw new IllegalArgumentException();
        }

        this.width = width;
        this.height = height;
        this.sw = sw;
    }

    public T getItem(GridCell cell){
        if (cell.x > this.width + this.sw.x || cell.y > this.height + this.sw.y ){ // can't be out of border
            throw new IllegalArgumentException();
        }

        if (cell.x < this.sw.x || cell.y < this.sw.y){ // cell cannot be on the west/south of south west corner
            throw new IllegalArgumentException();
        }

        return this.cellsNames.get(cell.toString()); // retrieves from string

    }
    public Iterator<GridCell> getGridCells(){

        int x = this.sw.x;
        while(x < this.sw.x + this.width){
            int y = this.sw.y;
            while(y < this.sw.y + this.height){
                this.cells.add(GridCell.at(x, y));
                y = y + 1;
            }
            x = x + 1;
        }

        return this.cells.iterator();

    }
    public boolean hasCell(GridCell cell){

        int x = this.sw.x;
        while(x < this.sw.x + this.width){
            int y = this.sw.y;
            while(y < this.sw.y + this.height){
                if(x==cell.x && y==cell.y){
                    return true;
                }
                y = y + 1;
            }
            x = x + 1;
        }

        return false;


    }

    public void addCell(GridCell cell, T t){
        this.cellsNames.put(cell.toString(), t);
    }
}
