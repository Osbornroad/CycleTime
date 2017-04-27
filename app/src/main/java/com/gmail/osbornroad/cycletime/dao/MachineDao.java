package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Machine;

import java.util.List;

/**
 * Created by User on 27.04.2017.
 */

public interface MachineDao {
    Machine get(int id);

    boolean save(Machine machine);

    List<Machine> getAll();

    void delete(int id);

}
