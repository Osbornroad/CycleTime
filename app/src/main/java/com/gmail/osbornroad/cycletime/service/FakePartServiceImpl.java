package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.dao.PartDao;
import com.gmail.osbornroad.cycletime.model.Part;

import java.util.List;

/**
 * Created by User on 27.04.2017.
 */

public class FakePartServiceImpl implements PartService {

    private PartDao partDao;

    public FakePartServiceImpl(PartDao partDao) {
        this.partDao = partDao;
    }

    @Override
    public Part get(int id) {
        return partDao.get(id);
    }

    @Override
    public boolean save(Part part) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Part> getAll() {
        return partDao.getAll();
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printAll() {

    }

    @Override
    public Part[] getPartArray() {
        return getAll().toArray(new Part[getAll().size()]);
    }
}
