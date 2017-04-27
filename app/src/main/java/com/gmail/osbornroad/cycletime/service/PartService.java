package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.model.Part;

import java.util.List;

/**
 * Created by User on 27.04.2017.
 */

public interface PartService {
    Part get(int id);

    boolean save(Part part);

    List<Part> getAll();

    void delete(int id);

    void printAll();

    Part[] getPartArray();
}
