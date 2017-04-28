package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User on 27.04.2017.
 */

public class FakeMachineDaoImpl implements MachineDao {

    private List<Machine> machineList;
    private AtomicInteger idCounter = new AtomicInteger(300);
    private String[] machineNamesArray;

    public FakeMachineDaoImpl() {
        this.machineList = new ArrayList<>();
        populateMachineList();
    }

    private void populateMachineList() {
        machineNamesArray = new String[]{
                "Assy P32R Cluster #0/1",
                "Assy P32R Cluster #2",
                "Assy P32S Cluster #0/2",
                "Assy P32S Cluster #1",
                "Assy P42M Cluster",
                "Assy P32R/S",
                "Assy P42M ABS (F)",
                "Assy P42M ABS (K)",
                "Tokai P32R",
                "Tokai P32S",
                "FD92",
                "TK390",
                "TK389",
                "TK324",
                "TK323",
                "LR 297",
                "LR 296",
                "LR 252",
                "LR 251",
                "Assy 2800",
                "Add bending 2800",
                "PCO Diesel P32R",
                "PCO Diesel P32S",
                "PCO straight",
                "QCN",
                "Endform T-9",
                "Endform T-8",
                "Endform T-7",
                "Endform T-6",
                "Buffing",
                "Chamfering",
                "Laser",
                "HPC"
        };
        for (String machineName : machineNamesArray) {
            machineList.add(new Machine(getNewId(), machineName, 0, true));
        }
    }

    private int getNewId() {
        return idCounter.getAndIncrement();
    }


    @Override
    public Machine get(int id) {
        for (Machine machine : machineList) {
            if (id == machine.getId()) {
                return machine;
            }
        }
        return null;
    }

    @Override
    public boolean save(Machine machine) {
        return false;
    }

    @Override
    public List<Machine> getAll() {
        return machineList;
    }

    @Override
    public void delete(int id) {

    }


}
