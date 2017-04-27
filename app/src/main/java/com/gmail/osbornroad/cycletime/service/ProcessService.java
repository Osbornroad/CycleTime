package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.model.Process;
import java.util.List;

/**
 * Created by User on 27.04.2017.
 */

public interface ProcessService {
    Process get(int id);

    boolean save(Process process);

    List<Process> getAll();

    void delete(int id);

    void printAll();

    Process[] getProcessArray();
}
