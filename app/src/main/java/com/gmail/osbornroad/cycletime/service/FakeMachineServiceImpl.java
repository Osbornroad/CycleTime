/*
package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.dao.FakeMachineDaoImpl;
import com.gmail.osbornroad.cycletime.dao.MachineDao;
import com.gmail.osbornroad.cycletime.model.Machine;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

*/
/**
 * Created by User on 27.04.2017.
 *//*


public class FakeMachineServiceImpl implements MachineService {

    private MachineDao machineDao;

    public FakeMachineServiceImpl(MachineDao machineDao) {
        this.machineDao = machineDao;
    }

    @Override
    public Machine get(int id) {
        return machineDao.get(id);
    }

    @Override
    public boolean save(Machine machine) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Machine> getAll() {
        return machineDao.getAll();
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printAll() {
        List<Machine> listForPrinting = getAll();
        Collections.sort(listForPrinting, new Comparator<Machine>() {
            @Override
            public int compare(Machine o1, Machine o2) {
                return o1.getId() - o2.getId();
            }
        });
        System.out.println();
        for (Machine machine : listForPrinting) {
            System.out.println(machine);
        }
        System.out.println();
        System.out.println("*********************************************************");
    }

    @Override
    public Machine[] getMachineArray() {
        return getAll().toArray(new Machine[getAll().size()]);
    }

    public static void main(String[] args) {
        MachineService machineService = new FakeMachineServiceImpl(new FakeMachineDaoImpl());

        machineService.printAll();
        System.out.println(machineService.get(305));

    }
}
*/
