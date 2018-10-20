package edu.toronto.csc301.grid.impl;

import edu.toronto.csc301.grid.GridCell;
import edu.toronto.csc301.grid.IGrid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class FlexGrid<T> implements IGrid<T> {

    private ArrayList<GridCell> cells = new ArrayList<>();
    private HashMap<String, T> cellNames = new HashMap<>();

    public FlexGrid(){
    }

    public FlexGrid(ArrayList<GridCell> cells){
        this.cells = cells;

    }

    public T getItem(GridCell cell){
        if (!this.cells.contains(cell)){
            throw new IllegalArgumentException();
        }
        for(GridCell c: cells){
            if(cell.x==c.x && cell.y==c.y){
                return cellNames.get(cell.toString());
            }
        }

        return cellNames.get(cell.toString());
    }
    public Iterator<GridCell> getGridCells(){

        return this.cells.iterator();

    }

    public boolean hasCell(GridCell cell){

        for(GridCell c: cells){
            if(cell.x == c.x && cell.y == c.y){
                return true;
            }
        }
        return false;

    }

    public void addCell(GridCell cell, T t){

        this.cellNames.put(cell.toString(), t);
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append(this.cells.toString())
                .append(this.cellNames.toString())
                .toString();
    }
}
